package com.halo.interceptor;

import com.halo.config.WebMvcProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Description:
 * @author: halo_ry
 * @Date: 2022/10/10 18:01
 */
@Slf4j
public class XesOriginInterceptor implements HandlerInterceptor {

    @Autowired
    private WebMvcProperties webMvcProperties;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String xesOriginValue = request.getHeader(WebMvcProperties.XesOrigin.HEADER_XES_ORIGIN);
        if (log.isInfoEnabled()) {
            log.info("请求[{}]进入拦截器XesOriginInterceptor，Header[xes-origin={}]", request.getRequestURL(), xesOriginValue);
        }
        WebMvcProperties.XesOrigin xesOrigin = webMvcProperties.getXesOrigin();
        if (xesOrigin == null || !xesOrigin.isValidated()) {
            return true;
        }
        if (StringUtils.isBlank(xesOriginValue)) {
            throw new IllegalArgumentException("请求头中xes-origin不能为空");
        }
        boolean isValid = xesOrigin.isValidXesOrigin(xesOriginValue);
        if (!isValid) {
            throw new IllegalArgumentException("无效的xes-origin");
        }
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
    }
}
