package com.halo.executor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;
import java.util.concurrent.*;

/**
 * @author:
 * @date: 2026/04/17 18:43
 * @description: FFmpeg 进程执行器。职责：启动 FFmpeg 子进程、读取 stderr、超时 watchdog、确保进程回收。
 */
@Slf4j
@Component
public class FfmpegProcessRunner {

    private final ScheduledExecutorService watchdogScheduler = Executors.newScheduledThreadPool(2, r -> {
        Thread t = new Thread(r, "ffmpeg-watchdog");
        t.setDaemon(true);
        return t;
    });

    /**
     * 执行 FFmpeg 命令
     *
     * @param command        完整命令行参数（直接使用 ffmpeg 命令名或可执行路径）
     * @param timeoutSeconds 超时时间（秒）
     * @param taskId         任务ID（仅用于日志）
     * @return 执行结果
     */
    public ProcessResult run(List<String> command, long timeoutSeconds, Long taskId) {
        log.info("[FFmpeg] taskId={}, 开始执行命令: {}", taskId, String.join(" ", command));

        Process process = null;
        ScheduledFuture<?> watchdog = null;
        boolean timeout = false;

        try {
            ProcessBuilder pb = new ProcessBuilder(command);
            pb.redirectErrorStream(false); // stderr 单独读取
            process = pb.start();

            long pid = process.pid();
            log.info("[FFmpeg] taskId={}, 进程已启动, PID={}", taskId, pid);

            // 异步读取 stderr
            Process finalProcess = process;
            Future<String> stderrFuture = CompletableFuture.supplyAsync(() -> readStream(finalProcess));

            // 启动 watchdog 超时线程
            Process watchedProcess = process;
            watchdog = watchdogScheduler.schedule(() -> {
                if (watchedProcess.isAlive()) {
                    log.warn("[FFmpeg] taskId={}, 进程超时（{}s），强制终止 PID={}", taskId, timeoutSeconds, pid);
                    watchedProcess.destroyForcibly();
                }
            }, timeoutSeconds, TimeUnit.SECONDS);

            // 等待进程结束
            int exitCode = process.waitFor();
            watchdog.cancel(false);

            String stderrOutput = stderrFuture.get(5, TimeUnit.SECONDS);

            // 判断是否因超时被 kill
            if (exitCode != 0 && !process.isAlive()) {
                // 检查是否是被 watchdog 杀掉的（exitCode 通常为 137 或负值）
                timeout = (exitCode == 137 || exitCode < 0);
            }

            log.info("[FFmpeg] taskId={}, 进程结束, exitCode={}, timeout={}", taskId, exitCode, timeout);
            return new ProcessResult(exitCode, stderrOutput, timeout, pid);

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("[FFmpeg] taskId={}, 执行被中断", taskId);
            return new ProcessResult(-1, "执行被中断: " + e.getMessage(), false, 0);
        } catch (Exception e) {
            log.error("[FFmpeg] taskId={}, 执行异常: {}", taskId, e.getMessage(), e);
            return new ProcessResult(-1, "执行异常: " + e.getMessage(), false, 0);
        } finally {
            // 确保进程被回收，防止孤儿进程
            if (process != null && process.isAlive()) {
                log.warn("[FFmpeg] taskId={}, finally 块销毁残留进程", taskId);
                process.destroyForcibly();
            }
            if (watchdog != null) {
                watchdog.cancel(false);
            }
        }
    }

    /**
     * 使用 ffprobe 探测视频信息，返回 JSON 格式输出
     */
    public String probe(String ffprobeCommand, String filePath) throws Exception {
        List<String> cmd = List.of(
                ffprobeCommand,
                "-v", "quiet",
                "-print_format", "json",
                "-show_format",
                "-show_streams",
                filePath
        );

        ProcessBuilder pb = new ProcessBuilder(cmd);
        pb.redirectErrorStream(true);
        Process process = pb.start();

        String output;
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }
            output = sb.toString();
        }

        int exitCode = process.waitFor();
        if (exitCode != 0) {
            throw new RuntimeException("ffprobe 返回非零退出码: " + exitCode + ", output: " + output);
        }
        return output;
    }

    private String readStream(Process process) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getErrorStream()))) {
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }
            return sb.toString();
        } catch (Exception e) {
            return "读取 stderr 失败: " + e.getMessage();
        }
    }

    /**
     * @author: wangweichang@tal.com
     * @date: 2026/04/17 18:43
     * @description: 进程执行结果
     */
    public record ProcessResult(int exitCode, String stderrOutput, boolean timeout, long pid) {
        public boolean isSuccess() {
            return exitCode == 0;
        }
    }
}
