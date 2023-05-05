package com.halo.enums;

public enum PriceTypeEnum {
    COURSE(1, "按总金额均摊"),
    CURRICULUM(2, "按课次设置"),
    CLASS_TIME(3, "按课时定价"),
    ;

    private final int code;
    private final String text;

    PriceTypeEnum(int code, String text) {
        this.code = code;
        this.text = text;
    }

    public static PriceTypeEnum valueOf(int code) {
        for (PriceTypeEnum item : PriceTypeEnum.values()) {
            if (item.code == code) {
                return item;
            }
        }
        return COURSE;
    }

    public int code() {
        return code;
    }

    public String text() {
        return text;
    }
}
