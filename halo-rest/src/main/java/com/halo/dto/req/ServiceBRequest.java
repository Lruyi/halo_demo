package com.halo.dto.req;

import lombok.Data;

/**
 * B服务请求DTO
 * 
 * @author halo_ry
 */
@Data
public class ServiceBRequest {
    
    /**
     * 员工编号
     */
    private String empNo;
    
    /**
     * 签名
     */
    private String sign;
}
