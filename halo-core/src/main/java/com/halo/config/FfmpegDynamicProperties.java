package com.halo.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

/**
 * 可通过配置中心热刷新的 FFmpeg 运行期参数。
 */
@Data
@Component
@RefreshScope
@ConfigurationProperties(prefix = "ffmpeg.dynamic")
public class FfmpegDynamicProperties {

    /** 默认任务超时时间（秒） */
    private int defaultTimeoutSeconds = 300;

    /** 默认最大重试次数 */
    private int defaultMaxRetry = 2;

    /** 磁盘空间最低阈值（GB） */
    private int minFreeDiskGb = 10;

    /** 临时文件保留时间（小时） */
    private int tmpFileRetainHours = 24;

    /**
     * 是否允许使用 GPU 视频编码。为 false 时始终使用软编（libx264），忽略 {@link #videoHwEncoder}。
     */
    private boolean gpuAccelEnabled = false;

    /**
     * 在 {@link #gpuAccelEnabled} 为 true 时生效：视频硬件编码后端
     * none（libx264）、nvenc（NVIDIA）、videotoolbox（macOS）、qsv（Intel）。
     * 需与本机 FFmpeg 编译选项及硬件一致。
     */
    private String videoHwEncoder = "none";

    /** 图片叠加任务的默认缩放边长上限（px） */
    private int defaultOverlayImageMaxSize = 720;

    /** 转场效果名称（xfade filter），VIDEO_TRANSITION 使用，如 fade、wipeleft */
    private String transitionType = "fade";

    /** 转场时长（ms），VIDEO_TRANSITION 使用 */
    private Integer transitionDurationMs = 800;
}
