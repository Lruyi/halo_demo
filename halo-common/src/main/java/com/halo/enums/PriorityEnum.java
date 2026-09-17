package com.halo.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.Getter;

/**
 * @author:
 * @date: 2026/04/17 18:43
 * @description:
 */

@Getter
public enum PriorityEnum {

    NORMAL(0, "普通"),
    HIGH(1, "高优先级"),
    ;

    @EnumValue
    private final int code;
    private final String desc;

    PriorityEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
