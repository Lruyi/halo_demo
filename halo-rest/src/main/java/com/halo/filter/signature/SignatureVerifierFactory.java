package com.halo.filter.signature;

import com.halo.filter.signature.impl.HmacSha256Verifier;
import com.halo.filter.signature.impl.Md5Verifier;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 签名验证器工厂
 * 根据算法类型创建对应的签名验证器实例
 *
 * @author Generated
 * @date 2026/01/16
 */
@Slf4j
public class SignatureVerifierFactory {

    /**
     * 验证器缓存（单例模式）
     */
    private static final Map<SignatureAlgorithm, SignatureVerifier> VERIFIER_CACHE = new ConcurrentHashMap<>();

    static {
        // 初始化默认验证器
        VERIFIER_CACHE.put(SignatureAlgorithm.HMAC_SHA256, new HmacSha256Verifier());
        VERIFIER_CACHE.put(SignatureAlgorithm.MD5, new Md5Verifier());
    }

    /**
     * 根据算法类型获取签名验证器
     *
     * @param algorithm 签名算法枚举
     * @return 签名验证器实例，如果算法不支持则返回 null
     */
    public static SignatureVerifier getVerifier(SignatureAlgorithm algorithm) {
        if (algorithm == null) {
            return null;
        }
        return VERIFIER_CACHE.get(algorithm);
    }

    /**
     * 根据算法名称获取签名验证器
     *
     * @param algorithmName 算法名称（如 "HMAC-SHA256"、"MD5"）
     * @return 签名验证器实例，如果算法不支持则返回 null
     */
    public static SignatureVerifier getVerifier(String algorithmName) {
        if (StringUtils.isBlank(algorithmName)) {
            return null;
        }
        SignatureAlgorithm algorithm = SignatureAlgorithm.fromName(algorithmName);
        return getVerifier(algorithm);
    }

    /**
     * 注册自定义签名验证器
     * 允许动态扩展新的签名算法
     *
     * @param algorithm 签名算法枚举
     * @param verifier  签名验证器实例
     */
    public static void registerVerifier(SignatureAlgorithm algorithm, SignatureVerifier verifier) {
        if (algorithm != null && verifier != null) {
            VERIFIER_CACHE.put(algorithm, verifier);
            log.info("注册签名验证器: {}", algorithm.getName());
        }
    }
}
