package com.halo.executor;

import com.halo.config.FfmpegDynamicProperties;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 根据全局配置与任务参数解析最终视频编码方式（是否走 GPU 硬件编码）。
 */
@Component
public class FfmpegGpuAccelResolver {

    @Resource
    private FfmpegDynamicProperties ffmpegDynamicProperties;

    /**
     * @param params 任务完整 JSON（含 options），与 {@link com.xes.mplat.ffmpeg.executor.FfmpegCommandBuilder#buildCommand} 一致
     * @return 传入 {@link FfmpegVideoEncodeArgs#forVideoEncoder} 的值：none / nvenc / videotoolbox / qsv
     */
    public String resolveVideoHwEncoder(Map<String, Object> params) {
        Boolean taskPref = readTaskUseGpuAccel(params);
        if (Boolean.FALSE.equals(taskPref)) {
            return "none";
        }
        if (!ffmpegDynamicProperties.isGpuAccelEnabled()) {
            return "none";
        }
        String configured = ffmpegDynamicProperties.getVideoHwEncoder();
        if (configured == null || configured.isBlank()) {
            return "none";
        }
        return configured.trim();
    }

    private static Boolean readTaskUseGpuAccel(Map<String, Object> params) {
        if (params == null) {
            return null;
        }
        Object options = params.get("options");
        if (!(options instanceof Map<?, ?> map)) {
            return null;
        }
        Object v = map.get("useGpuAccel");
        if (v instanceof Boolean b) {
            return b;
        }
        if (v == null) {
            return null;
        }
        if (v instanceof Number n) {
            return n.intValue() != 0;
        }
        return Boolean.parseBoolean(v.toString());
    }
}
