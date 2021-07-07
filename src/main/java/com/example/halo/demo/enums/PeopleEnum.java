package com.example.halo.demo.enums;

import com.example.halo.demo.exception.BizException;
import lombok.Getter;

/**
 * @Description:
 * @author: halo_ry
 * @Date: 2021/7/1 17:45
 */
public enum PeopleEnum {

    /**
     * 白种人
     */
    WHITE(1, "白种人"),

    /**
     * 黄种人
     */
    BLACK(2, "黄种人"),

    /**
     * 黑种人
     */
    ASIAN(3, "黑种人"),
    ;

    @Getter
    private int code;
    @Getter
    private String desc;

    PeopleEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static PeopleEnum parse(int code) {
        for (PeopleEnum peopleEnum : PeopleEnum.values()) {
            if (peopleEnum.getCode() == code) {
                return peopleEnum;
            }
        }
        throw new BizException("不支持的人种类型");
    }
}
