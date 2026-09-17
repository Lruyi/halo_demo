package com.halo.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

import java.time.Duration;

/**
 * @author:
 * @date: 2026/04/17 18:43
 * @description:
 */

@Configuration
public class RestClientConfig {

    @Value("${callback.connect-timeout-ms:5000}")
    private int connectTimeoutMs;

    @Value("${callback.read-timeout-ms:10000}")
    private int readTimeoutMs;

    @Bean
    public RestClient callbackRestClient() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(Duration.ofMillis(connectTimeoutMs));
        factory.setReadTimeout(Duration.ofMillis(readTimeoutMs));

        return RestClient.builder()
                .defaultHeader("Content-Type", "application/json")
                .requestFactory(factory)
                .build();
    }
}
