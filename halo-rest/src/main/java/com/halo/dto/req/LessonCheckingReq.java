package com.halo.dto.req;

import lombok.Data;

/**
 * 在读班对账/修复请求DTO
 * 
 * @author liuruyi
 */
@Data
public class LessonCheckingReq {
    
    /**
     * 学员UID
     */
    private Long studentUid;
    
    /**
     * 学员ID
     */
    private String studentId;
    
    /**
     * 班级ID
     */
    private String classId;
    
    /**
     * 报名ID
     */
    private String registId;
    
    /**
     * 商品类型
     */
    private Integer goodsType;
    
    /**
     * 产品ID
     */
    private String productId;
}
