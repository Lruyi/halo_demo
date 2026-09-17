package com.halo.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JacksonConfig {

    /**
     * 显式注册 Jackson 2.x ObjectMapper Bean，供项目中使用 com.fasterxml.jackson 的组件注入。
     * Spring Boot 4 自动配置的是 Jackson 3.x (tools.jackson) 的 ObjectMapper，两者是不同类型。
     */
    @Bean("objectMapper")
    @ConditionalOnMissingBean(ObjectMapper.class)
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }
}
