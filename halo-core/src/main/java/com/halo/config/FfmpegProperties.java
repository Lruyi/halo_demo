package com.halo.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author:
 * @date: 2026/04/17 18:43
 * @description:
 */

@Data
@Component
@ConfigurationProperties(prefix = "ffmpeg")
public class FfmpegProperties {

    /** FFmpeg 可执行文件路径（当前保留备用，默认执行依赖 PATH 中的 ffmpeg） */
    private String binPath = "/usr/local/bin/ffmpeg";

    /** ffprobe 可执行文件路径（当前保留备用，默认执行依赖 PATH 中的 ffprobe） */
    private String ffprobePath = "/usr/local/bin/ffprobe";

    /** 任务执行临时目录 */
    private String tmpDir = "/tmp/data/ffmpeg-tmp";

    /** 最大并发执行任务数 */
    private int maxConcurrency = 3;

    /** 任务等待队列最大长度 */
    private int maxQueueSize = 50;
}
