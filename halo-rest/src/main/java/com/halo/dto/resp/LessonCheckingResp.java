package com.halo.dto.resp;

import lombok.Data;

/**
 * 在读班对账响应DTO
 * 
 * @author liuruyi
 */
@Data
public class LessonCheckingResp {
    
    /**
     * 业务ID
     */
    private String businessId;
    
    /**
     * 是否通过对账
     * true-对账通过, false-对账不通过
     */
    private Boolean passed;
    
    /**
     * 是否需要修复
     * true-需要修复, false-不需要修复
     */
    private Boolean needRepair;
}
