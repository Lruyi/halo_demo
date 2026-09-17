CREATE TABLE `tb_ffmpeg_task`
(
    `id`                 bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键（即返回给业务服务的 taskId）',
    `task_type`          varchar(64)  NOT NULL COMMENT '任务类型：VIDEO_OVERLAY / VIDEO_CONCAT / VIDEO_TRIM / VIDEO_SPEED_ADJUST / MERGE_AUDIO_VIDEO / BURN_SUBTITLES / EXTRACT_FRAME',
    `priority`           tinyint(4) NOT NULL DEFAULT '0' COMMENT '优先级：0-普通, 1-高优先级',
    `params`             json         NOT NULL COMMENT '完整任务参数（含输入文件URL、输出路径、callbackUrl等）',
    `callback_url`       varchar(512) NOT NULL COMMENT '业务服务回调地址',
    `status`             tinyint(4) NOT NULL DEFAULT '0' COMMENT '任务状态：0-待执行,1-执行中,2-成功,3-失败,4-超时,5-已取消',
    `result_url`         varchar(512)          DEFAULT NULL COMMENT '输出文件 COS 地址（执行成功时）',
    `fail_reason`        text COMMENT '失败原因（FFmpeg stderr 摘要）',
    `retry_count`        int(11) NOT NULL DEFAULT '0' COMMENT '已重试次数',
    `max_retry`          int(11) NOT NULL DEFAULT '2' COMMENT '最大重试次数',
    `timeout_seconds`    int(11) NOT NULL DEFAULT '300' COMMENT '超时时间（秒）',
    `pid`                int(11) DEFAULT NULL COMMENT '执行中的 FFmpeg 进程PID',
    `ffmpeg_cmd`         text COMMENT '实际执行的 FFmpeg 命令（调试用）',
    `start_time`         datetime              DEFAULT NULL COMMENT '开始执行时间',
    `end_time`           datetime              DEFAULT NULL COMMENT '执行结束时间',
    `callback_status`    tinyint(4) NOT NULL DEFAULT '0' COMMENT '回调状态：0-未回调,1-回调失败,2-回调成功',
    `callback_retry`     int(11) NOT NULL DEFAULT '0' COMMENT '回调重试次数',
    `last_callback_time` datetime              DEFAULT NULL COMMENT '最后回调时间',
    `caller_app`         varchar(64)           DEFAULT NULL COMMENT '调用方应用标识（便于多业务服务接入）',
    `biz_task_id`        varchar(128)          DEFAULT NULL COMMENT '业务侧任务ID（透传，原样回调给业务服务）',
    `create_time`        datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `update_time`        datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    KEY                  `idx_status` (`status`),
    KEY                  `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='FFmpeg 任务主表';


CREATE TABLE `tb_ffmpeg_task_log`
(
    `id`          bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
    `task_id`     bigint(20) NOT NULL COMMENT '关联 tb_ffmpeg_task.id',
    `log_type`    varchar(32) NOT NULL COMMENT '日志类型：SUBMIT/START/SUCCESS/FAILED/TIMEOUT/CALLBACK',
    `content`     text COMMENT '日志内容',
    `create_time` datetime    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    KEY           `idx_task_id` (`task_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8mb4 COMMENT='任务执行日志';