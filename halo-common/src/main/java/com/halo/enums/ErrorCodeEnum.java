package com.halo.enums;

public enum ErrorCodeEnum {

    SUCCESS("000000", "成功"),
    ERROR("999999", "失败"),
    PARAM_ERROR("100001", "参数错误"),
    TASK_EXIST("100002", "任务已存在"),
    TASK_NOT_EXIST("100003", "任务不存在"),
    BARCODE_RULES_CONFLICT("100004", "规则冲突"),
    IM_SESSION_EXIST("100005", "会话已存在"),
    IM_SESSION_NOT_EXIST("100006", "会话不存在"),
    /** 网络文件校验，http、非允许的域名都会提示这个异常 */
    ERROR_NEWFILE_VALIDATE("100007", "网络文件校验失败"),
    TASK_NOT_FOUND("100002", "任务不存在"),
    TASK_QUEUE_FULL("100003", "任务队列已满，请稍后重试"),
    TASK_CANNOT_CANCEL("100004", "任务当前状态无法取消"),
    DISK_SPACE_LOW("100005", "磁盘空间不足，暂停接受新任务"),
    FFMPEG_NOT_AVAILABLE("100006", "FFmpeg 不可用"),
    TASK_TYPE_NOT_SUPPORTED("100007", "不支持的任务类型"),
    FORBIDDEN("400000", "无权访问"),
    PERMISSION_DENIED("400001", "无权访问，需要申请权限"),
    TOKEN_EXPIRED("400002", "凭证过期"),
    AI_RESP_JSON_PARSE_ERROR("500001", "AI响应JSON解析异常"),

    ;

    private final String code;
    private final String message;

    ErrorCodeEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
