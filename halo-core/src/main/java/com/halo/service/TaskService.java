package com.halo.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.halo.config.FfmpegDynamicProperties;
import com.halo.dto.CallbackRequestDTO;
import com.halo.dto.TaskInput;
import com.halo.dto.TaskOptions;
import com.halo.dto.SubmitTaskRequest;
import com.halo.dto.SubmitTaskResponse;
import com.halo.dto.resp.TaskStatusResponse;
import com.halo.entity.FfmpegTask;
import com.halo.entity.FfmpegTaskLog;
import com.halo.enums.*;
import com.halo.exception.BusinessException;
import com.halo.mapper.FfmpegTaskLogMapper;
import com.halo.mapper.FfmpegTaskMapper;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author:
 * @date: 2026/04/17 18:43
 * @description: 任务管理服务。提供任务提交、查询、取消、手动重试回调等接口。
 */
@Slf4j
@Service
public class TaskService {

    @Resource
    private FfmpegTaskMapper taskMapper;
    @Resource
    private FfmpegTaskLogMapper taskLogMapper;
    @Resource
    private TaskQueueManager taskQueueManager;
    @Resource
    private DiskSpaceMonitor diskSpaceMonitor;
    @Resource
    private CallbackService callbackService;
    @Resource
    private FfmpegDynamicProperties ffmpegDynamicProperties;
    @Resource
    private ObjectMapper objectMapper;

    /**
     * 提交任务
     */
    @SuppressWarnings("unchecked")
    public SubmitTaskResponse submit(SubmitTaskRequest request) {
        // 1. 校验磁盘空间
        if (!diskSpaceMonitor.hasEnoughSpace()) {
            throw new BusinessException(ErrorCodeEnum.DISK_SPACE_LOW);
        }

        // 2. 校验任务类型
        TaskTypeEnum taskTypeEnum = TaskTypeEnum.getByCode(request.getTaskType());
        if (taskTypeEnum == null) {
            throw new BusinessException(ErrorCodeEnum.TASK_TYPE_NOT_SUPPORTED);
        }
        validateRequestByTaskType(request, taskTypeEnum);

        // 3. 校验队列容量
        if (taskQueueManager.isFull()) {
            throw new BusinessException(ErrorCodeEnum.TASK_QUEUE_FULL);
        }

        // 4. 构建任务实体
        FfmpegTask task = new FfmpegTask();
        task.setTaskType(taskTypeEnum.getCode());
        task.setPriority(parsePriority(request.getPriority()));
        task.setCallbackUrl(request.getCallbackUrl());
        task.setCallerApp(request.getCallerApp());
        task.setBizTaskId(request.getBizTaskId());
        task.setStatus(TaskStatusEnum.PENDING.getCode());
        task.setRetryCount(0);
        task.setMaxRetry(request.getMaxRetry() != null ? request.getMaxRetry() : ffmpegDynamicProperties.getDefaultMaxRetry());
        task.setTimeoutSeconds(request.getTimeoutSeconds() != null ? request.getTimeoutSeconds() : ffmpegDynamicProperties.getDefaultTimeoutSeconds());
        task.setCallbackStatus(CallbackStatusEnum.NOT_CALLED.getCode());
        task.setCallbackRetry(0);

        // 完整请求参数存入 params JSON
        Map<String, Object> params = objectMapper.convertValue(request, Map.class);
        task.setParams(params);

        // 5. 写入 DB
        taskMapper.insert(task);
        log.info("[提交] 任务已创建, taskId={}, taskTypeEnum={}, bizTaskId={}", task.getId(), taskTypeEnum, request.getBizTaskId());

        // 6. 记录日志
        saveLog(task.getId(), "SUBMIT", "任务提交, taskTypeEnum=" + taskTypeEnum + ", callerApp=" + request.getCallerApp());

        // 7. 入队
        boolean enqueued = taskQueueManager.enqueue(task);
        if (!enqueued) {
            // 入队失败（竞态条件下队列刚满），更新状态
            task.setStatus(TaskStatusEnum.FAILED.getCode());
            task.setFailReason("任务入队失败，队列已满");
            taskMapper.updateById(task);
            throw new BusinessException(ErrorCodeEnum.TASK_QUEUE_FULL);
        }

        // 8. 返回
        int estimatedWaitSeconds = taskQueueManager.size() * 30; // 粗略估计
        return SubmitTaskResponse.builder()
                .taskId(task.getId())
                .status(TaskStatusEnum.PENDING.name())
                .estimatedWaitSeconds(estimatedWaitSeconds)
                .build();
    }

    /**
     * 查询任务状态
     */
    public TaskStatusResponse getTaskStatus(Long taskId) {
        FfmpegTask task = taskMapper.selectById(taskId);
        if (task == null) {
            throw new BusinessException(ErrorCodeEnum.TASK_NOT_FOUND);
        }
        return TaskStatusResponse.builder()
                .taskId(task.getId())
                .bizTaskId(task.getBizTaskId())
                .taskType(task.getTaskType())
                .status(getStatusName(task.getStatus()))
                .resultUrl(task.getResultUrl())
                .failReason(task.getFailReason())
                .retryCount(task.getRetryCount())
                .startTime(task.getStartTime())
                .endTime(task.getEndTime())
                .createTime(task.getCreateTime())
                .build();
    }

    /**
     * 取消任务
     */
    public Map<String, Object> cancelTask(Long taskId) {
        FfmpegTask task = taskMapper.selectById(taskId);
        if (task == null) {
            throw new BusinessException(ErrorCodeEnum.TASK_NOT_FOUND);
        }

        Integer previousStatus = task.getStatus();
        TaskStatusEnum previousStatusEnum = TaskStatusEnum.getByCode(previousStatus);

        // 已终态的任务无需取消
        if (previousStatusEnum != null && previousStatusEnum.isTerminal()) {
            throw new BusinessException(ErrorCodeEnum.TASK_CANNOT_CANCEL);
        }

        // PENDING 状态：从队列移除
        if (Objects.equals(previousStatus, TaskStatusEnum.PENDING.getCode())) {
            taskQueueManager.removeById(taskId);
        }

        // RUNNING 状态：需要通过 PID kill 进程（进程会自然退出并被 TaskExecutorService 捕获）
        // 这里仅更新状态，实际的进程 kill 依赖进程自然结束或超时 watchdog
        if (Objects.equals(previousStatus, TaskStatusEnum.RUNNING.getCode()) && task.getPid() != null) {
            try {
                ProcessHandle.of(task.getPid()).ifPresent(ProcessHandle::destroy);
                log.info("[取消] taskId={}, 已发送 kill 信号到 PID={}", taskId, task.getPid());
            } catch (Exception e) {
                log.warn("[取消] taskId={}, kill 进程失败: {}", taskId, e.getMessage());
            }
        }

        task.setStatus(TaskStatusEnum.CANCELLED.getCode());
        taskMapper.updateById(task);
        saveLog(taskId, "CANCEL", "任务取消, 之前状态=" + getStatusName(previousStatus));
        log.info("[取消] taskId={}, {} → CANCELLED", taskId, getStatusName(previousStatus));

        return Map.of(
                "taskId", taskId,
                "previousStatus", getStatusName(previousStatus),
                "currentStatus", TaskStatusEnum.CANCELLED.name()
        );
    }

    /**
     * 手动重触回调
     */
    public Map<String, Object> retryCallback(Long taskId) {
        FfmpegTask task = taskMapper.selectById(taskId);
        if (task == null) {
            throw new BusinessException(ErrorCodeEnum.TASK_NOT_FOUND);
        }
        TaskStatusEnum statusEnum = TaskStatusEnum.getByCode(task.getStatus());
        if (statusEnum == null || !statusEnum.isTerminal() || statusEnum == TaskStatusEnum.CANCELLED) {
            throw new BusinessException("任务状态不支持重触回调: " + getStatusName(task.getStatus()));
        }

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
        return Map.of("taskId", taskId, "callbackTriggered", true);
    }

    private Integer parsePriority(String priority) {
        if ("HIGH".equalsIgnoreCase(priority)) {
            return PriorityEnum.HIGH.getCode();
        }
        return PriorityEnum.NORMAL.getCode();
    }

    private void validateRequestByTaskType(SubmitTaskRequest request, TaskTypeEnum taskTypeEnum) {
        switch (taskTypeEnum) {
            case VIDEO_CONCAT_IMAGE_OVERLAY -> validateVideoConcatImageOverlayRequest(request);
            case VIDEO_SEGMENT_REPLACE -> validateVideoSegmentReplaceRequest(request);
            default -> {}
        }
    }

    private void validateVideoConcatImageOverlayRequest(SubmitTaskRequest request) {
        List<TaskInput> inputs = request.getInputs();
        if (inputs == null || inputs.isEmpty()) {
            throw new IllegalArgumentException("VIDEO_CONCAT_IMAGE_OVERLAY 缺少 inputs");
        }

        long videoCount = inputs.stream()
                .filter(input -> input.getRole() == InputRoleEnum.VIDEO)
                .count();
        if (videoCount < 1) {
            throw new IllegalArgumentException("VIDEO_CONCAT_IMAGE_OVERLAY 至少需要一条 role=VIDEO 的输入");
        }

        List<TaskInput> imageInputs = inputs.stream()
                .filter(input -> input.getRole() == InputRoleEnum.IMAGE)
                .toList();
        if (imageInputs.size() != 1) {
            throw new IllegalArgumentException("VIDEO_CONCAT_IMAGE_OVERLAY 必须且只能有一条 role=IMAGE 的输入");
        }

        TaskInput imageInput = imageInputs.getFirst();
        if (imageInput.getPosX() == null || imageInput.getPosY() == null) {
            throw new IllegalArgumentException("IMAGE 输入必须提供 posX 和 posY");
        }

        TaskOptions options = request.getOptions();
        if (options != null && options.getImageMaxSize() != null && options.getImageMaxSize() <= 0) {
            throw new IllegalArgumentException("options.imageMaxSize 必须大于 0");
        }
    }

    public void validateVideoSegmentReplaceRequest(SubmitTaskRequest request) {
        List<TaskInput> inputs = request.getInputs();
        if (CollectionUtils.isEmpty(inputs)) {
            throw new IllegalArgumentException("VIDEO_SEGMENT_REPLACE 缺少 inputs");
        }

        long baseVideoCount = inputs.stream()
                .filter(input -> input.getRole() == InputRoleEnum.BASE_VIDEO)
                .count();
        if (baseVideoCount != 1) {
            throw new IllegalArgumentException("VIDEO_SEGMENT_REPLACE 必须且只能有一条 role=BASE_VIDEO 的输入");
        }

        List<TaskInput> overlays = inputs.stream()
                .filter(input -> input.getRole() == InputRoleEnum.OVERLAY_VIDEO)
                .sorted(Comparator.comparingInt(i -> Objects.requireNonNullElse(i.getStartMs(), 0)))
                .toList();
        if (overlays.isEmpty()) {
            throw new IllegalArgumentException("VIDEO_SEGMENT_REPLACE 至少需要一条 role=OVERLAY_VIDEO 的输入");
        }

        for (TaskInput overlay : overlays) {
            if (overlay.getStartMs() == null || overlay.getEndMs() == null) {
                throw new IllegalArgumentException("OVERLAY_VIDEO 必须提供 startMs 和 endMs");
            }
            if (overlay.getStartMs() < 0) {
                throw new IllegalArgumentException("OVERLAY_VIDEO startMs 不能为负数");
            }
            if (overlay.getStartMs() >= overlay.getEndMs()) {
                throw new IllegalArgumentException("OVERLAY_VIDEO startMs 必须小于 endMs");
            }
        }

        long subtitleCount = inputs.stream()
                .filter(input -> input.getRole() == InputRoleEnum.SUBTITLE)
                .count();
        if (subtitleCount > 1) {
            throw new IllegalArgumentException("VIDEO_SEGMENT_REPLACE SUBTITLE 输入最多 1 条");
        }

        long fontCount = inputs.stream()
                .filter(input -> input.getRole() == InputRoleEnum.FONT)
                .count();
        if (fontCount > 1) {
            throw new IllegalArgumentException("VIDEO_SEGMENT_REPLACE FONT 输入最多 1 条");
        }

        if (fontCount > 0 && subtitleCount == 0) {
            throw new IllegalArgumentException("VIDEO_SEGMENT_REPLACE 提供 FONT 输入时必须同时提供 SUBTITLE 输入");
        }
    }

    private String getStatusName(Integer statusCode) {
        TaskStatusEnum statusEnum = TaskStatusEnum.getByCode(statusCode);
        return statusEnum != null ? statusEnum.name() : String.valueOf(statusCode);
    }

    private void saveLog(Long taskId, String logType, String content) {
        FfmpegTaskLog logEntity = new FfmpegTaskLog();
        logEntity.setTaskId(taskId);
        logEntity.setLogType(logType);
        logEntity.setContent(content);
        taskLogMapper.insert(logEntity);
    }
}
