package com.halo.dto.resp;

import lombok.Data;

/**
 * 在读班对账接口响应DTO（外层包装）
 * 
 * @author liuruyi
 */
@Data
public class LessonCheckingApiResp {
    
    /**
     * 结果
     */
    private Boolean rlt;
    
    /**
     * 消息
     */
    private String message;
    
    /**
     * 响应码
     */
    private String code;
    
    /**
     * 数据
     */
    private LessonCheckingResp data;
}
