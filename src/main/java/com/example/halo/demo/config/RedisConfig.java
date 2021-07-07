package com.example.halo.demo.config;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * @Description: 自定义一些Redis的配置
 * @Author: Halo_ry
 * @Date: 2020/4/7 16:05
 */
@Configuration
@EnableCaching //启用缓存，这个注解很重要
public class RedisConfig {

//    @Bean
//    public RedisConnectionFactory redisConnectionFactory() {
//        JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory();
//
//        return jedisConnectionFactory;
//    }

    @Bean(name = "redisTemplate")
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);
        // 字符串
        RedisSerializer stringSerializer = new StringRedisSerializer();
        template.setDefaultSerializer(stringSerializer);
//        RedisSerializer<Object> serializer = new Jackson2JsonRedisSerializer<>(Object.class);
//        template.setDefaultSerializer(serializer);
        return template;
    }
}
