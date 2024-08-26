package com.halo.dto;

import java.util.Random;

/**
 * @Description: 灰度切流算法实现，按随机数占比精准切流
 * @author: halo_ry
 * @Date: 2024/8/22 17:17
 */
public class TrafficRouterDTO {
    private static final Random RANDOM = new Random();

    // 判断当前请求是否应该执行新功能
    public static boolean isGrayEnabled(double grayRatio) {
        // 生成一个0到1之间的随机数
        double randomValue = RANDOM.nextDouble();
        // 如果随机数小于灰度比例，返回true，表示切流到新功能
        return randomValue < grayRatio;
    }

    public static void main(String[] args) {
        // 假设当前灰度比例为10%（0.1）
        double grayRatio = 0.1;
        int grayCount = 0;
        int totalRequests = 10000;

        // 模拟大量请求判断
        for (int i = 0; i < totalRequests; i++) {
            if (isGrayEnabled(grayRatio)) {
                grayCount++;
            }
        }

        System.out.println("切流到新功能的请求数: " + grayCount);
        System.out.println("实际切流比例: " + (double) grayCount / totalRequests);
    }
}





