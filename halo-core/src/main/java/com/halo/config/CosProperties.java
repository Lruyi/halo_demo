package com.halo.config;

import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.region.Region;
import lombok.Data;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author:
 * @date: 2026/04/17 18:43
 * @description:
 */

@Data
@Configuration
@ConfigurationProperties(prefix = "cos.config")
public class CosProperties {

    private String endpoint = "";
    private String accessKeyId = "";
    private String accessKeySecret = "";
    private String fileBucketName = "";
    private String mpsRegionId = "ap-guangzhou";
    private String downLoadUrlPrefix = "";

    @Bean
    @ConditionalOnProperty(prefix = "cos.config", name = "access-key-id")
    public COSClient cosClient() {
        COSCredentials credentials = new BasicCOSCredentials(accessKeyId, accessKeySecret);
        ClientConfig clientConfig = new ClientConfig(new Region(mpsRegionId));
        return new COSClient(credentials, clientConfig);
    }
}
