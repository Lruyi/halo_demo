package com.halo.enums;

import lombok.Getter;

/**
 * 解锁规则
 */
public enum ArrangeRuleTypeEnum {

    /**
     * 每x日解锁1节课次
     */
    DAYS(1, "每x日解锁1节课次"),

    /**
     * 每周x,y,z解锁1节课次
     */
    DAY_OF_WEEK(2, "每周x,y,z解锁1节课次"),

    /**
     * 一次性解锁全部课次
     */
    UNLOCK_ALL(3, "一次性解锁全部课次"),
    ;

    @Getter
    private int code;

    @Getter
    private String desc;

    ArrangeRuleTypeEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static ArrangeRuleTypeEnum valueOf(int code) {
        for (ArrangeRuleTypeEnum e : ArrangeRuleTypeEnum.values()) {
            if (e.getCode() == code) {
                return e;
            }
        }
        throw new IllegalArgumentException("解锁规则错误,code=" + code);
    }
}
