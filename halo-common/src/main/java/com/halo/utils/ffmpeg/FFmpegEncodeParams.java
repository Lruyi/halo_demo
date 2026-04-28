package com.halo.utils.ffmpeg;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * FFmpeg 视频编码侧可调参数（libx264 及滤镜线程），通过 JVM 系统属性配置，无需改代码即可按环境调优。
 * <p>
 * 与 {@link FFmpegCommandRunner} 一致，在启动参数中传入，例如：
 * {@code -Daigc.ffmpeg.x264.preset=fast -Daigc.ffmpeg.x264.threads=half}
 * </p>
 * <p><b>耗时与 CPU 关系（简要）</b>：</p>
 * <ul>
 *     <li>{@code -preset} 从 {@code medium} 调到 {@code veryfast/faster}：通常<b>缩短</b>墙钟时间、降低瞬时 CPU 强度，码率/画质需业务验收。</li>
 *     <li>{@code -threads} 限制过小：通常<b>拉长</b>墙钟时间，但单进程 CPU 峰值下降，适合与 K8s CPU limit 配合。</li>
 *     <li>{@code -filter_threads}：限制滤镜并行，略降 CPU 峰值，复杂滤镜场景可能略增耗时。</li>
 * </ul>
 *
 * @author: wangweichang@tal.com
 * @date: 2026/04/02
 * @description: 解析 FFmpeg/libx264 编码与全局滤镜线程相关的 JVM 系统属性，在构建命令时注入 {@code -preset}、{@code -threads}、{@code -filter_threads} 等参数，便于按环境限制 CPU 与调优转码耗时。
 */
@Slf4j
public final class FFmpegEncodeParams {

    /**
     * libx264 预设，默认 veryfast（服务端批量转码常用折中：较快、CPU 压力相对可控）。
     */
    private static final String PROP_X264_PRESET = "aigc.ffmpeg.x264.preset";

    /**
     * libx264 编码线程数。未设置：不追加 -threads，由 FFmpeg/x264 默认（多核拉满）。
     * 支持：正整数；{@code half} 表示 max(1, 逻辑核数/2)；{@code 0} 或 {@code auto} 表示不传 -threads。
     */
    private static final String PROP_X264_THREADS = "aigc.ffmpeg.x264.threads";

    /**
     * 全局滤镜线程上限，对应 FFmpeg {@code -filter_threads}，仅在大于 0 时插入在 {@code ffmpeg} 之后。
     */
    private static final String PROP_FILTER_THREADS = "aigc.ffmpeg.filter.threads";

    private static final String DEFAULT_PRESET = "veryfast";

    private static final String X264_PRESET = resolvePreset();
    private static final Integer X264_THREADS = resolveX264Threads();
    private static final int FILTER_THREADS = resolveFilterThreads();

    static {
        log.info("[ffmpeg] 编码参数: x264.preset={}, x264.threads={}, filter.threads={}",
                X264_PRESET,
                X264_THREADS == null ? "(默认)" : X264_THREADS,
                FILTER_THREADS <= 0 ? "(未启用)" : FILTER_THREADS);
    }

    private FFmpegEncodeParams() {
    }

    private static String resolvePreset() {
        String raw = System.getProperty(PROP_X264_PRESET);
        if (StringUtils.isBlank(raw)) {
            return DEFAULT_PRESET;
        }
        return raw.trim();
    }

    private static Integer resolveX264Threads() {
        String raw = System.getProperty(PROP_X264_THREADS);
        if (StringUtils.isBlank(raw)) {
            return null;
        }
        raw = raw.trim();
        if ("0".equals(raw) || "auto".equalsIgnoreCase(raw)) {
            return null;
        }
        if ("half".equalsIgnoreCase(raw)) {
            return Math.max(1, Runtime.getRuntime().availableProcessors() / 2);
        }
        try {
            int v = Integer.parseInt(raw);
            if (v <= 0) {
                return null;
            }
            return v;
        } catch (NumberFormatException e) {
            log.warn("[ffmpeg] {} 非法: {}，将不传入 -threads", PROP_X264_THREADS, raw);
            return null;
        }
    }

    private static int resolveFilterThreads() {
        String raw = System.getProperty(PROP_FILTER_THREADS);
        if (StringUtils.isBlank(raw)) {
            return 0;
        }
        try {
            return Math.max(0, Integer.parseInt(raw.trim()));
        } catch (NumberFormatException e) {
            log.warn("[ffmpeg] {} 非法: {}", PROP_FILTER_THREADS, raw);
            return 0;
        }
    }

    /**
     * 在命令列表开头为 ffmpeg 时，于其后插入全局 {@code -filter_threads N}（若已配置 N&gt;0）。
     * 适用于含 {@code -filter_complex} 或 {@code -vf} 的命令。
     */
    public static void insertGlobalOptionsAfterFfmpeg(List<String> commands) {
        if (commands == null || commands.isEmpty() || FILTER_THREADS <= 0) {
            return;
        }
        if (!"ffmpeg".equals(commands.get(0))) {
            return;
        }
        commands.add(1, Integer.toString(FILTER_THREADS));
        commands.add(1, "-filter_threads");
    }

    /**
     * 在已写入 {@code -c:v} {@code libx264} 之后调用，追加 {@code -preset} 与可选 {@code -threads}。
     */
    public static void appendLibx264Tuning(List<String> commands) {
        if (commands == null) {
            return;
        }
        commands.add("-preset");
        commands.add(X264_PRESET);
        if (X264_THREADS != null) {
            commands.add("-threads");
            commands.add(Integer.toString(X264_THREADS));
        }
    }
}
