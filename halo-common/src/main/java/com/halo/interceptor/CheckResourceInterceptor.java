package com.halo.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.lang.Nullable;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Description: 拦截器，拦截URL，处理访问权限
 * @Author: Halo_ry
 * @Date: 2020/4/5 11:03
 */
@Slf4j
public class CheckResourceInterceptor extends HandlerInterceptorAdapter {

    public static final ThreadLocal<Integer> BUSINESS_LINE_THREAD_LOCAL = new ThreadLocal<>();

    /**
     * This implementation always returns {@code true}.
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        String businessLine = request.getHeader("businessLine");
        if (StringUtils.isNotBlank(businessLine)) {
            BUSINESS_LINE_THREAD_LOCAL.set(Integer.parseInt(businessLine));
        }
        Integer integer = BUSINESS_LINE_THREAD_LOCAL.get();
        log.info("进入拦截器。。。");
        // 这里可以获取一些请求头里面的东西进行判断等等。。。
        return true;
    }

    /**
     * This implementation is empty.
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           @Nullable ModelAndView modelAndView) throws Exception {
    }

    /**
     * This implementation is empty.
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
                                @Nullable Exception ex) throws Exception {
        BUSINESS_LINE_THREAD_LOCAL.remove();
    }

    /**
     * This implementation is empty.
     */
    @Override
    public void afterConcurrentHandlingStarted(HttpServletRequest request, HttpServletResponse response,
                                               Object handler) throws Exception {
    }
}
