package com.halo.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author:
 * @date: 2026/04/17 18:43
 * @description:
 */

@Data
@TableName("tb_ffmpeg_task_log")
public class FfmpegTaskLog {

    @TableId(type = IdType.AUTO)
    private Long id;

    /** 关联任务ID */
    private Long taskId;

    /** 日志类型：SUBMIT/START/SUCCESS/FAILED/TIMEOUT/CALLBACK */
    private String logType;

    /** 日志内容 */
    private String content;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}
