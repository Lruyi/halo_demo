package com.halo.dto.resp;

import lombok.Data;

/**
 * 在读班修复接口响应DTO
 * 
 * @author liuruyi
 */
@Data
public class LessonRepairApiResp {
    
    /**
     * 结果
     */
    private Boolean rlt;
    
    /**
     * 成功标识
     */
    private Boolean success;
    
    /**
     * 消息
     */
    private String message;
    
    /**
     * 响应码
     */
    private String code;
    
    /**
     * 数据（修复接口返回 "success" 字符串）
     */
    private String data;
}
