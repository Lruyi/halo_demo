package com.halo.filter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Description: 过滤器，拦截URL，处理访问权限
 * @Author: Halo_ry
 * @Date: 2020/4/5 11:00
 *
 *  // @WebFilter(filterName = "accessUpdateLog", urlPatterns = {"/*"})
 */
@Component
@WebFilter(urlPatterns = {"/*"})
@Slf4j
public class CheckResourceFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // 项目启动时就执行的方法
        log.info("进入过滤器init方法，开始初始化。。。。");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        try {
            // 拦截请求
            log.info("进入过滤器。。。。");
            HttpServletRequest request = (HttpServletRequest) servletRequest;
            HttpServletResponse response = ((HttpServletResponse)servletResponse);
            // 放行，在这之前可以做一些白名单什么的
            // 放行后，执行真正的后台接口逻辑
            filterChain.doFilter(request, response);
        } finally {
            // 拦截响应
            log.info("走出过滤器。。。。");
        }
    }
}
