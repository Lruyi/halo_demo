package com.halo.dto.resp;

import lombok.Data;

/**
 * @author:
 * @date: 2025/09/11 18:09
 * @description:
 */
@Data
public class CosTmpCredentialResp {

    /**
     * 过期时间字符串
     */
    private String expiration;

    /**
     * 过期时间
     */
    private Long expireTime;
    /**
     * 端点
     */
    private String endpoint;

    /**
     * 存储桶名称
     */
    private String bucketName;
    /**
     * 地域
     */
    private String region;

    /**
     * 临时密钥token
     */
    private String token;

    /**
     * 临时密钥id
     */
    private String secretId;

    /**
     * 临时密钥key
     */
    private String secretKey;

}
