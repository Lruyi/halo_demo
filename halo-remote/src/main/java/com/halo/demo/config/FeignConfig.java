package com.halo.demo.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cloud.openfeign.support.HttpMessageConverterCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

/**
 * @Description: Feign配置
 * @Author: Halo_ry
 * @Date: 2023/2/8 17:51
 */
@Configuration("defaultFeignConfig")
public class FeignConfig {

    @Autowired(required = false)
    ObjectMapper objectMapper;

    @Bean("defaultHttpMessageConverterCustomizer")
    public HttpMessageConverterCustomizer feignHttpMessageConverterCustomizer() {
        return converters -> {
            converters.removeIf(c -> c instanceof MappingJackson2HttpMessageConverter);
            MappingJackson2HttpMessageConverter jacksonConverter = new MappingJackson2HttpMessageConverter();
            jacksonConverter.setSupportedMediaTypes(Arrays.asList(
                    MediaType.parseMediaType("application/json;charset=UTF-8"),
                    MediaType.APPLICATION_OCTET_STREAM));
            if (objectMapper != null) {
                jacksonConverter.setObjectMapper(objectMapper);
            }
            converters.add(0, jacksonConverter);

            converters.removeIf(c -> c instanceof StringHttpMessageConverter);
            StringHttpMessageConverter stringConverter = new StringHttpMessageConverter();
            stringConverter.setSupportedMediaTypes(Arrays.asList(
                    MediaType.parseMediaType("application/json;charset=UTF-8"),
                    MediaType.APPLICATION_OCTET_STREAM));
            stringConverter.setDefaultCharset(StandardCharsets.UTF_8);
            converters.add(1, stringConverter);
        };
    }

    /**
     * 配置Feign日志级别
     */
    @Bean("defaultLoggerLevel")
    @ConditionalOnMissingBean(Logger.Level.class)
    public Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }
}
