package com.halo.config;

import com.halo.interceptor.XesOriginInterceptor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Description:
 * @author: halo_ry
 * @Date: 2022/10/10 18:13
 */
@Configuration
@EnableConfigurationProperties(WebMvcProperties.class)
public class WebMvcAutoConfiguration {

    @Bean
    public XesOriginInterceptor xesOriginInterceptor() {
        return new XesOriginInterceptor();
    }
}
