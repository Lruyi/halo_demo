package com.halo.filter;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.halo.config.SignatureFilterConfig;
import com.halo.config.SourceInfo;
import com.halo.config.SourcesConfig;
import com.halo.constant.CommonConstant;
import com.halo.filter.signature.SignatureVerifier;
import com.halo.filter.signature.SignatureVerifierFactory;
import com.halo.utils.HmacSha256Util;
import jakarta.annotation.Resource;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 签名验证过滤器（通用）
 * 支持多种签名算法（HMAC-SHA256、MD5等）
 * 支持多个端（source）接入，每个端可配置不同的签名算法
 * 签名算法必须从配置文件获取，不允许客户端通过请求头指定（确保双方约定好算法）
 * 支持 GET 和 POST 请求
 *
 * @author Generated
 * @date 2026/01/16
 */
@Slf4j
@Component
public class SignVerificationFilter implements Filter {

    @Resource
    private SourcesConfig sourcesConfig;

    @Resource
    private SignatureFilterConfig signatureFilterConfig;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String requestPath = httpRequest.getRequestURI();

        // 使用包装器缓存请求体（支持 POST 请求）
        CachingRequestWrapper wrapper = new CachingRequestWrapper(httpRequest);

        try {
            // 1. 提取请求头参数
            String source = httpRequest.getHeader(CommonConstant.SOURCE);
            String timestamp = httpRequest.getHeader(CommonConstant.TIMESTAMP);
            String signature = httpRequest.getHeader(CommonConstant.SIGNATURE);

            // 2. 从配置文件确定签名算法（优先级：SourceInfo配置 > sourceAlgorithmMap配置 > 默认配置）
            // 不允许客户端通过请求头指定算法，必须双方约定好算法
            String algorithmName = null;
            
            // 2.1 优先从 SourceInfo 配置中获取
            try {
                SourceInfo sourceInfo = getSourceInfo(source);
                if (sourceInfo != null && StringUtils.isNotBlank(sourceInfo.getSignatureAlgorithm())) {
                    algorithmName = sourceInfo.getSignatureAlgorithm();
                }
            } catch (Exception e) {
                log.debug("获取SourceInfo配置失败，继续使用其他配置方式", e);
            }

            // 2.2 如果仍未获取到，使用 SignatureFilterConfig 中的 source 配置
            if (StringUtils.isBlank(algorithmName)) {
                algorithmName = signatureFilterConfig.getAlgorithmBySource(source);
            }

            log.info("签名验证请求, path: {}, source: {}, timestamp: {}, algorithm: {}", requestPath, source, timestamp, algorithmName);

            // 3. 验证请求头参数
            if (StringUtils.isBlank(source) || StringUtils.isBlank(timestamp) || StringUtils.isBlank(signature)) {
                log.warn("签名验证失败，请求头参数缺失，path: {}, source: {}, timestamp: {}, signature: {}", requestPath, source, timestamp, signature);
                sendErrorResponse((HttpServletResponse) response, "请求头参数缺失");
                return;
            }

            // 4. 验证 source 是否在配置中存在
            if (!isSourceConfigured(source)) {
                log.warn("签名验证失败，source未配置，path: {}, source: {}", requestPath, source);
                sendErrorResponse((HttpServletResponse) response, "source参数不正确");
                return;
            }

            // 5. 验证时间戳有效性（使用配置的时间戳有效期）
            long timestampValidity = signatureFilterConfig.getTimestampValidity() != null 
                    ? signatureFilterConfig.getTimestampValidity() 
                    : 10 * 60 * 1000L;
            if (!HmacSha256Util.isTimestampValid(timestamp, timestampValidity)) {
                log.warn("签名验证失败，时间戳过期，path: {}, source: {}, timestamp: {}", 
                        requestPath, source, timestamp);
                sendErrorResponse((HttpServletResponse) response, "签名已过期");
                return;
            }

            // 6. 获取签名验证器
            SignatureVerifier verifier = SignatureVerifierFactory.getVerifier(algorithmName);
            if (verifier == null) {
                log.warn("签名验证失败，不支持的签名算法，path: {}, source: {}, algorithm: {}", 
                        requestPath, source, algorithmName);
                sendErrorResponse((HttpServletResponse) response, "不支持的签名算法");
                return;
            }

            // 7. 获取请求参数（支持 GET 和 POST）
            Map<String, String> params = extractRequestParams(wrapper, httpRequest);

            // 8. 获取密钥（根据 source 获取对应的密钥）
            String secretKey;
            try {
                secretKey = sourcesConfig.getSourceSecretKey(source);
            } catch (Exception e) {
                log.error("获取密钥失败，path: {}, source: {}", requestPath, source, e);
                sendErrorResponse((HttpServletResponse) response, "系统配置错误");
                return;
            }

            // 9. 构建签名数据（包含 timestamp）
            Map<String, String> signParams = new HashMap<>(params);
            signParams.put("timestamp", timestamp);

            // 10. 验证签名（使用策略模式，支持多种算法）
            if (!verifier.verify(signParams, secretKey, signature)) {
                log.warn("签名验证失败，签名验证不通过，path: {}, source: {}, algorithm: {}, timestamp: {}, params: {}",
                        requestPath, source, algorithmName, timestamp, params);
                sendErrorResponse((HttpServletResponse) response, "签名不正确");
                return;
            }

            log.info("签名验证通过，path: {}, source: {}, algorithm: {}", requestPath, source, algorithmName);

            // 11. 验证通过，继续处理请求
            chain.doFilter(wrapper, response);

        } catch (Exception e) {
            log.error("签名验证异常，path: {}", requestPath, e);
            sendErrorResponse((HttpServletResponse) response, "系统异常");
        }
    }

    /**
     * 提取请求参数（支持 GET 和 POST）
     * GET 请求：从 query parameters 获取
     * POST 请求：从 JSON body 获取
     *
     * @param wrapper   请求包装器（包含缓存的请求体）
     * @param request   HTTP 请求
     * @return 参数 Map
     */
    private Map<String, String> extractRequestParams(CachingRequestWrapper wrapper, HttpServletRequest request)
            throws IOException {
        Map<String, String> params = new HashMap<>();
        String method = request.getMethod();

        if ("GET".equalsIgnoreCase(method)) {
            // GET 请求：从 query parameters 获取
            Map<String, String[]> parameterMap = request.getParameterMap();
            for (Map.Entry<String, String[]> entry : parameterMap.entrySet()) {
                String key = entry.getKey();
                String[] values = entry.getValue();
                if (values != null && values.length > 0 && StringUtils.isNotBlank(values[0])) {
                    params.put(key, values[0]);
                }
            }
        } else if ("POST".equalsIgnoreCase(method)) {
            // POST 请求：从 JSON body 获取
            String body = wrapper.getBody();
            if (StringUtils.isNotBlank(body)) {
                try {
                    Map<String, Object> bodyMap = objectMapper.readValue(body, new TypeReference<>() {});
                    for (Map.Entry<String, Object> entry : bodyMap.entrySet()) {
                        String key = entry.getKey();
                        Object value = entry.getValue();
                        if (value != null) {
                            params.put(key, String.valueOf(value));
                        }
                    }
                } catch (Exception e) {
                    log.error("解析POST请求体失败，body: {}", body, e);
                    throw new IllegalArgumentException("请求体格式错误");
                }
            }
        }

        return params;
    }

    /**
     * 发送错误响应
     *
     * @param response HTTP 响应
     * @param message  错误消息
     */
    private void sendErrorResponse(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        response.setContentType("application/json;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");

        String jsonResponse = String.format("{\"code\":\"400\",\"message\":\"%s\",\"data\":null}", message);
        response.getWriter().write(jsonResponse);
        response.getWriter().flush();
    }

    /**
     * 获取 SourceInfo 配置
     *
     * @param source 业务来源
     * @return SourceInfo 对象，如果不存在则返回 null
     */
    private SourceInfo getSourceInfo(String source) {
        if (StringUtils.isBlank(source) || sourcesConfig.getConfig() == null) {
            return null;
        }
        return sourcesConfig.getConfig().stream()
                .filter(info -> source.equals(info.getName()))
                .findFirst()
                .orElse(null);
    }

    /**
     * 检查 source 是否在配置中存在
     *
     * @param source 业务来源
     * @return true 表示已配置，false 表示未配置
     */
    private boolean isSourceConfigured(String source) {
        if (StringUtils.isBlank(source) || sourcesConfig.getConfig() == null) {
            return false;
        }
        return sourcesConfig.getConfig().stream()
                .anyMatch(info -> source.equals(info.getName()));
    }
}
