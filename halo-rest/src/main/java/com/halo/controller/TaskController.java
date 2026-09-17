package com.halo.controller;

import com.halo.common.Result;
import com.halo.dto.SubmitTaskRequest;
import com.halo.dto.SubmitTaskResponse;
import com.halo.dto.resp.TaskStatusResponse;
import com.halo.enums.ErrorCodeEnum;
import com.halo.service.TaskQueueManager;
import com.halo.service.TaskService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @author:
 * @date: 2026/04/17 18:43
 * @description: 任务接口
 */
@Slf4j
@Validated
@RestController
@RequestMapping("/tasks")
public class TaskController {

    @Resource
    private TaskService taskService;

    @Resource
    private TaskQueueManager taskQueueManager;

    /**
     * 提交任务
     */
    @PostMapping("/submit")
    public ResponseEntity<Result<SubmitTaskResponse>> submitTask(@RequestBody @Valid SubmitTaskRequest request) {
        // 队列已满时返回 429
        if (taskQueueManager.isFull()) {
            Result<SubmitTaskResponse> result = new Result<>();
            result.setRlt(false);
            result.setCode(Integer.parseInt(ErrorCodeEnum.TASK_QUEUE_FULL.getCode()));
            result.setMsg(ErrorCodeEnum.TASK_QUEUE_FULL.getMessage());
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body(result);
        }

        SubmitTaskResponse response = taskService.submit(request);
        return ResponseEntity.ok(Result.getSuccess(response));
    }

    /**
     * 查询任务状态
     */
    @GetMapping("/query/{taskId}")
    public Result<TaskStatusResponse> getTaskStatus(@PathVariable Long taskId) {
        return Result.getSuccess(taskService.getTaskStatus(taskId));
    }

    /**
     * 取消任务
     */
    @PostMapping("/cancel/{taskId}")
    public Result<Map<String, Object>> cancelTask(@PathVariable Long taskId) {
        return Result.getSuccess(taskService.cancelTask(taskId));
    }
}
