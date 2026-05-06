package com.halo.filter.signature.impl;

import com.halo.filter.signature.SignatureAlgorithm;
import com.halo.filter.signature.SignatureVerifier;
import com.halo.utils.MD5Util;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

/**
 * MD5 签名验证器实现
 * MD5 签名规则：MD5(secretKey + timestamp)
 * 注意：MD5 算法只使用 secretKey 和 timestamp，不使用请求体参数
 *
 * @author Generated
 * @date 2026/01/16
 */
@Slf4j
public class Md5Verifier implements SignatureVerifier {

    @Override
    public boolean verify(Map<String, String> signData, String secretKey, String signature) {
        if (StringUtils.isBlank(secretKey) || StringUtils.isBlank(signature)) {
            log.warn("MD5签名验证失败，密钥或签名为空");
            return false;
        }

        try {
            // MD5 签名规则：MD5(secretKey + timestamp)
            // 从 signData 中提取 timestamp
            String timestamp = signData.get("timestamp");
            if (StringUtils.isBlank(timestamp)) {
                log.warn("MD5签名验证失败，时间戳为空");
                return false;
            }

            String expectedSign = MD5Util.getMD5(secretKey + timestamp);
            boolean result = expectedSign.equals(signature);
            
            if (!result) {
                log.warn("MD5签名验证失败，期望签名: {}, 实际签名: {}", expectedSign, signature);
            }
            
            return result;
        } catch (Exception e) {
            log.error("MD5签名验证异常", e);
            return false;
        }
    }

    @Override
    public String generate(Map<String, String> signData, String secretKey) {
        if (signData == null || signData.isEmpty() || StringUtils.isBlank(secretKey)) {
            throw new IllegalArgumentException("签名数据或密钥不能为空");
        }

        String timestamp = signData.get("timestamp");
        if (StringUtils.isBlank(timestamp)) {
            throw new IllegalArgumentException("时间戳不能为空");
        }

        return MD5Util.getMD5(secretKey + timestamp);
    }

    @Override
    public SignatureAlgorithm getAlgorithm() {
        return SignatureAlgorithm.MD5;
    }
}
