package com.halo.demo.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Logger;
import feign.codec.Decoder;
import feign.codec.Encoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.support.ResponseEntityDecoder;
import org.springframework.cloud.openfeign.support.SpringDecoder;
import org.springframework.cloud.openfeign.support.SpringEncoder;
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

    @Bean("defaultHttpMessageConverters")
    @ConditionalOnMissingBean(HttpMessageConverters.class)
    public HttpMessageConverters httpMessageConverters() {
        MappingJackson2HttpMessageConverter jacksonConverter = new MappingJackson2HttpMessageConverter();
        jacksonConverter.setSupportedMediaTypes(Arrays.asList(MediaType.parseMediaType("application/json;charset=UTF-8"), MediaType.APPLICATION_OCTET_STREAM));
        jacksonConverter.setObjectMapper(objectMapper);

        StringHttpMessageConverter stringHttpMessageConverter = new StringHttpMessageConverter();
        stringHttpMessageConverter.setSupportedMediaTypes(Arrays.asList(MediaType.parseMediaType("application/json;charset=UTF-8"),
                MediaType.APPLICATION_OCTET_STREAM));
        stringHttpMessageConverter.setDefaultCharset(StandardCharsets.UTF_8);
        return new HttpMessageConverters(jacksonConverter, stringHttpMessageConverter);
    }

    @Bean("defaultDecoder")
    @ConditionalOnMissingBean(Decoder.class)
    public Decoder feignDecoder() {
        return new ResponseEntityDecoder(new SpringDecoder(this::httpMessageConverters));
    }

    @Bean("defaultEncoder")
    @ConditionalOnMissingBean(Encoder.class)
    public Encoder feignEncoder() {
        return new SpringEncoder(this::httpMessageConverters);
    }

    /**
     * 配置Feign日志级别
     *
     * @return
     */
    @Bean("defaultLoggerLevel")
    @ConditionalOnMissingBean(Logger.Level.class)
    public Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }
}
