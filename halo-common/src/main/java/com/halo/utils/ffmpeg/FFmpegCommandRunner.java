package com.halo.utils.ffmpeg;

import com.google.common.base.Stopwatch;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/**
 * FFmpeg 命令执行器
 * 负责执行 FFmpeg 命令并处理进程输出
 *
 * <p>JVM 参数说明：</p>
 * <ul>
 *     <li>aigc.ffmpeg.max-concurrency：FFmpeg 最大并发进程数，默认 2</li>
 *     <li>aigc.ffmpeg.acquire-timeout-ms：获取并发许可超时时间（毫秒），默认 180000</li>
 *     <li>aigc.ffmpeg.x264.preset：libx264 预设，默认 veryfast（见 {@link FFmpegEncodeParams}）</li>
 *     <li>aigc.ffmpeg.x264.threads：libx264 线程，未设置则不限制；可设 half、正整数、0/auto</li>
 *     <li>aigc.ffmpeg.filter.threads：全局 -filter_threads，默认不启用</li>
 * </ul>
 *
 * @author: wangweichang@tal.com
 * @date: 2025/12/17 15:51
 */
@Slf4j
public class FFmpegCommandRunner {

    /**
     * 默认 FFmpeg 最大并发进程数。
     */
    private static final int DEFAULT_MAX_CONCURRENT_PROCESSES = 2;

    /**
     * 获取并发许可默认超时时间（3分钟）。
     */
    private static final long DEFAULT_ACQUIRE_TIMEOUT_MILLIS = TimeUnit.MINUTES.toMillis(3);

    /**
     * 获取并发许可最小超时时间（1秒）。
     */
    private static final long MIN_ACQUIRE_TIMEOUT_MILLIS = TimeUnit.SECONDS.toMillis(1);

    /**
     * FFmpeg 进程并发上限，默认2，可通过 JVM 参数覆盖：-Daigc.ffmpeg.max-concurrency=1
     */
    private static final int MAX_CONCURRENT_PROCESSES = resolveMaxConcurrentProcesses();

    /**
     * 全局并发闸门，限制同一 JVM 内同时运行的 FFmpeg 进程数量
     */
    private static final Semaphore PROCESS_SEMAPHORE = new Semaphore(MAX_CONCURRENT_PROCESSES);

    /**
     * 获取 FFmpeg 并发许可的超时时间（毫秒），默认3分钟
     * 可通过 JVM 参数覆盖：-Daigc.ffmpeg.acquire-timeout-ms=120000
     */
    private static final long ACQUIRE_TIMEOUT_MILLIS = resolveAcquireTimeoutMillis();

    /**
     * 执行 FFmpeg 命令（使用默认处理器）
     *
     * @param commands 命令列表
     * @return 进程输出结果，执行失败返回 null
     */
    public static String runProcess(List<String> commands) {
        try {
            return runProcess(commands, new ProcessCallbackHandler());
        } catch (Exception e) {
            log.error("[ffmpeg] 执行命令失败, cmd: {}", FFmpegUtils.ffmpegCmdLine(commands), e);
            return null;
        }
    }

    /**
     * 执行 FFmpeg 命令（使用自定义处理器）
     *
     * @param commands 命令列表
     * @param handler  输出流处理器
     * @return 进程输出结果，执行失败返回 null
     */
    public static String runProcess(List<String> commands, ProcessCallbackHandler handler) {
        if (commands == null || commands.isEmpty()) {
            log.warn("[ffmpeg] 命令列表为空，跳过执行");
            return null;
        }

        Process process = null;
        String cmdLine = FFmpegUtils.ffmpegCmdLine(commands);
        log.info("[ffmpeg] 开始执行命令: {}", cmdLine);
        Stopwatch stopwatch = Stopwatch.createStarted();

        ProcessBuilder pb = new ProcessBuilder(commands);
        pb.redirectErrorStream(true);

        String result = null;
        int exitCode = -1;
        boolean permitAcquired = false;
        boolean acquireTimedOut = false;

        try {
            // 获取并发许可（带超时），避免请求无限等待
            permitAcquired = PROCESS_SEMAPHORE.tryAcquire(ACQUIRE_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS);
            if (!permitAcquired) {
                acquireTimedOut = true;
                log.error("[ffmpeg] 获取并发许可超时({}ms)，当前上限={}, cmd: {}",
                        ACQUIRE_TIMEOUT_MILLIS, MAX_CONCURRENT_PROCESSES, cmdLine);
            } else {
                // 启动进程
                process = pb.start();
                // TODO jdk21的命令
//                log.debug("[ffmpeg] 进程已启动, PID: {}", process.pid());

                // 读取进程输出
                result = handler.handler(process.getInputStream());

                // 等待进程结束
                exitCode = process.waitFor();

                if (exitCode != 0) {
                    log.error("[ffmpeg] 进程执行失败, exitCode: {}, cmd: {}", exitCode, cmdLine);
                    if (result != null && log.isDebugEnabled()) {
                        log.debug("[ffmpeg] 错误输出: {}", result);
                    }
                } else {
                    log.debug("[ffmpeg] 进程执行成功");
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("[ffmpeg] 等待进程结束被中断, cmd: {}", cmdLine, e);
        } catch (IOException e) {
            log.error("[ffmpeg] 读取进程输出失败, cmd: {}", cmdLine, e);
        } catch (Exception e) {
            log.error("[ffmpeg] 执行命令时发生异常, cmd: {}", cmdLine, e);
        } finally {
            // 关闭进程资源
            closeProcessResources(process);
            stopwatch.stop();

            long elapsedSeconds = stopwatch.elapsed(TimeUnit.SECONDS);
            long elapsedMillis = stopwatch.elapsed(TimeUnit.MILLISECONDS);
            if (acquireTimedOut) {
                log.warn("[ffmpeg] 命令未执行，原因: 获取并发许可超时, 耗时: {}秒 ({}毫秒), cmd: {}",
                        elapsedSeconds, elapsedMillis, cmdLine);
            } else if (exitCode == 0) {
                log.info("[ffmpeg] 命令执行完成, 耗时: {}秒 ({}毫秒), cmd: {}", elapsedSeconds, elapsedMillis, cmdLine);
            } else {
                log.warn("[ffmpeg] 命令执行完成(异常), exitCode: {}, 耗时: {}秒 ({}毫秒), cmd: {}", 
                        exitCode, elapsedSeconds, elapsedMillis, cmdLine);
            }

            if (permitAcquired) {
                PROCESS_SEMAPHORE.release();
            }
        }

        return result;
    }

    private static int resolveMaxConcurrentProcesses() {
        String raw = System.getProperty("aigc.ffmpeg.max-concurrency");
        if (StringUtils.isBlank(raw)) {
            return DEFAULT_MAX_CONCURRENT_PROCESSES;
        }
        try {
            int parsed = Integer.parseInt(raw);
            return Math.max(1, parsed);
        } catch (NumberFormatException e) {
            log.warn("[ffmpeg] aigc.ffmpeg.max-concurrency 非法: {}, 使用默认值{}", raw, DEFAULT_MAX_CONCURRENT_PROCESSES);
            return DEFAULT_MAX_CONCURRENT_PROCESSES;
        }
    }

    private static long resolveAcquireTimeoutMillis() {
        String raw = System.getProperty("aigc.ffmpeg.acquire-timeout-ms");
        if (StringUtils.isBlank(raw)) {
            return DEFAULT_ACQUIRE_TIMEOUT_MILLIS;
        }
        try {
            long parsed = Long.parseLong(raw);
            return Math.max(MIN_ACQUIRE_TIMEOUT_MILLIS, parsed);
        } catch (NumberFormatException e) {
            log.warn("[ffmpeg] aigc.ffmpeg.acquire-timeout-ms 非法: {}, 使用默认值{}ms", raw, DEFAULT_ACQUIRE_TIMEOUT_MILLIS);
            return DEFAULT_ACQUIRE_TIMEOUT_MILLIS;
        }
    }

    /**
     * 关闭进程相关资源
     *
     * @param process 进程对象
     */
    private static void closeProcessResources(Process process) {
        if (process == null) {
            return;
        }

        try {
            // 关闭输入流
            if (process.getInputStream() != null) {
                process.getInputStream().close();
            }
        } catch (IOException e) {
            log.warn("[ffmpeg] 关闭输入流失败", e);
        }

        try {
            // 关闭输出流
            if (process.getOutputStream() != null) {
                process.getOutputStream().close();
            }
        } catch (IOException e) {
            log.warn("[ffmpeg] 关闭输出流失败", e);
        }

        try {
            // 关闭错误流
            if (process.getErrorStream() != null) {
                process.getErrorStream().close();
            }
        } catch (IOException e) {
            log.warn("[ffmpeg] 关闭错误流失败", e);
        }

        // 销毁进程
        if (process.isAlive()) {
            process.destroy();
            log.debug("[ffmpeg] 进程已销毁");
        }
    }
}
