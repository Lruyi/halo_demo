package com.halo.dto.resp;

import lombok.Data;

/**
 * B服务响应DTO
 * 
 * @author halo_ry
 */
@Data
public class ServiceBResponse {
    
    /**
     * 验证结果
     */
    private Boolean success;
    
    /**
     * 消息
     */
    private String message;
}
