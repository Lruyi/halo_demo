package com.halo.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

/**
 * @Description:
 * @author: halo_ry
 * @Date: 2023/3/22 18:55
 */
@Data
@RefreshScope
@Configuration
@ConfigurationProperties(prefix = "logging")
public class HaloCommon {
//    @Value("${logging.rootPath}")
    private String rootPath;
//    @Value("${logging.level}")
    private String level;
}
