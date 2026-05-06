package com.halo.filter;

import com.halo.constant.CommonConstant;
import com.halo.exception.AuthException;
import com.halo.security.JwtAuthenticationDetails;
import com.halo.utils.JwtTokenCacheUtil;
import com.halo.utils.JwtTokenUtil;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;

/**
 * JWT认证过滤器
 * 负责从请求头中提取JWT token，验证token有效性，并设置Spring Security认证上下文
 *
 * @author: wangweichang@tal.com
 * @date: 2025/10/31 16:33
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    /**
     * Authorization请求头名称
     */
    public static final String AUTHORIZATION_HEADER = "Authorization";

    /**
     * Bearer token前缀
     */
    public static final String BEARER_PREFIX = "Bearer ";

    /**
     * Bearer前缀长度
     */
    private static final int BEARER_PREFIX_LENGTH = BEARER_PREFIX.length();

    private final HandlerExceptionResolver handlerExceptionResolver;
    private final JwtTokenUtil jwtTokenUtil;
    private final JwtTokenCacheUtil tokenCacheUtil;
    private final UserDetailsService userDetailService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            String token = extractTokenFromHeader(request);
            if (StringUtils.isNotBlank(token)) {
                authenticateRequest(request, token);
            }
        } catch (ExpiredJwtException e) {
            log.warn("token已过期");
            handlerExceptionResolver.resolveException(request, response, null, e);
            return;
        } catch (JwtException | UsernameNotFoundException e) {
            log.warn("JWT认证失败: " + e.getMessage(), e);
            handlerExceptionResolver.resolveException(request, response, null, e);
            return;
        } catch (Exception e) {
            log.error("JWT认证处理异常", e);
            handlerExceptionResolver.resolveException(request, response, null, e);
            return;
        }

        filterChain.doFilter(request, response);
    }

    /**
     * 从请求头中提取JWT token
     *
     * @param request HTTP请求
     * @return JWT token，如果不存在则返回null
     */
    private String extractTokenFromHeader(HttpServletRequest request) {
        String tokenHeader = request.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.isBlank(tokenHeader) || !tokenHeader.startsWith(BEARER_PREFIX)) {
            return null;
        }
        return tokenHeader.substring(BEARER_PREFIX_LENGTH);
    }

    /**
     * 验证token并设置认证上下文
     *
     * @param request HTTP请求
     * @param token   JWT token
     */
    private void authenticateRequest(HttpServletRequest request, String token) {
        String userId = validateTokenAndExtractSubject(token);
        if (StringUtils.isBlank(userId)) {
            return;
        }
        String source = request.getHeader(CommonConstant.SOURCE);
        String accessToken = tokenCacheUtil.getAccessToken(userId, source);
        if (StringUtils.isBlank(accessToken) || !StringUtils.equals(token, accessToken)) {
            throw new AuthException("token无效");
        }
        // 从token中提取用户名
        String username = jwtTokenUtil.extractUsername(token);
        UserDetails userDetails = loadUserDetails(userId);
        setAuthenticationContext(request, userDetails, username);
    }

    /**
     * 验证token并提取用户ID
     *
     * @param token JWT token
     * @return 用户ID，如果token无效则返回null
     */
    private String validateTokenAndExtractSubject(String token) {
        return jwtTokenUtil.extractSubject(token);
    }

    /**
     * 加载用户详情
     *
     * @param userId 用户ID
     * @return 用户详情
     */
    private UserDetails loadUserDetails(String userId) {
        return userDetailService.loadUserByUsername(userId);
    }

    /**
     * 设置Spring Security认证上下文
     *
     * @param request     HTTP请求
     * @param userDetails 用户详情
     * @param username    用户名（从JWT token中提取）
     */
    private void setAuthenticationContext(HttpServletRequest request, UserDetails userDetails, String username) {
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities());
        authentication.setDetails(new JwtAuthenticationDetails(request, username));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        log.debug("已为用户 {} 设置认证上下文", userDetails.getUsername());
    }
}
