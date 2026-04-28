package com.halo.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * @Description: 拦截器，拦截URL，处理访问权限
 * @Author: Halo_ry
 * @Date: 2020/4/5 11:03
 */
@Slf4j
@Component
public class CheckResourceInterceptor implements HandlerInterceptor {

    public static final ThreadLocal<Integer> BUSINESS_LINE_THREAD_LOCAL = new ThreadLocal<>();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        String businessLine = request.getHeader("businessLine");
        if (StringUtils.isNotBlank(businessLine)) {
            BUSINESS_LINE_THREAD_LOCAL.set(Integer.parseInt(businessLine));
        }
        log.info("进入拦截器。。。");
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
                                @Nullable Exception ex) throws Exception {
        BUSINESS_LINE_THREAD_LOCAL.remove();
    }
}
