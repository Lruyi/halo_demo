package com.halo.filter.signature.impl;

import com.halo.filter.signature.SignatureAlgorithm;
import com.halo.filter.signature.SignatureVerifier;
import com.halo.utils.HmacSha256Util;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

/**
 * HMAC-SHA256 签名验证器实现
 *
 * @author Generated
 * @date 2026/01/16
 */
@Slf4j
public class HmacSha256Verifier implements SignatureVerifier {

    @Override
    public boolean verify(Map<String, String> signData, String secretKey, String signature) {
        if (signData == null || signData.isEmpty() || StringUtils.isBlank(secretKey) || StringUtils.isBlank(signature)) {
            log.warn("HMAC-SHA256签名验证失败，参数为空");
            return false;
        }

        try {
            // 构建签名字符串（使用 TreeMap 自动排序）
            String signDataStr = HmacSha256Util.buildSignDataFromMap(signData);
            return HmacSha256Util.verifySign(signDataStr, secretKey, signature);
        } catch (Exception e) {
            log.error("HMAC-SHA256签名验证异常", e);
            return false;
        }
    }

    @Override
    public String generate(Map<String, String> signData, String secretKey) {
        if (signData == null || signData.isEmpty() || StringUtils.isBlank(secretKey)) {
            throw new IllegalArgumentException("签名数据或密钥不能为空");
        }

        String signDataStr = HmacSha256Util.buildSignDataFromMap(signData);
        return HmacSha256Util.generateSign(signDataStr, secretKey);
    }

    @Override
    public SignatureAlgorithm getAlgorithm() {
        return SignatureAlgorithm.HMAC_SHA256;
    }
}
