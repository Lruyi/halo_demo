package com.halo.config;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * 签名验证过滤器配置（通用）
 * 支持按 source（端）配置不同的签名算法
 * 支持多个端接入，每个端可配置不同的签名算法
 *
 * @author Generated
 * @date 2026/01/16
 */
@Data
@Configuration
@ConfigurationProperties("sign.filter")
public class SignatureFilterConfig {

    /**
     * 默认签名算法（全局默认值）
     */
    private String defaultAlgorithm = "HMAC-SHA256";

    /**
     * 按 source（端）配置算法
     * 格式：source -> algorithm
     * 示例：
     *   sourceAlgorithmMap:
     *     its: HMAC-SHA256
     *     other: MD5
     */
    private Map<String, String> sourceAlgorithmMap = new HashMap<>();

    /**
     * 时间戳有效期（毫秒）
     * 默认10分钟
     */
    private Long timestampValidity = 10 * 60 * 1000L;

    /**
     * 根据 source（端）获取签名算法
     * 优先级：sourceAlgorithmMap > defaultAlgorithm
     *
     * @param source 业务来源（端）
     * @return 签名算法名称
     */
    public String getAlgorithmBySource(String source) {
        if (StringUtils.isNotBlank(source) && sourceAlgorithmMap.containsKey(source)) {
            return sourceAlgorithmMap.get(source);
        }
        return defaultAlgorithm;
    }
}
