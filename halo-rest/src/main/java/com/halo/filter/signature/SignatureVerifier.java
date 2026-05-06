package com.halo.filter.signature;

import java.util.Map;

/**
 * 签名验证器接口
 * 定义签名验证的通用方法
 *
 * @author Generated
 * @date 2026/01/16
 */
public interface SignatureVerifier {

    /**
     * 验证签名
     *
     * @param signData  待签名的数据（已排序的参数 Map）
     * @param secretKey 密钥
     * @param signature 待验证的签名
     * @return true 表示签名匹配，false 表示不匹配
     */
    boolean verify(Map<String, String> signData, String secretKey, String signature);

    /**
     * 生成签名
     *
     * @param signData  待签名的数据（已排序的参数 Map）
     * @param secretKey 密钥
     * @return 生成的签名字符串
     */
    String generate(Map<String, String> signData, String secretKey);

    /**
     * 获取算法类型
     *
     * @return 签名算法枚举
     */
    SignatureAlgorithm getAlgorithm();
}
