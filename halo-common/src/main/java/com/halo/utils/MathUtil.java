package com.halo.utils;

import java.math.BigDecimal;

/**
 * @Description:
 * @Author: Halo_ry
 * @Date: 2022/11/8 15:54
 */
public class MathUtil {

    public static final int DEFAULT_SCALE = 2;

    /**
     * 加法
     *
     * @param v1
     * @param v2
     * @return
     */
    public static double add(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.add(b2).doubleValue();
    }

    /**
     * 减法
     *
     * @param v1
     * @param v2
     * @return
     */
    public static double sub(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.subtract(b2).doubleValue();
    }

    /**
     * 乘法
     *
     * @param v1
     * @param v2
     * @return
     */
    public static BigDecimal mul(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.multiply(b2);
    }

    /**
     * 除法，默认精度
     *
     * @param v1
     * @param v2
     * @return
     */
    public static BigDecimal div(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.divide(b2, DEFAULT_SCALE, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * 除法，默认精度
     *
     * @param v1
     * @param v2
     * @return
     */
    public static double div(double v1, double v2, int scale) {
        if (scale < 0) {
            new IllegalArgumentException("The scale must be a positive integer or zero");
        }
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     * 四舍五入
     *
     * @param v1
     * @param scale
     * @return
     */
    public static double round(double v1, int scale) {
        if (scale < 0) {
            new IllegalArgumentException("The scale must be a positive integer or zero");
        }
        return new BigDecimal(Double.toString(v1)).setScale(0, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     * 计算"2的n次方的值"的幂
     *
     * @param value 2的n次方的值，大于0。
     * @return 幂
     */
    public static int log(int value) {
        if (value <= 0) throw new RuntimeException("参数非法！");

        int n = 0;
        while (value % 2 == 0) {
            value = value / 2;
            n++;
        }
        return n;
    }
}
