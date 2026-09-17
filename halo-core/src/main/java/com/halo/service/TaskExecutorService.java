package com.halo.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.halo.config.FfmpegProperties;
import com.halo.dto.CallbackRequestDTO;
import com.halo.entity.FfmpegTask;
import com.halo.entity.FfmpegTaskLog;
import com.halo.enums.InputRoleEnum;
import com.halo.enums.TaskStatusEnum;
import com.halo.enums.TaskTypeEnum;
import com.halo.executor.FfmpegCommandBuilder;
import com.halo.executor.FfmpegExecutableCommands;
import com.halo.executor.FfmpegProcessRunner;
import com.halo.executor.task.VideoSpeedAdjustCommandBuilder;
import com.halo.mapper.FfmpegTaskLogMapper;
import com.halo.mapper.FfmpegTaskMapper;
import com.halo.storage.FileDownloader;
import com.halo.storage.FileUploader;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Semaphore;

/**
 * @author:
 * @date: 2026/04/17 18:43
 * @description: 任务执行调度服务。核心调度逻辑：从队列取任务 → 下载文件 → 构建命令 → 执行 FFmpeg → 上传结果 → 更新状态 → 回调。
 */
@Slf4j
@Service
public class TaskExecutorService {

    private static final DateTimeFormatter OUTPUT_TS_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS");
    private static final int FAIL_REASON_MAX_LEN = 2000;
    private static final int TASK_LOG_MAX_LEN = 5000;
    private static final int STDERR_RETRY_LOG_MAX_LEN = 2000;

    @Resource
    private FfmpegProperties ffmpegProperties;
    @Resource
    private TaskQueueManager taskQueueManager;
    @Resource
    private FfmpegTaskMapper taskMapper;
    @Resource
    private FfmpegTaskLogMapper taskLogMapper;
    @Resource
    private FileDownloader fileDownloader;
    @Resource
    private FileUploader fileUploader;
    @Resource
    private FfmpegProcessRunner processRunner;
    @Resource
    private CallbackService callbackService;
    @Resource
    private ObjectMapper objectMapper;
    @Resource
    @Qualifier("ffmpegExecutor")
    private ExecutorService ffmpegExecutor;

    /** 所有 FfmpegCommandBuilder 按 TaskTypeEnum 索引 */
    private Map<TaskTypeEnum, FfmpegCommandBuilder> commandBuilders;

    private Semaphore concurrencySlots;

    @Resource
    public void setCommandBuilders(List<FfmpegCommandBuilder> builders) {
        this.commandBuilders = new HashMap<>();
        for (FfmpegCommandBuilder builder : builders) {
            commandBuilders.put(builder.supportedType(), builder);
        }
    }

    @PostConstruct
    public void init() {
        this.concurrencySlots = new Semaphore(ffmpegProperties.getMaxConcurrency());
        // 启动调度循环线程
        Thread dispatcher = new Thread(this::dispatchLoop, "task-dispatcher");
        dispatcher.setDaemon(true);
        dispatcher.start();
        log.info("[调度] 启动完成, maxConcurrency={}", ffmpegProperties.getMaxConcurrency());
    }

    /**
     * 调度循环：不断从队列取任务并提交到线程池执行
     */
    private void dispatchLoop() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                // 阻塞等待队列中有任务
                FfmpegTask task = taskQueueManager.take();
                // 获取并发槽位（阻塞直到有空闲槽位）
                concurrencySlots.acquire();
                // 提交到线程池执行
                ffmpegExecutor.submit(() -> executeTask(task));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                log.info("[调度] 调度线程被中断，退出");
                break;
            } catch (Exception e) {
                log.error("[调度] 调度循环异常: {}", e.getMessage(), e);
            }
        }
    }

    /**
     * 执行单个任务的完整流程
     */
    private void executeTask(FfmpegTask task) {
        Long taskId = task.getId();
        log.info("[执行] 开始执行任务 taskId={}, taskType={}", taskId, task.getTaskType());

        try {
            // 1. 更新状态为 RUNNING
            task.setStatus(TaskStatusEnum.RUNNING.getCode());
            task.setStartTime(LocalDateTime.now());
            taskMapper.updateById(task);
            saveLog(taskId, "START", "任务开始执行");

            // 2. 解析参数
            Map<String, Object> params = task.getParams();
            List<Map<String, Object>> inputs = objectMapper.convertValue(
                    params.get("inputs"), new TypeReference<>() {});
            Map<String, Object> output = objectMapper.convertValue(
                    params.get("output"), new TypeReference<>() {});

            // 3. 下载输入文件
            Map<String, Path> inputFiles = downloadInputFiles(taskId, inputs);

            // 4. 准备输出文件路径
            String format = (String) output.get("format");
            if (StringUtils.isBlank(format)) {
                throw new IllegalArgumentException("output.format 不能为空");
            }
            String outputCosPath = buildOutputCosPath(taskId, (String) output.get("path"), format);
            Path outputDir = fileDownloader.getTaskOutputDir(taskId);
            Path outputFile = outputDir.resolve("output." + format);

            // 5. VIDEO_SPEED_ADJUST 特殊处理：需要先 probe 获取原始时长
            if (StringUtils.equals(task.getTaskType(), TaskTypeEnum.VIDEO_SPEED_ADJUST.getCode())) {
                handleSpeedAdjust(task, params, inputFiles, outputFile, outputCosPath);
                return;
            }

            // 5a. VIDEO_SEGMENT_REPLACE 特殊处理：需要先 probe 底层视频时长
            if (StringUtils.equals(task.getTaskType(), TaskTypeEnum.VIDEO_SEGMENT_REPLACE.getCode())) {
                handleVideoSegmentReplace(task, params, inputFiles, outputFile, outputCosPath);
                return;
            }

            // 5b. VIDEO_TRANSITION 特殊处理：需要 probe 每段视频时长以计算 xfade offset
            if (StringUtils.equals(task.getTaskType(), TaskTypeEnum.VIDEO_TRANSITION.getCode())) {
                handleVideoTransition(task, params, inputFiles, outputFile, outputCosPath);
                return;
            }

            // 6. 构建并执行 FFmpeg 命令
            FfmpegCommandBuilder builder = commandBuilders.get(TaskTypeEnum.getByCode(task.getTaskType()));
            if (builder == null) {
                failTask(task, "不支持的任务类型: " + task.getTaskType());
                return;
            }

            executeCommand(task, builder, params, inputFiles, outputFile, outputCosPath);

        } catch (Exception e) {
            log.error("[执行] taskId={}, 执行异常: {}", taskId, e.getMessage(), e);
            failTask(task, "执行异常: " + e.getMessage());
        } finally {
            concurrencySlots.release();
            // 回调（在 Semaphore 释放之后，不占用并发槽位）
            doCallback(task);
            // 清理临时文件
            cleanTempFiles(taskId);
        }
    }

    /**
     * VIDEO_SPEED_ADJUST 特殊处理
     */
    private void handleSpeedAdjust(FfmpegTask task, Map<String, Object> params,
                                   Map<String, Path> inputFiles, Path outputFile,
                                   String outputCosPath) throws Exception {
        Long taskId = task.getId();
        Map<String, Object> options = objectMapper.convertValue(
                params.get("options"), new TypeReference<>() {});
        int targetDurationMs = ((Number) options.get("targetDurationMs")).intValue();

        // probe 获取原始时长
        String probeJson = processRunner.probe(FfmpegExecutableCommands.FFPROBE,
                inputFiles.get("video_0").toString());
        long originalDurationMs = parseDurationMs(probeJson);

        double speedRatio = (double) originalDurationMs / targetDurationMs;
        log.info("[SPEED_ADJUST] taskId={}, originalDurationMs={}, targetDurationMs={}, speedRatio={}",
                taskId, originalDurationMs, targetDurationMs, speedRatio);

        // 速度比接近 1 时跳过转码
        if (VideoSpeedAdjustCommandBuilder.isSpeedRatioNearOne(speedRatio)) {
            log.info("[SPEED_ADJUST] taskId={}, speedRatio≈1, 跳过转码", taskId);
            String resultUrl = fileUploader.upload(inputFiles.get("video_0").toFile(), outputCosPath);
            task.setStatus(TaskStatusEnum.SUCCESS.getCode());
            task.setResultUrl(resultUrl);
            task.setDurationMs(originalDurationMs);
            task.setEndTime(LocalDateTime.now());
            taskMapper.updateById(task);
            saveLog(taskId, "SUCCESS", "跳过转码（speedRatio≈1）, resultUrl=" + resultUrl);
            return;
        }

        // 注入 speedRatio 到 params
        params.put("_speedRatio", speedRatio);

        FfmpegCommandBuilder builder = commandBuilders.get(TaskTypeEnum.VIDEO_SPEED_ADJUST);
        executeCommand(task, builder, params, inputFiles, outputFile, outputCosPath);
    }

    /**
     * VIDEO_SEGMENT_REPLACE 特殊处理：
     * probe 底层视频与各覆盖视频的实际时长，注入 _baseDurationMs 和 _overlayDurationMs_i 后交由 builder 处理。
     */
    @SuppressWarnings("unchecked")
    private void handleVideoSegmentReplace(FfmpegTask task, Map<String, Object> params,
                                           Map<String, Path> inputFiles, Path outputFile,
                                           String outputCosPath) throws Exception {
        // probe 底层视频时长
        String baseProbeJson = processRunner.probe(FfmpegExecutableCommands.FFPROBE,
                inputFiles.get("base_video").toString());
        long baseDurationMs = parseDurationMs(baseProbeJson);
        VideoStreamMeta baseVideoMeta = parseVideoStreamMeta(baseProbeJson);
        log.info("[SEGMENT_REPLACE] taskId={}, baseDurationMs={}", task.getId(), baseDurationMs);
        params.put("_baseDurationMs", baseDurationMs);
        params.put("_baseWidth", baseVideoMeta.width());
        params.put("_baseHeight", baseVideoMeta.height());
        params.put("_hasAudio", hasAudioStream(baseProbeJson));

        // probe 各覆盖视频时长（按 startMs 升序，与 builder 排序一致）
        List<Map<String, Object>> allInputs = (List<Map<String, Object>>) params.get("inputs");
        List<Map<String, Object>> overlays = allInputs.stream()
                .filter(input -> InputRoleEnum.OVERLAY_VIDEO == InputRoleEnum.fromRow(input))
                .sorted(Comparator.comparingInt(i -> getIntFromMap(i, "startMs")))
                .toList();
        for (int i = 0; i < overlays.size(); i++) {
            String overlayProbeJson = processRunner.probe(FfmpegExecutableCommands.FFPROBE,
                    inputFiles.get("overlay_" + i).toString());
            long overlayDurationMs = parseDurationMs(overlayProbeJson);
            log.info("[SEGMENT_REPLACE] taskId={}, overlay_{} durationMs={}", task.getId(), i, overlayDurationMs);
            params.put("_overlayDurationMs_" + i, overlayDurationMs);
        }

        FfmpegCommandBuilder builder = commandBuilders.get(TaskTypeEnum.VIDEO_SEGMENT_REPLACE);
        executeCommand(task, builder, params, inputFiles, outputFile, outputCosPath);
    }

    /**
     * VIDEO_TRANSITION 特殊处理：
     * probe 每段视频实际时长，注入 _videoDurationMs_i 后交由 builder 计算 xfade offset。
     */
    @SuppressWarnings("unchecked")
    private void handleVideoTransition(FfmpegTask task, Map<String, Object> params,
                                       Map<String, Path> inputFiles, Path outputFile,
                                       String outputCosPath) throws Exception {
        List<Map<String, Object>> allInputs = (List<Map<String, Object>>) params.get("inputs");
        List<Map<String, Object>> videoInputs = allInputs.stream()
                .filter(input -> InputRoleEnum.VIDEO == InputRoleEnum.fromRow(input))
                .sorted(Comparator.comparingInt(i -> getIntFromMap(i, "seq")))
                .toList();

        String probeJson0 = null;
        for (int i = 0; i < videoInputs.size(); i++) {
            String probeJson = processRunner.probe(FfmpegExecutableCommands.FFPROBE,
                    inputFiles.get("video_" + i).toString());
            long durationMs = parseDurationMs(probeJson);
            String fps = parseVideoFps(probeJson);
            log.info("[VIDEO_TRANSITION] taskId={}, video_{} durationMs={} fps={}", task.getId(), i, durationMs, fps);
            params.put("_videoDurationMs_" + i, durationMs);
            params.put("_videoFps_" + i, fps);
            if (i == 0) {
                probeJson0 = probeJson;
                params.put("_hasAudio", hasAudioStream(probeJson));
            }
        }

        if (inputFiles.containsKey("transition_video")) {
            String transProbeJson = processRunner.probe(FfmpegExecutableCommands.FFPROBE,
                    inputFiles.get("transition_video").toString());
            long transitionDurationMs = parseDurationMs(transProbeJson);
            if (transitionDurationMs == 0) {
                throw new IllegalArgumentException("转场 MOV 时长 probe 失败或为 0");
            }
            params.put("_transitionVideoDurationMs", transitionDurationMs);
            log.info("[VIDEO_TRANSITION] taskId={}, transitionVideo durationMs={}", task.getId(), transitionDurationMs);

            VideoStreamMeta meta0 = parseVideoStreamMeta(probeJson0);
            params.put("_videoWidth_0", meta0.width());
            params.put("_videoHeight_0", meta0.height());
        }

        FfmpegCommandBuilder builder = commandBuilders.get(TaskTypeEnum.VIDEO_TRANSITION);
        executeCommand(task, builder, params, inputFiles, outputFile, outputCosPath);
    }

    private int getIntFromMap(Map<String, Object> map, String key) {
        Object val = map.get(key);
        return val instanceof Number n ? n.intValue() : 0;
    }

    private void executeCommand(FfmpegTask task, FfmpegCommandBuilder builder,
                                Map<String, Object> params, Map<String, Path> inputFiles,
                                Path outputFile, String outputCosPath) throws Exception {
        List<String> command = builder.buildCommand(
                params, inputFiles, outputFile, FfmpegExecutableCommands.FFMPEG);
        task.setFfmpegCmd(String.join(" ", command));
        taskMapper.updateById(task);

        FfmpegProcessRunner.ProcessResult result = processRunner.run(command, task.getTimeoutSeconds(), task.getId());
        task.setPid((int) result.pid());

        if (result.timeout()) {
            timeoutTask(task);
        } else if (result.isSuccess()) {
            successTask(task, outputFile, outputCosPath);
        } else {
            handleFailure(task, result);
        }
    }

    /** 下载所有输入文件，返回文件标识 → 本地路径的映射 */
    private Map<String, Path> downloadInputFiles(Long taskId, List<Map<String, Object>> inputs) throws Exception {
        Map<String, Path> files = new LinkedHashMap<>();
        int videoIdx = 0, overlayIdx = 0, imageIdx = 0, audioIdx = 0, subtitleIdx = 0;

        for (Map<String, Object> input : inputs) {
            Object roleObj = input.get("role");
            InputRoleEnum role = InputRoleEnum.fromObject(roleObj);
            String url = (String) input.get("url");
            String fileName = extractFileName(url);

            if (role == InputRoleEnum.FONT) {
                // 字体下载到 font 目录
                Path fontDir = Path.of(fileDownloader.getTaskWorkDir(taskId).toString(), "fonts");
                Files.createDirectories(fontDir);
                fileDownloader.download(url, taskId, "fonts/" + fileName);
                files.put("font_dir", fontDir);
                continue;
            }

            final String key;
            if (role != null) {
                key = switch (role) {
                    case BASE_VIDEO -> "base_video";
                    case OVERLAY_VIDEO -> "overlay_" + overlayIdx++;
                    case VIDEO -> "video_" + videoIdx++;
                    case IMAGE -> "image_" + imageIdx++;
                    case AUDIO -> "audio_" + audioIdx++;
                    case SUBTITLE -> "subtitle_" + subtitleIdx++;
                    case TRANSITION_VIDEO -> "transition_video";
                    case FONT -> throw new IllegalStateException("FONT 应在前面分支处理");
                };
            } else {
                String legacy = InputRoleEnum.legacyKeySuffix(roleObj);
                if (legacy == null) {
                    throw new IllegalArgumentException("inputs.role 不能为空或无法解析");
                }
                key = legacy;
            }
            Path localFile = fileDownloader.download(url, taskId, fileName);
            files.put(key, localFile);
        }
        return files;
    }

    private void successTask(FfmpegTask task, Path outputFile, String outputCosPath) {
        try {
            // 上传到 COS
            String resultUrl = fileUploader.upload(outputFile.toFile(), outputCosPath);
            // probe 获取输出文件时长
            Long durationMs = probeDurationSafe(outputFile);

            task.setStatus(TaskStatusEnum.SUCCESS.getCode());
            task.setResultUrl(resultUrl);
            task.setDurationMs(durationMs);
            task.setEndTime(LocalDateTime.now());
            taskMapper.updateById(task);

            saveLog(task.getId(), "SUCCESS", "resultUrl=" + resultUrl + ", durationMs=" + durationMs);
            log.info("[执行] taskId={}, 执行成功, resultUrl={}", task.getId(), resultUrl);
        } catch (Exception e) {
            log.error("[执行] taskId={}, 上传输出文件失败: {}", task.getId(), e.getMessage(), e);
            failTask(task, "上传输出文件失败: " + e.getMessage());
        }
    }

    private void failTask(FfmpegTask task, String reason) {
        task.setStatus(TaskStatusEnum.FAILED.getCode());
        task.setFailReason(truncateKeepingTail(reason, FAIL_REASON_MAX_LEN));
        task.setEndTime(LocalDateTime.now());
        taskMapper.updateById(task);
        saveLog(task.getId(), "FAILED", reason);
        log.warn("[执行] taskId={}, 执行失败: {}", task.getId(), reason);
    }

    private void timeoutTask(FfmpegTask task) {
        String reason = String.format("FFmpeg process exceeded timeout (%ds)", task.getTimeoutSeconds());
        task.setStatus(TaskStatusEnum.TIMEOUT.getCode());
        task.setFailReason(reason);
        task.setEndTime(LocalDateTime.now());
        taskMapper.updateById(task);
        saveLog(task.getId(), "TIMEOUT", reason);
        log.warn("[执行] taskId={}, 执行超时", task.getId());
    }

    private void handleFailure(FfmpegTask task, FfmpegProcessRunner.ProcessResult result) {
        // 判断是否可重试
        if (task.getRetryCount() < task.getMaxRetry()) {
            task.setRetryCount(task.getRetryCount() + 1);
            task.setStatus(TaskStatusEnum.PENDING.getCode());
            taskMapper.updateById(task);
            saveLog(task.getId(), "RETRY", "重试第 " + task.getRetryCount() + " 次, stderr=" +
                    truncateKeepingTail(result.stderrOutput(), STDERR_RETRY_LOG_MAX_LEN));
            log.info("[执行] taskId={}, 重新入队重试 ({}/{})", task.getId(), task.getRetryCount(), task.getMaxRetry());
            taskQueueManager.enqueue(task);
        } else {
            String reason = buildFfmpegFailureReason(result);
            failTask(task, reason);
        }
    }

    private void doCallback(FfmpegTask task) {
        // 只在终态时回调
        TaskStatusEnum statusEnum = TaskStatusEnum.getByCode(task.getStatus());
        if (statusEnum == null || !statusEnum.isTerminal()) {
            return;
        }
        // CANCELLED 不回调
        if (statusEnum == TaskStatusEnum.CANCELLED) {
            return;
        }
        try {
            CallbackRequestDTO body = CallbackRequestDTO.builder()
                    .taskId(task.getId())
                    .bizTaskId(task.getBizTaskId())
                    .taskType(task.getTaskType())
                    .status(statusEnum.name())
                    .resultUrl(task.getResultUrl())
                    .durationMs(task.getDurationMs())
                    .failReason(task.getFailReason())
                    .build();
            callbackService.executeCallback(task.getId(), task.getCallbackUrl(), body);
        } catch (Exception e) {
            log.error("[回调] taskId={}, 回调异常: {}", task.getId(), e.getMessage(), e);
        }
    }

    private void cleanTempFiles(Long taskId) {
        try {
            Path workDir = fileDownloader.getTaskWorkDir(taskId);
            if (Files.exists(workDir)) {
                deleteDirectory(workDir.toFile());
                log.debug("[清理] taskId={}, 临时目录已清理: {}", taskId, workDir);
            }
        } catch (Exception e) {
            log.warn("[清理] taskId={}, 清理临时文件失败: {}", taskId, e.getMessage());
        }
    }

    private void deleteDirectory(File dir) {
        File[] files = dir.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    deleteDirectory(file);
                } else {
                    file.delete();
                }
            }
        }
        dir.delete();
    }

    private void saveLog(Long taskId, String logType, String content) {
        FfmpegTaskLog log = new FfmpegTaskLog();
        log.setTaskId(taskId);
        log.setLogType(logType);
        log.setContent(truncate(content, TASK_LOG_MAX_LEN));
        taskLogMapper.insert(log);
    }

    /** probe 输出文件时长，失败不影响主流程 */
    private Long probeDurationSafe(Path file) {
        try {
            String json = processRunner.probe(FfmpegExecutableCommands.FFPROBE, file.toString());
            return parseDurationMs(json);
        } catch (Exception e) {
            log.warn("[Probe] 获取输出文件时长失败: {}", e.getMessage());
            return null;
        }
    }

    /** 从 ffprobe JSON 输出中解析时长（ms） */
    @SuppressWarnings("unchecked")
    private long parseDurationMs(String probeJson) throws Exception {
        Map<String, Object> probeResult = objectMapper.readValue(probeJson, new TypeReference<>() {});
        Map<String, Object> format = (Map<String, Object>) probeResult.get("format");
        String durationStr = (String) format.get("duration");
        return (long) (Double.parseDouble(durationStr) * 1000);
    }

    /** 从 ffprobe JSON 输出中解析第一条视频流的帧率（如 "25/1"），找不到则返回 "25/1" */
    @SuppressWarnings("unchecked")
    private String parseVideoFps(String probeJson) throws Exception {
        Map<String, Object> probeResult = objectMapper.readValue(probeJson, new TypeReference<>() {});
        List<Map<String, Object>> streams = (List<Map<String, Object>>) probeResult.get("streams");
        if (streams != null) {
            for (Map<String, Object> stream : streams) {
                if ("video".equals(stream.get("codec_type"))) {
                    Object fps = stream.get("r_frame_rate");
                    if (fps instanceof String s && !s.isBlank()) {
                        return s;
                    }
                }
            }
        }
        return "25/1";
    }

    /** 从 ffprobe JSON 输出中判断是否存在音频流 */
    @SuppressWarnings("unchecked")
    private boolean hasAudioStream(String probeJson) throws Exception {
        Map<String, Object> probeResult = objectMapper.readValue(probeJson, new TypeReference<>() {});
        List<Map<String, Object>> streams = (List<Map<String, Object>>) probeResult.get("streams");
        if (streams == null) {
            return false;
        }
        return streams.stream().anyMatch(s -> "audio".equals(s.get("codec_type")));
    }

    /** 从 ffprobe JSON 输出中解析第一条视频流的宽高 */
    @SuppressWarnings("unchecked")
    private VideoStreamMeta parseVideoStreamMeta(String probeJson) throws Exception {
        Map<String, Object> probeResult = objectMapper.readValue(probeJson, new TypeReference<>() {});
        List<Map<String, Object>> streams = (List<Map<String, Object>>) probeResult.get("streams");
        if (streams == null || streams.isEmpty()) {
            throw new IllegalArgumentException("ffprobe 未返回 streams");
        }

        for (Map<String, Object> stream : streams) {
            if (!"video".equals(stream.get("codec_type"))) {
                continue;
            }
            Object width = stream.get("width");
            Object height = stream.get("height");
            if (width instanceof Number widthNum && height instanceof Number heightNum) {
                return new VideoStreamMeta(widthNum.intValue(), heightNum.intValue());
            }
            break;
        }
        throw new IllegalArgumentException("ffprobe 未返回有效的视频宽高");
    }

    private record VideoStreamMeta(int width, int height) {}

    private String extractFileName(String url) {
        String path = url.contains("?") ? url.substring(0, url.indexOf("?")) : url;
        return path.substring(path.lastIndexOf("/") + 1);
    }

    private String buildOutputCosPath(Long taskId, String requestedPath, String format) {
        String normalizedPath = normalizeCosPath(requestedPath);
        String timestamp = OUTPUT_TS_FORMATTER.format(LocalDateTime.now());
        String extension = format.trim();

        String prefix = normalizedPath;
        String baseName = "output";
        if (!normalizedPath.endsWith("/")) {
            int lastSlash = normalizedPath.lastIndexOf('/');
            String lastSegment = lastSlash >= 0 ? normalizedPath.substring(lastSlash + 1) : normalizedPath;
            if (lastSegment.contains(".")) {
                prefix = lastSlash >= 0 ? normalizedPath.substring(0, lastSlash) : "";
                baseName = lastSegment.substring(0, lastSegment.lastIndexOf('.'));
            }
        }
        if (StringUtils.isBlank(baseName)) {
            baseName = "output";
        }

        String generatedFileName = String.format("%s-%d-%s.%s", baseName, taskId, timestamp, extension);
        return joinCosPath(prefix, String.valueOf(taskId), timestamp, generatedFileName);
    }

    private String normalizeCosPath(String path) {
        if (StringUtils.isBlank(path)) {
            return "";
        }
        String normalized = path.trim().replace("\\", "/");
        while (normalized.startsWith("/")) {
            normalized = normalized.substring(1);
        }
        while (normalized.contains("//")) {
            normalized = normalized.replace("//", "/");
        }
        if ("/".equals(normalized)) {
            return "";
        }
        return normalized;
    }

    private String joinCosPath(String... parts) {
        StringBuilder sb = new StringBuilder();
        for (String part : parts) {
            if (StringUtils.isBlank(part)) {
                continue;
            }
            String cleaned = normalizeCosPath(part);
            if (StringUtils.isBlank(cleaned)) {
                continue;
            }
            if (sb.length() > 0 && sb.charAt(sb.length() - 1) != '/') {
                sb.append('/');
            }
            sb.append(cleaned);
        }
        return sb.toString();
    }

    private String truncate(String str, int maxLen) {
        if (str == null) return null;
        return str.length() > maxLen ? str.substring(0, maxLen) + "..." : str;
    }

    private String truncateKeepingTail(String str, int maxLen) {
        if (str == null) return null;
        if (str.length() <= maxLen) {
            return str;
        }
        int headLen = Math.min(300, maxLen / 4);
        int tailLen = Math.max(0, maxLen - headLen - 32);
        return str.substring(0, headLen)
                + "\n... (stderr truncated, showing tail) ...\n"
                + str.substring(str.length() - tailLen);
    }

    private String buildFfmpegFailureReason(FfmpegProcessRunner.ProcessResult result) {
        String stderr = result.stderrOutput();
        if (StringUtils.isBlank(stderr)) {
            return "FFmpeg exit code " + result.exitCode() + ", stderr is empty";
        }
        return "FFmpeg exit code " + result.exitCode() + ":\n"
                + truncateKeepingTail(stderr.trim(), FAIL_REASON_MAX_LEN - 32);
    }
}
