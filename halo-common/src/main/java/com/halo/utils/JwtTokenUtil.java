package com.halo.utils;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author: wangweichang@tal.com
 * @date: 2025/10/31 10:51
 * @description:
 */
@Component
public class JwtTokenUtil {

    public static final String AUTHORITIES = "authorities";

    public static final String USERNAME = "username";
    
    @Value("${jwt.secret}")
    private String secret;

    private SecretKey secretKey;

    /**
     * AccessToken过期时间（2小时）
     */
    private final static long ACCESS_TOKEN_EXPIRATION = 10 * 3600 * 1000;

    /**
     * RefreshToken过期时间（1天）
     */
    private final static long REFRESH_TOKEN_EXPIRATION = 24 * 3600 * 1000L;

    @PostConstruct
    public void init() {
        byte[] decode = Base64.getDecoder().decode(secret);
        this.secretKey = Keys.hmacShaKeyFor(decode);
        System.out.println(secretKey);
    }

    /**
     * 生成AccessToken（使用默认过期时间）
     *
     * @param userId      用户ID
     * @param username    用户名称
     * @param authorities 权限列表
     * @return AccessToken
     */
    public String generateToken(String userId, String username, Collection<? extends GrantedAuthority> authorities) {
        return generateToken(userId, username, authorities, ACCESS_TOKEN_EXPIRATION);
    }

    /**
     * 生成AccessToken（指定过期时间）
     *
     * @param userId      用户ID
     * @param username    用户名称
     * @param authorities 权限列表
     * @param expiration  过期时间（毫秒），如果为null则使用默认过期时间
     * @return AccessToken
     */
    public String generateToken(String userId, String username, Collection<? extends GrantedAuthority> authorities, Long expiration) {
        long effectiveExpiration = (expiration != null && expiration > 0) ? expiration : ACCESS_TOKEN_EXPIRATION;
        return generateToken(userId, username, authorities, effectiveExpiration);
    }

    /**
     * 生成RefreshToken（使用默认过期时间）
     *
     * @param userId      用户ID
     * @param username    用户名称
     * @param authorities 权限列表
     * @return RefreshToken
     */
    public String generateRefreshToken(String userId, String username, Collection<? extends GrantedAuthority> authorities) {
        return generateToken(userId, username, authorities, REFRESH_TOKEN_EXPIRATION);
    }

    /**
     * 生成RefreshToken（指定过期时间）
     *
     * @param userId      用户ID
     * @param username    用户名称
     * @param authorities 权限列表
     * @param expiration  过期时间（毫秒），如果为null则使用默认过期时间
     * @return RefreshToken
     */
    public String generateRefreshToken(String userId, String username, Collection<? extends GrantedAuthority> authorities, Long expiration) {
        long effectiveExpiration = (expiration != null && expiration > 0) ? expiration : REFRESH_TOKEN_EXPIRATION;
        return generateToken(userId, username, authorities, effectiveExpiration);
    }

    /**
     * 生成Token（通用方法）
     *
     * @param userId      用户ID
     * @param username    用户名称
     * @param authorities 权限列表
     * @param expiration  过期时间（毫秒）
     * @return Token
     */
    private String generateToken(String userId, String username, Collection<? extends GrantedAuthority> authorities, long expiration) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(AUTHORITIES, authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet()));
        claims.put(USERNAME, username);

        Date date = new Date();
        return Jwts.builder()
                .claims(claims)
                .subject(userId)
                .issuedAt(date)
                .expiration(new Date(date.getTime() + expiration))
                .signWith(secretKey)
                .compact();
    }

    public String extractSubject(String token) {
        return extractClaims(token, Claims::getSubject);
    }

    public String extractUsername(String token) {
        return extractClaims(token, claims -> claims.get(USERNAME, String.class));
    }

    public Date extractExpireTime(String token) {
        return extractClaims(token, Claims::getExpiration);
    }

    public <T> T extractClaims(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public Claims extractAllClaims(String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload();
    }

    public static void main(String[] args) throws NoSuchAlgorithmException {
        // 指定算法为HMAC SHA-256
        String algorithm = "HmacSHA256"; // 可改为 HmacSHA384 或 HmacSHA512
        KeyGenerator keyGenerator = KeyGenerator.getInstance(algorithm);
        keyGenerator.init(256); // 指定密钥长度：256位

        // 生成密钥
        SecretKey secretKey = keyGenerator.generateKey();

        // 将密钥编码为Base64以便存储
        String base64Secret = Base64.getEncoder().encodeToString(secretKey.getEncoded());

        System.out.println("Generated JWT Secret: " + base64Secret);

        // 转为 SecretKey
        byte[] decode = Base64.getDecoder().decode(base64Secret);
        SecretKey secret = Keys.hmacShaKeyFor(decode);
        System.out.println(secret);
    }


}
