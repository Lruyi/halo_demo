package com.halo.controller;

import com.halo.common.Result;
import com.halo.dto.req.ServiceBRequest;
import com.halo.dto.resp.ServiceBResponse;
import com.halo.utils.HmacSha256Util;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * B服务 - 服务端验证接口
 * 验证A服务传递过来的签名和时间戳
 * 
 * @author halo_ry
 */
@RestController
@RequestMapping("/serviceB")
@Slf4j
public class ServiceBController {

    /**
     * B服务分配给A服务的密钥
     * 实际项目中应该从配置中心或数据库获取，根据不同的A服务分配不同的key
     */
    @Value("${service.b.secret.key:default-secret-key-123456}")
    private String secretKey;

    /**
     * 时间戳有效期（毫秒），默认10分钟
     */
    private static final long TIMESTAMP_VALID_PERIOD = 10 * 60 * 1000L;

    /**
     * 验证签名接口
     * 
     * @param request 请求参数，包含empNo和sign
     * @param httpServletRequest HTTP请求，用于获取header中的timestamp
     * @return 验证结果
     */
    @PostMapping("/verify")
    public Result<ServiceBResponse> verify(@RequestBody ServiceBRequest request, 
                                          HttpServletRequest httpServletRequest) {
        try {
            // 1. 从header中获取时间戳
            String timestampStr = httpServletRequest.getHeader("timestamp");
            if (timestampStr == null || timestampStr.isEmpty()) {
                log.warn("缺少timestamp header");
                return Result.getFail("缺少timestamp参数");
            }

            long timestamp;
            try {
                timestamp = Long.parseLong(timestampStr);
            } catch (NumberFormatException e) {
                log.warn("timestamp格式错误: {}", timestampStr);
                return Result.getFail("timestamp格式错误");
            }

            // 2. 验证时间戳是否在10分钟内
            long currentTime = System.currentTimeMillis();
            long timeDiff = Math.abs(currentTime - timestamp);
            
            if (timeDiff > TIMESTAMP_VALID_PERIOD) {
                log.warn("时间戳过期 - timestamp: {}, currentTime: {}, timeDiff: {}ms", 
                        timestamp, currentTime, timeDiff);
                ServiceBResponse response = new ServiceBResponse();
                response.setSuccess(false);
                response.setMessage("时间戳过期，请求已超过10分钟有效期");
                return Result.getSuccess(response, "验证失败");
            }

            // 3. 构建待验证的签名数据（必须与A服务构建方式一致）
            String signData = HmacSha256Util.buildSignData(request.getEmpNo(), timestamp);
            
            // 4. 使用相同的密钥和算法验证签名
            boolean isValid = HmacSha256Util.verifySign(signData, secretKey, request.getSign());
            
            log.info("B服务验证签名 - empNo: {}, timestamp: {}, signData: {}, sign: {}, isValid: {}", 
                    request.getEmpNo(), timestamp, signData, request.getSign(), isValid);

            ServiceBResponse response = new ServiceBResponse();
            if (isValid) {
                response.setSuccess(true);
                response.setMessage("签名验证通过");
                return Result.getSuccess(response, "验证成功");
            } else {
                response.setSuccess(false);
                response.setMessage("签名验证失败");
                return Result.getSuccess(response, "验证失败");
            }
            
        } catch (Exception e) {
            log.error("验证签名时发生异常", e);
            ServiceBResponse response = new ServiceBResponse();
            response.setSuccess(false);
            response.setMessage("验证过程发生异常: " + e.getMessage());
            return Result.getFail("验证失败: " + e.getMessage());
        }
    }

    /**
     * 分配密钥给A服务的接口（示例）
     * 实际项目中应该通过管理后台或配置中心分配
     * 
     * @return 分配的密钥
     */
    @GetMapping("/assignKey")
    public Result<Map<String, String>> assignKey() {
        Map<String, String> result = new HashMap<>();
        result.put("key", secretKey);
        result.put("message", "这是分配给A服务的密钥，请妥善保管");
        return Result.getSuccess(result, "密钥分配成功");
    }
}
