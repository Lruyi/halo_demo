package com.halo.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.Getter;

/**
 * @author:
 * @date: 2026/04/17 18:43
 * @description:
 */

@Getter
public enum TaskStatusEnum {

    PENDING(0, "待执行"),
    RUNNING(1, "执行中"),
    SUCCESS(2, "成功"),
    FAILED(3, "失败"),
    TIMEOUT(4, "超时"),
    CANCELLED(5, "已取消"),
    ;

    @EnumValue
    private final int code;
    private final String desc;

    TaskStatusEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static TaskStatusEnum getByCode(Integer code) {
        if (code == null) {
            return null;
        }
        for (TaskStatusEnum status : values()) {
            if (status.code == code) {
                return status;
            }
        }
        return null;
    }

    /** 是否为终态（不可变更） */
    public boolean isTerminal() {
        return this == SUCCESS || this == FAILED || this == TIMEOUT || this == CANCELLED;
    }
}
