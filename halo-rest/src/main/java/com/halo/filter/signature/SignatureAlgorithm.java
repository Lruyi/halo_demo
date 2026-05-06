package com.halo.filter.signature;

/**
 * 签名算法枚举
 * 定义支持的签名算法类型
 *
 * @author Generated
 * @date 2026/01/16
 */
public enum SignatureAlgorithm {
    /**
     * HMAC-SHA256 算法
     */
    HMAC_SHA256("HMAC-SHA256"),

    /**
     * MD5 算法
     */
    MD5("MD5");

    private final String name;

    SignatureAlgorithm(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    /**
     * 根据名称获取算法枚举
     *
     * @param name 算法名称（不区分大小写）
     * @return 签名算法枚举，如果未找到则返回 null
     */
    public static SignatureAlgorithm fromName(String name) {
        if (name == null) {
            return null;
        }
        for (SignatureAlgorithm algorithm : values()) {
            if (algorithm.name.equalsIgnoreCase(name) || algorithm.name().equalsIgnoreCase(name)) {
                return algorithm;
            }
        }
        return null;
    }
}
