package com.halo.filter;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.halo.utils.IsIntegerCheck;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Map;
import java.util.Objects;

/**
 * @Description: 目前没有配置filterConfig所以不会走到这个过滤器
 * @Author: liuruyi
 * @Date: 2026/1/16 15:01
 */
@Slf4j
public class XesPlatformFilter implements Filter {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        // 强制转换为 HttpServletRequest
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        // 用自定义包装器包装请求
        CachingRequestWrapper wrapper = new CachingRequestWrapper(httpRequest);

        // 获取 header 中 "xes-platform" 值
        String xesPlatform = httpRequest.getHeader("xes_platform_header");
        // 二进制平台码 TODO 伪代码
        Object binaryPlatform = null;
        // 获取请求体内容（JDK8兼容写法）
        String body = wrapper.getBody();
        if (StringUtils.isNotBlank(body)) {
            // 解析 JSON 成 Map 对象
            Map<String, Object> bodyMap = objectMapper.readValue(body, new TypeReference<Map<String, Object>>() {});

            if (StringUtils.isNotBlank(xesPlatform)) {
                // header 有值，则使用 header 值转换
                if (!IsIntegerCheck.isInteger(xesPlatform)) {
                    throw new IllegalArgumentException("无效的xes-platform:[" + xesPlatform + "],请检查");
                }
                // 校验平台码是否合法
//                Platform platform = Platform.valueOf(Integer.parseInt(xesPlatform));
//                if (Objects.equals(platform, Platform.UNKNOWN)) {
//                    throw new IllegalArgumentException("无效的xes-platform:[" + xesPlatform + "],请检查");
//                }
                // 转换二进制平台码
//                binaryPlatform = PlatformConverter.convertToBinaryPlatform(platform);
//                log.info("header传xes-platform时xesPlatform:[{}]转换后:[{}]", xesPlatform, Objects.nonNull(binaryPlatform) ? binaryPlatform.getCode() : null);
            } else {
                // 获取请求路径 context-path
                String contextPath = ((HttpServletRequest) request).getContextPath();
                // 优惠券服务的请求路径为 /promotion，不需要转换，本身传的就是二进制平台码
                if (!Objects.equals(contextPath, "coupon_context_path")) {
                    // header 无值，则使用 body 中的 platform 字段转换
                    Object platformObj = bodyMap.get("PLATFORM_BODY");
                    String platformStr = Objects.nonNull(platformObj) ? platformObj.toString() : StringUtils.EMPTY;
                    if (StringUtils.isNotBlank(platformStr)) {
                        if (!IsIntegerCheck.isInteger(platformStr)) {
                            throw new IllegalArgumentException("无效的xes-platform:[" + xesPlatform + "],请检查");
                        }
//                        PlatformConverter.EPlatform ePlatform = PlatformConverter.EPlatform.valueOf(Integer.parseInt(platformStr));
                        // 转换二进制平台码
//                        binaryPlatform = PlatformConverter.convertToBinaryPlatform(ePlatform);
//                        log.info("header未传xes-platform时body中platform:[{}]转换后值:[{}]", platformStr, Objects.nonNull(binaryPlatform) ? binaryPlatform.getCode() : null);
                    }
                }
            }

            if (Objects.nonNull(binaryPlatform)) {
                // 更新 platform 字段
//                bodyMap.put("platform", binaryPlatform.getCode());
//                log.info("接口 [" + ((HttpServletRequest) request).getServletPath() + "] 经过XesPlatformFilter转换后的platform值: {}", binaryPlatform.getCode());
                // 将修改后的 Map 转换成 JSON 字符串
                String modifiedBody = objectMapper.writeValueAsString(bodyMap);
                // 设置修改后的内容到包装器中
                wrapper.setBody(modifiedBody);

            }
        }
        // 传递包装后的 request 到后续 Filter 或 Controller
        // 而原始 request 的 InputStream 已经被 wrapper 在构造函数中读取过一次（不可重复读取）
        chain.doFilter(wrapper, response);
    }
}
