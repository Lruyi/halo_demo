package com.halo.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

/**
 * @author:
 * @date: 2026/04/17 18:43
 * @description:
 */

@Data
public class SubmitTaskRequest {

    /** 任务类型 */
    @NotBlank(message = "taskType 不能为空")
    private String taskType;

    /** 业务侧任务ID（原样透传到回调） */
    private String bizTaskId;

    /** 调用方应用标识 */
    @NotBlank(message = "callerApp 不能为空")
    private String callerApp;

    /** 回调地址 */
    @NotBlank(message = "callbackUrl 不能为空")
    private String callbackUrl;

    /** 优先级：NORMAL / HIGH */
    private String priority;

    /** 超时时间（秒），覆盖默认值 */
    private Integer timeoutSeconds;

    /** 最大重试次数，覆盖默认值 */
    private Integer maxRetry;

    /** 输入媒体文件列表 */
    @NotEmpty(message = "inputs 不能为空")
    @Valid
    private List<TaskInput> inputs;

    /** 输出描述 */
    @NotNull(message = "output 不能为空")
    @Valid
    private TaskOutput output;

    /** 任务类型专属参数 */
    private TaskOptions options;
}
