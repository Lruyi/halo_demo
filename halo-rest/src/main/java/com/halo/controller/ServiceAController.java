package com.halo.controller;

import com.halo.common.Result;
import com.halo.dto.req.ServiceARequest;
import com.halo.utils.HmacSha256Util;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * A服务 - 客户端示例
 * 使用分配的key生成HMAC-SHA256签名，调用B服务
 * 
 * @author halo_ry
 */
@RestController
@RequestMapping("/serviceA")
@Slf4j
public class ServiceAController {

    @Resource
    private RestTemplate restTemplate;

    /**
     * B服务分配给A服务的密钥
     * 实际项目中应该从配置中心或数据库获取
     */
    @Value("${service.b.secret.key:default-secret-key-123456}")
    private String secretKey;

    /**
     * B服务的接口地址
     */
    @Value("${service.b.url:http://localhost:8090/report/serviceB/verify}")
    private String serviceBUrl;

    /**
     * A服务调用B服务的示例接口
     * 
     * @param request 请求参数，包含empNo
     * @return 调用结果
     */
    @PostMapping("/callServiceB")
    public Result<String> callServiceB(@RequestBody ServiceARequest request) {
        try {
            // 1. 获取当前时间戳（毫秒）
            long timestamp = System.currentTimeMillis();
            
            // 2. 构建待签名的数据：empNo=xxx&timestamp=xxx
            String signData = HmacSha256Util.buildSignData(request.getEmpNo(), timestamp);
            
            // 3. 使用HMAC-SHA256生成签名
            String sign = HmacSha256Util.generateSign(signData, secretKey);
            
            log.info("A服务生成签名 - empNo: {}, timestamp: {}, signData: {}, sign: {}", 
                    request.getEmpNo(), timestamp, signData, sign);
            
            // 4. 构建请求头，包含时间戳
            HttpHeaders headers = new HttpHeaders();
            headers.set("timestamp", String.valueOf(timestamp));
            headers.set("Content-Type", "application/json");
            
            // 5. 构建请求体，包含empNo和sign
            Map<String, String> requestBody = new HashMap<>();
            requestBody.put("empNo", request.getEmpNo());
            requestBody.put("sign", sign);
            
            HttpEntity<Map<String, String>> httpEntity = new HttpEntity<>(requestBody, headers);
            
            // 6. 调用B服务
            ResponseEntity<Map> response = restTemplate.exchange(
                    serviceBUrl,
                    HttpMethod.POST,
                    httpEntity,
                    Map.class
            );
            
            log.info("B服务返回结果: {}", response.getBody());
            
            return Result.getSuccess("调用B服务成功，返回结果: " + response.getBody());
            
        } catch (Exception e) {
            log.error("调用B服务失败", e);
            return Result.getFail("调用B服务失败: " + e.getMessage());
        }
    }
}
