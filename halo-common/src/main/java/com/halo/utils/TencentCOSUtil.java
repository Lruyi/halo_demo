package com.halo.utils;

import com.halo.annotation.ExecutionTime;
import com.halo.dto.resp.CosTmpCredentialResp;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.exception.CosServiceException;
import com.tencent.cloud.CosStsClient;
import com.tencent.cloud.Response;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.TreeMap;

/**
 * @Description:
 * @Author: halo_ry
 * @Date: 2025/9/11 16:59
 */
@Slf4j
@Component
//@RefreshScope
public class TencentCOSUtil {

    @Value("${tencent.cos.secretId:}")
    private String secretId;

    @Value("${tencent.cos.secretKey:}")
    private String secretKey;

    @Value("${tencent.cos.region:}")
    private String region;

    @Value("${tencent.cos.bucketName:}")
    private String bucketName;

    @Value("${tencent.cos.endpoint:}")
    private String endpoint;

    @Value("${tencent.cos.downLoadUrlPrefix:}")
    private String downLoadUrlPrefix;

//    @Value("${tencent.cos.appId:}")
//    private String appId;
//
//    @Value("${tencent.cos.cnamehost:}")
//    private String cnamehost;
//
//    @Value("${tencent.sts.expireTime:3600}")
//    private Integer expireTimeSeconds;
//
//    @Value("${tencent.sts.policy:}")
//    private String policy;

    @Autowired
    private COSClient cosClient;

    public CosTmpCredentialResp getCosTmpCredential(Integer duration) {
        try {
            TreeMap<String, Object> config = buildCredentialConfig(duration);
            Response response = CosStsClient.getCredential(config);
            return buildCosTmpCredentialResp(response);
        } catch (Exception e) {
            log.error("生成cos临时密钥异常: " + e.getMessage(), e);
            return null;
        }
    }

    @NotNull
    private TreeMap<String, Object> buildCredentialConfig(Integer duration) {
        TreeMap<String, Object> config = new TreeMap<>();
        // 云 api 密钥 SecretId
        config.put("secretId", secretId);
        // 云 api 密钥 SecretKey
        config.put("secretKey", secretKey);

        // 临时密钥有效时长，单位是秒 (30分钟)
        config.put("durationSeconds", duration);

        // bucket名称
        config.put("bucket", bucketName);
        // bucket所在地区
        config.put("region", region);

        // 可以通过 allowPrefixes 指定前缀数组
        config.put("allowPrefixes", new String[]{"*"});

        // 密钥的权限列表
        String[] allowActions = new String[]{
                // 简单上传
                "name/cos:PutObject",
                "name/cos:PostObject",
                // 分片上传
                "name/cos:InitiateMultipartUpload",
                "name/cos:ListMultipartUploads",
                "name/cos:ListParts",
                "name/cos:UploadPart",
                "name/cos:CompleteMultipartUpload"
        };
        config.put("allowActions", allowActions);

        try {
            Response response = CosStsClient.getCredential(config);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return config;
    }

    @NotNull
    private CosTmpCredentialResp buildCosTmpCredentialResp(Response response) {
        CosTmpCredentialResp data = new CosTmpCredentialResp();
        data.setExpiration(response.expiration);
        data.setExpireTime(response.expiredTime);
        data.setEndpoint(downLoadUrlPrefix);// 从桶拉取文件时需要（也可以是绑定到cos桶上的自定义域名）
        data.setBucketName(bucketName);
        data.setRegion(region);
        data.setToken(response.credentials.sessionToken);
        data.setSecretId(response.credentials.tmpSecretId);
        data.setSecretKey(response.credentials.tmpSecretKey);
        return data;
    }

    /**
     * 上传文件
     * @param file 待上传的文件
     * @param fileKey 文件在COS中的路径+名称，例如：folder/subfolder/filename.ext
     * @return
     */
    @ExecutionTime("上传文件到COS")
    public String uploadFile(File file, String fileKey) {
        try {
            cosClient.putObject(bucketName, fileKey, file);
            log.info("uploadFile | putObjectResult successful");
        } catch (CosServiceException e) {
            log.error("uploadFile | fail to upload file to cos");
            throw new RuntimeException("fail to upload file to cos", e);
        }
        String url = downLoadUrlPrefix + fileKey;
        log.info("uploadFile | return url is {}", url);
        return url;
    }
}
