package com.halo.executor;

import java.util.List;

/**
 * 根据配置生成视频编码相关 FFmpeg 参数（侧重硬件编码以减轻 CPU）。
 */
public final class FfmpegVideoEncodeArgs {

    private FfmpegVideoEncodeArgs() {
    }

    /**
     * @param videoHwEncoder 配置值：none、nvenc、videotoolbox、qsv（大小写不敏感）
     */
    public static List<String> forVideoEncoder(String videoHwEncoder) {
        if (videoHwEncoder == null || videoHwEncoder.isBlank()) {
            return softwareArgs();
        }
        return switch (videoHwEncoder.trim().toLowerCase()) {
            case "nvenc" -> List.of(
                    "-c:v", "h264_nvenc",
                    "-preset", "p4",
                    "-rc", "vbr",
                    "-cq", "28",
                    "-b:v", "0",
                    "-video_track_timescale", "12800"
            );
            case "videotoolbox" -> List.of(
                    "-c:v", "h264_videotoolbox",
                    "-b:v", "8M",
                    "-video_track_timescale", "12800"
            );
            case "qsv" -> List.of(
                    "-c:v", "h264_qsv",
                    "-preset", "medium",
                    "-look_ahead", "1",
                    "-video_track_timescale", "12800"
            );
            default -> softwareArgs();
        };
    }

    private static List<String> softwareArgs() {
        return List.of("-c:v", "libx264", "-preset", "veryfast", "-threads", "2",
                "-video_track_timescale", "12800");
    }
}
