package com.halo.utils;

import lombok.extern.slf4j.Slf4j;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

/**
 * HMAC-SHA256 签名工具类
 * 
 * @author halo_ry
 */
@Slf4j
public class HmacSha256Util {

    private static final String HMAC_SHA256 = "HmacSHA256";

    /**
     * 使用HMAC-SHA256算法生成签名
     * 
     * @param data 待签名的数据（通常是参数拼接后的字符串）
     * @param key 密钥
     * @return Base64编码的签名
     */
    public static String generateSign(String data, String key) {
        try {
            Mac mac = Mac.getInstance(HMAC_SHA256);
            SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), HMAC_SHA256);
            mac.init(secretKeySpec);
            byte[] hash = mac.doFinal(data.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            log.error("生成HMAC-SHA256签名失败", e);
            throw new RuntimeException("生成签名失败", e);
        }
    }

    /**
     * 验证签名是否匹配
     * 
     * @param data 待验证的数据
     * @param key 密钥
     * @param sign 待验证的签名
     * @return true表示签名匹配，false表示不匹配
     */
    public static boolean verifySign(String data, String key, String sign) {
        String expectedSign = generateSign(data, key);
        return expectedSign.equals(sign);
    }

    /**
     * 构建待签名的字符串
     * 按照参数名排序后拼接：key1=value1&key2=value2&timestamp=xxx
     * 
     * @param empNo 员工编号
     * @param timestamp 时间戳
     * @return 待签名的字符串
     */
    public static String buildSignData(String empNo, Long timestamp) {
        return String.format("empNo=%s&timestamp=%d", empNo, timestamp);
    }
}
