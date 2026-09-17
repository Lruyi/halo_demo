package com.halo.executor.task;

import com.halo.enums.TaskTypeEnum;
import com.halo.executor.FfmpegCommandBuilder;
import com.halo.executor.FfmpegGpuAccelResolver;
import com.halo.executor.FfmpegVideoEncodeArgs;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author:
 * @date: 2026/04/17 18:43
 * @description: VIDEO_SPEED_ADJUST — 视频调速。根据目标时长计算速度比，调整视频 setpts 和音频 atempo。
 */
@Slf4j
@Component
public class VideoSpeedAdjustCommandBuilder implements FfmpegCommandBuilder {

    @Resource
    private FfmpegGpuAccelResolver gpuAccelResolver;

    @Override
    public TaskTypeEnum supportedType() {
        return TaskTypeEnum.VIDEO_SPEED_ADJUST;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<String> buildCommand(Map<String, Object> params,
                                     Map<String, Path> inputFiles,
                                     Path outputFile,
                                     String ffmpegCommand) {
        Map<String, Object> options = (Map<String, Object>) params.getOrDefault("options", Map.of());
        int targetDurationMs = getInt(options, "targetDurationMs", 0);
        if (targetDurationMs <= 0) {
            throw new IllegalArgumentException("targetDurationMs 必须大于 0");
        }

        // speedRatio 需要在 TaskExecutorService 中根据 probe 结果计算
        // 这里从 params 中获取（由 TaskExecutorService 注入）
        double speedRatio = getDouble(params, "_speedRatio", 1.0);

        log.info("[SPEED_ADJUST] speedRatio={}, targetDurationMs={}", speedRatio, targetDurationMs);

        List<String> cmd = new ArrayList<>();
        cmd.add(ffmpegCommand);
        cmd.addAll(List.of("-filter_threads", "2"));
        cmd.addAll(List.of("-i", inputFiles.get("video_0").toString()));

        // 构建 filter_complex
        String videoPts = String.format("1/%.4f", speedRatio);
        String audioFilter = buildAtempoFilter(speedRatio);
        String filterComplex = String.format("[0:v]setpts=%s*PTS[v];[0:a]%s[a]", videoPts, audioFilter);

        cmd.addAll(List.of("-filter_complex", filterComplex));
        cmd.addAll(List.of("-map", "[v]", "-map", "[a]"));
        cmd.addAll(FfmpegVideoEncodeArgs.forVideoEncoder(gpuAccelResolver.resolveVideoHwEncoder(params)));
        cmd.addAll(List.of("-c:a", "aac"));
        cmd.addAll(List.of("-y", outputFile.toString()));

        return cmd;
    }

    /**
     * 构建 atempo 滤镜链
     * atempo 范围限制为 [0.5, 2.0]，超出范围则链式叠加
     * 例如 speedRatio=3.0 → atempo=2.0,atempo=1.5
     */
    String buildAtempoFilter(double speedRatio) {
        List<String> atempos = new ArrayList<>();
        double remaining = speedRatio;

        while (remaining > 2.0) {
            atempos.add("atempo=2.0");
            remaining /= 2.0;
        }
        while (remaining < 0.5) {
            atempos.add("atempo=0.5");
            remaining /= 0.5;
        }
        atempos.add(String.format("atempo=%.4f", remaining));

        return String.join(",", atempos);
    }

    /** 判断速度比是否接近1（±1%），用于跳过转码 */
    public static boolean isSpeedRatioNearOne(double speedRatio) {
        return speedRatio >= 0.99 && speedRatio <= 1.01;
    }

    private int getInt(Map<String, Object> map, String key, int defaultValue) {
        Object val = map.get(key);
        if (val instanceof Number num) {
            return num.intValue();
        }
        return defaultValue;
    }

    private double getDouble(Map<String, Object> map, String key, double defaultValue) {
        Object val = map.get(key);
        if (val instanceof Number num) {
            return num.doubleValue();
        }
        return defaultValue;
    }
}
