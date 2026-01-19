package com.halo.utils;

import java.util.regex.Pattern;

/**
 * @Description:
 * @Author: halo_ry
 * @Date: 2025/2/12 16:06
 */
public class IsIntegerCheck {

    /**
     * 判断字符串是否为整数
     *
     * @param str 字符串
     * @return 是否为整数
     */
    public static boolean isInteger(String str) {
        // 匹配整数的正则表达式(-1，1，1243)
        Pattern pattern = Pattern.compile("^-?\\d+$");
        return pattern.matcher(str).matches();
    }
}
