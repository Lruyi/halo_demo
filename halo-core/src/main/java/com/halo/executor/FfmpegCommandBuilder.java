package com.halo.executor;


import com.halo.enums.TaskTypeEnum;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;

/**
 * @author: wangweichang@tal.com
 * @date: 2026/04/17 18:43
 * @description: FFmpeg 命令构建策略接口。每种任务类型实现此接口，由 TaskExecutorService 根据 taskType 查找对应的 builder。
 */
public interface FfmpegCommandBuilder {

    /** 该 builder 支持的任务类型 */
    TaskTypeEnum supportedType();

    /**
     * 构建 FFmpeg 命令行参数
     *
     * @param params     完整任务参数（从 DB 中读取的 JSON）
     * @param inputFiles 已下载到本地的输入文件映射：key=文件标识（如 "base_video"、"overlay_0"），value=本地路径
     * @param outputFile 输出文件本地路径
     * @param ffmpegCommand ffmpeg 命令名（如 ffmpeg）
     * @return 完整的命令行参数列表
     */
    List<String> buildCommand(Map<String, Object> params,
                              Map<String, Path> inputFiles,
                              Path outputFile,
                              String ffmpegCommand);
}
