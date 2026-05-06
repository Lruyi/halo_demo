package com.halo.utils;

import com.halo.constant.AigcConstant;
import io.jsonwebtoken.JwtException;
import io.micrometer.common.util.StringUtils;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * JWT Token缓存工具类
 * 负责token在Redis中的增删查操作
 * Redis存储结构：
 * - key: username
 * - hashKey: "accessToken" 和 "refreshToken"
 * - value: 对应的token值
 *
 * @author: wangweichang@tal.com
 * @date: 2025/01/20
 */
@Slf4j
@Component
public class JwtTokenCacheUtil {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Resource
    private JwtTokenUtil jwtTokenUtil;

    @Value("${spring.profiles.active}")
    private String env;

    private static final String USER_TOKEN_PREFIX = "user:token:";

    /**
     * Hash key名称 - AccessToken
     */
    private static final String HASH_KEY_ACCESS_TOKEN = "accessToken";

    /**
     * Hash key名称 - RefreshToken
     */
    private static final String HASH_KEY_REFRESH_TOKEN = "refreshToken";

    /**
     * RefreshToken缓存默认有效期（天）
     */
    private static final long DEFAULT_REFRESH_TOKEN_EXPIRE_DAYS = 1;

    /**
     * 构建Redis key（使用username和source作为key）
     * 注意：test和dev环境使用相同的key，方便dev环境自测
     *
     * @param username 用户名（workCode）
     * @param source   请求来源
     * @return Redis key
     */
    private String buildRedisKey(String username, String source) {
        // test和dev环境使用相同的key，方便dev环境自测
        String effectiveEnv = "test".equals(env) || "dev".equals(env) ? "test" : env;
        String effectiveSource = StringUtils.isBlank(source) ? AigcConstant.DEFAULT : source;
        return effectiveEnv + ":" + effectiveSource + ":" + USER_TOKEN_PREFIX + username;
    }

    /**
     * 保存AccessToken到Redis（使用默认过期时间10小时）
     *
     * @param username    用户名（workCode）
     * @param accessToken AccessToken
     * @param source      请求来源
     */
    public void saveAccessToken(String username, String accessToken, String source) {
        saveAccessToken(username, accessToken, source, 10, TimeUnit.HOURS);
    }

    /**
     * 保存AccessToken到Redis（指定过期时间和单位）
     *
     * @param username    用户名（workCode）
     * @param accessToken AccessToken
     * @param source      请求来源
     * @param timeout     过期时间
     * @param unit        时间单位
     */
    public void saveAccessToken(String username, String accessToken, String source, long timeout, TimeUnit unit) {
        String redisKey = buildRedisKey(username, source);
        redisTemplate.opsForHash().put(redisKey, HASH_KEY_ACCESS_TOKEN, accessToken);
        redisTemplate.expire(redisKey, timeout, unit);
        log.debug("保存AccessToken到Redis成功，key: {}, timeout: {} {}", redisKey, timeout, unit);
    }

    /**
     * 保存accessToken和refreshToken到Redis
     *
     * @param username     用户名（workCode）
     * @param accessToken  AccessToken
     * @param refreshToken RefreshToken
     * @param source       请求来源
     */
    public void saveTokens(String username, String accessToken, String refreshToken, String source) {
        String redisKey = buildRedisKey(username, source);

        redisTemplate.opsForHash().put(redisKey, HASH_KEY_ACCESS_TOKEN, accessToken);
        redisTemplate.opsForHash().put(redisKey, HASH_KEY_REFRESH_TOKEN, refreshToken);
        redisTemplate.expire(redisKey, DEFAULT_REFRESH_TOKEN_EXPIRE_DAYS, TimeUnit.DAYS);

        log.debug("保存token到Redis成功，key: {}, accessToken: {}, refreshToken: {}", redisKey, accessToken, refreshToken);
    }

    /**
     * 从Redis中查询AccessToken
     *
     * @param username 用户名（workCode）
     * @param source   请求来源
     * @return AccessToken，如果不存在则返回null
     */
    public String getAccessToken(String username, String source) {
        if (StringUtils.isBlank(username)) {
            return null;
        }
        String redisKey = buildRedisKey(username, source);
        String accessToken = (String) redisTemplate.opsForHash().get(redisKey, HASH_KEY_ACCESS_TOKEN);
        log.debug("从Redis查询AccessToken，key: {}, accessToken: {}", redisKey, accessToken);
        return accessToken;
    }

    /**
     * 从Redis中查询RefreshToken
     *
     * @param username 用户名（workCode）
     * @param source   请求来源
     * @return RefreshToken，如果不存在则返回null
     */
    public String getRefreshToken(String username, String source) {
        if (StringUtils.isBlank(username)) {
            return null;
        }
        String redisKey = buildRedisKey(username, source);
        String refreshToken = (String) redisTemplate.opsForHash().get(redisKey, HASH_KEY_REFRESH_TOKEN);
        log.debug("从Redis查询RefreshToken，key: {}, refreshToken: {}", redisKey, refreshToken);
        return refreshToken;
    }

    /**
     * 通过AccessToken查询用户名（从token中解析）
     *
     * @param accessToken AccessToken
     * @return 用户名，如果token无效则返回null
     */
    public String getUsernameByAccessToken(String accessToken) {
        if (StringUtils.isBlank(accessToken)) {
            return null;
        }
        try {
            String username = jwtTokenUtil.extractSubject(accessToken);
            log.debug("通过AccessToken解析username，accessToken: {}, username: {}", accessToken, username);
            return username;
        } catch (JwtException e) {
            log.warn("解析AccessToken失败: {}", e.getMessage());
            return null;
        }
    }

    /**
     * 通过RefreshToken查询用户名（从token中解析）
     *
     * @param refreshToken RefreshToken
     * @return 用户名，如果token无效则返回null
     */
    public String getUsernameByRefreshToken(String refreshToken) {
        if (StringUtils.isBlank(refreshToken)) {
            return null;
        }
        try {
            String username = jwtTokenUtil.extractSubject(refreshToken);
            log.debug("通过RefreshToken解析username，refreshToken: {}, username: {}", refreshToken, username);
            return username;
        } catch (JwtException e) {
            log.warn("解析RefreshToken失败: {}", e.getMessage());
            return null;
        }
    }

    /**
     * 检查用户token是否存在
     *
     * @param username 用户名（workCode）
     * @param source   请求来源
     * @return 如果token存在返回true，否则返回false
     */
    public boolean exists(String username, String source) {
        if (StringUtils.isBlank(username)) {
            return false;
        }
        String redisKey = buildRedisKey(username, source);
        Boolean exists = redisTemplate.hasKey(redisKey);
        return exists != null && exists;
    }

    /**
     * 删除用户的token
     *
     * @param username 用户名（workCode）
     * @param source   请求来源
     */
    public void deleteToken(String username, String source) {
        if (StringUtils.isBlank(username)) {
            log.warn("删除token失败，username为空");
            return;
        }
        String redisKey = buildRedisKey(username, source);
        redisTemplate.delete(redisKey);
        log.debug("删除token成功，key: {}", redisKey);
    }
}

