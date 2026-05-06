package com.halo.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;
import java.util.TreeMap;

/**
 * HMAC-SHA256签名工具类
 * 提供签名生成和验证功能
 *
 * @author Generated
 * @date 2026/01/16
 */
@Slf4j
public class HmacSha256Util {

    private static final String ALGORITHM = "HmacSHA256";

    /**
     * 使用HMAC-SHA256算法生成签名
     *
     * @param data 待签名的数据
     * @param key  密钥
     * @return Base64编码的签名字符串
     */
    public static String generateSign(String data, String key) {
        if (StringUtils.isBlank(data) || StringUtils.isBlank(key)) {
            log.error("生成签名失败，数据或密钥为空");
            throw new IllegalArgumentException("数据或密钥不能为空");
        }

        try {
            Mac mac = Mac.getInstance(ALGORITHM);
            SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), ALGORITHM);
            mac.init(secretKeySpec);
            byte[] hash = mac.doFinal(data.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hash);
        } catch (Exception e) {
            log.error("生成HMAC-SHA256签名异常，data: {}, key: {}", data, key, e);
            throw new RuntimeException("生成签名失败", e);
        }
    }

    /**
     * 验证签名是否匹配
     *
     * @param data 待验证的数据
     * @param key  密钥
     * @param sign 待验证的签名
     * @return true表示签名匹配，false表示不匹配
     */
    public static boolean verifySign(String data, String key, String sign) {
        if (StringUtils.isBlank(data) || StringUtils.isBlank(key) || StringUtils.isBlank(sign)) {
            log.warn("验证签名失败，数据、密钥或签名为空");
            return false;
        }

        try {
            String expectedSign = generateSign(data, key);
            return expectedSign.equals(sign);
        } catch (Exception e) {
            log.error("验证HMAC-SHA256签名异常，data: {}, key: {}, sign: {}", data, key, sign, e);
            return false;
        }
    }

    /**
     * 构建待签名的字符串（从请求对象构建，自动按字典序排序）
     * 使用 TreeMap 确保参数顺序固定，避免因属性顺序不同导致签名不一致
     *
     * @param empNo     员工编号
     * @param timestamp 时间戳（请求头中的时间戳）
     * @param type      操作类型（请求体中的type，可为null）
     * @return 待签名的字符串（按字典序排序：empNo, timestamp, type）
     */
    public static String buildSignData(String empNo, String timestamp, Integer type) {
        // 统一使用 buildSignDataFromRequest 方法，确保使用 TreeMap 自动排序
        if (StringUtils.isBlank(empNo) || StringUtils.isBlank(timestamp)) {
            log.error("构建签名字符串失败，员工编号或时间戳为空");
            throw new IllegalArgumentException("员工编号或时间戳不能为空");
        }

        // 使用 TreeMap 自动按 key 的字典序排序，确保签名一致性
        // 无论参数传入顺序如何，最终签名字符串的顺序都是固定的
        TreeMap<String, String> params = new TreeMap<>();
        params.put("empNo", empNo);
        params.put("timestamp", timestamp);
        if (type != null) {
            params.put("type", String.valueOf(type));
        }

        // 构建签名字符串：key1=value1&key2=value2（已按字典序排序）
        StringBuilder signData = new StringBuilder();
        boolean first = true;
        for (java.util.Map.Entry<String, String> entry : params.entrySet()) {
            if (!first) {
                signData.append("&");
            }
            signData.append(entry.getKey()).append("=").append(entry.getValue());
            first = false;
        }

        return signData.toString();
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


    /**
     * 从 Map 构建待签名的字符串（自动按字典序排序）
     * 使用 TreeMap 确保参数顺序固定，避免因参数顺序不同导致签名不一致
     *
     * @param params Map 参数（key-value 对）
     * @return 待签名的字符串（按字典序排序：key1=value1&key2=value2）
     */
    public static String buildSignDataFromMap(Map<String, String> params) {
        if (MapUtils.isEmpty(params)) {
            log.error("构建签名字符串失败，参数Map为空");
            throw new IllegalArgumentException("参数Map不能为空");
        }

        // 使用 TreeMap 自动按 key 的字典序排序，确保签名一致性
        TreeMap<String, String> sortedParams = new TreeMap<>(params);

        // 构建签名字符串：key1=value1&key2=value2（已按字典序排序）
        StringBuilder signData = new StringBuilder();
        boolean first = true;
        for (java.util.Map.Entry<String, String> entry : sortedParams.entrySet()) {
            if (!first) {
                signData.append("&");
            }
            signData.append(entry.getKey()).append("=").append(entry.getValue());
            first = false;
        }

        return signData.toString();
    }

    /**
     * 验证时间戳是否在有效期内
     * 使用绝对值计算时间差，允许未来时间戳（在有效期内），以容忍服务器时钟偏差
     *
     * @param timestamp       时间戳字符串（毫秒）
     * @param validityPeriodMs 有效期（毫秒）
     * @return true表示有效，false表示无效
     */
    public static boolean isTimestampValid(String timestamp, long validityPeriodMs) {
        if (StringUtils.isBlank(timestamp)) {
            log.warn("时间戳为空");
            return false;
        }

        try {
            long timestampValue = Long.parseLong(timestamp);
            long currentTime = System.currentTimeMillis();
            long timeDiff = Math.abs(currentTime - timestampValue);

            if (timeDiff > validityPeriodMs) {
                log.warn("时间戳过期 - timestamp: {}, currentTime: {}, timeDiff: {}ms, validityPeriod: {}ms",
                        timestampValue, currentTime, timeDiff, validityPeriodMs);
                return false;
            }

            return true;
        } catch (NumberFormatException e) {
            log.warn("时间戳格式错误，timestamp: {}", timestamp);
            return false;
        }
    }
}
