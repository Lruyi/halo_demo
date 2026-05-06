package com.halo.security;

import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

/**
 * JWT认证详情类，扩展WebAuthenticationDetails以存储用户名
 *
 * @author: wangweichang@tal.com
 * @date: 2025/01/07
 * @description: 在认证上下文中存储从JWT token中提取的用户名，支持多线程场景
 */
@Getter
public class JwtAuthenticationDetails extends WebAuthenticationDetails {

    /**
     * 用户名（从JWT token的claims中提取）
     */
    private final String username;

    public JwtAuthenticationDetails(HttpServletRequest request, String username) {
        super(request);
        this.username = username;
    }
}

