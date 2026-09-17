package com.halo.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.Getter;

/**
 * @author:
 * @date: 2026/04/17 18:43
 * @description:
 */

@Getter
public enum CallbackStatusEnum {

    NOT_CALLED(0, "未回调"),
    FAILED(1, "回调失败"),
    SUCCESS(2, "回调成功"),
    ;

    @EnumValue
    private final int code;
    private final String desc;

    CallbackStatusEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
