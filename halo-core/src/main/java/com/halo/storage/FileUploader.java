package com.halo.storage;

import com.halo.config.CosProperties;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.model.PutObjectRequest;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;

/**
 * @author:
 * @date: 2026/04/17 18:43
 * @description: 文件上传器 — 上传本地文件至腾讯 COS
 */
@Slf4j
@Component
public class FileUploader {

    @Autowired(required = false)
    private COSClient cosClient;

    @Resource
    private CosProperties cosProperties;

    /**
     * 上传文件到 COS
     *
     * @param localFile 本地文件
     * @param cosPath   COS 路径（相对路径，不含 bucket）
     * @return 文件访问 URL
     */
    public String upload(File localFile, String cosPath) {
        if (cosClient == null) {
            throw new IllegalStateException("COS 未配置，无法上传文件");
        }
        log.info("[上传] 开始上传到 COS, cosPath={}, 文件大小: {} bytes", cosPath, localFile.length());

        PutObjectRequest putRequest = new PutObjectRequest(cosProperties.getFileBucketName(), cosPath, localFile);
        cosClient.putObject(putRequest);

        String resultUrl = cosProperties.getDownLoadUrlPrefix() + "/" + cosPath;
        log.info("[上传] 完成, resultUrl={}", resultUrl);
        return resultUrl;
    }
}
