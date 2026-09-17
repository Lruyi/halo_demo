package com.halo.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * @author:
 * @date: 2026/04/17 18:43
 * @description:
 */

@Data
@TableName(value = "tb_ffmpeg_task", autoResultMap = true)
public class FfmpegTask {

    @TableId(type = IdType.AUTO)
    private Long id;

    /** 任务类型 */
    private String taskType;

    /** 优先级 */
    private Integer priority;

    /** 完整任务参数（JSON） */
    @TableField(typeHandler = JacksonTypeHandler.class)
    private Map<String, Object> params;

    /** 业务服务回调地址 */
    private String callbackUrl;

    /** 任务状态 */
    private Integer status;

    /** 输出文件 COS 地址 */
    private String resultUrl;

    /** 失败原因 */
    private String failReason;

    /** 已重试次数 */
    private Integer retryCount;

    /** 最大重试次数 */
    private Integer maxRetry;

    /** 超时时间（秒） */
    private Integer timeoutSeconds;

    /** 执行中的 FFmpeg 进程 PID */
    private Integer pid;

    /** 存库展示的 FFmpeg 命令（不含 binPath） */
    private String ffmpegCmd;

    /** 开始执行时间 */
    private LocalDateTime startTime;

    /** 执行结束时间 */
    private LocalDateTime endTime;

    /** 回调状态 */
    private Integer callbackStatus;

    /** 回调重试次数 */
    private Integer callbackRetry;

    /** 最后回调时间 */
    private LocalDateTime lastCallbackTime;

    /** 调用方应用标识 */
    private String callerApp;

    /** 业务侧任务ID（透传） */
    private String bizTaskId;

    /** 输出文件时长（ms），仅内存用于回调，表结构无此列 */
    @TableField(exist = false)
    private Long durationMs;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
