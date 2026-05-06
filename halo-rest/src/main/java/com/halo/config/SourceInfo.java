package com.halo.config;

import lombok.Data;

@Data
public class SourceInfo {

    private String name;

    /**
     * 来源方请求aigc-adapter的secret
     */
    private String clientSecret;

    /**
     * 回调来源方接口的secretKey
     */
    private String secretKey;

    /**
     * 签名算法（可选）
     * 如果配置了此字段，则使用此算法；否则使用 SignatureFilterConfig 中的配置
     * 支持的值：HMAC-SHA256、MD5 等
     */
    private String signatureAlgorithm;
}
