package com.halo.enums;

import lombok.Getter;

/**
 * @Description:
 * @author: halo_ry
 * @Date: 2021/8/5 17:01
 */
public enum BusinessBelongEnum {

    /**
     * 代表全部，不区分业务归属
     */
    ALL(-1, "全部"),

    /**
     * 培优
     */
    PEIYOU(1, "培优"),

    /**
     * 小猴学堂
     */
    LITTLE_MONKEY_SCHOOL(3, "小猴学堂"),
    ;
    @Getter
    private int code;
    @Getter
    private String desc;

    BusinessBelongEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
