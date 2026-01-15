package com.halo.common;

import java.time.format.DateTimeFormatter;

/**
 * @Description:
 * @Author: halo_ry
 * @Date: 2025/1/10 15:15
 */
public class DateFormatterConstants {

    /**
     * 日期格式化 yyyyMMddHHmmssSSS
     */
    public static final DateTimeFormatter TIMESTAMP_MILLISECOND_FORMATTER =  DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS");

    /**
     * 日期格式化 yyyyMMddHHmmss
     */
    public static final DateTimeFormatter TIMESTAMP_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

    /**
     * 日期格式化 yyyy-MM-dd HH:mm:ss
     */
    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * 日期格式化 yyyy-MM-dd
     */
    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    /**
     * 日期格式化 yyyyMMdd
     */
    public static final DateTimeFormatter SHORT_DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");

}
