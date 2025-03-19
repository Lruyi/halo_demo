package com.halo.dto.req;

import lombok.Data;

/**
 * @Description:
 * @Author: halo_ry
 * @Date: 2025/3/5 10:50
 */
@Data
public class InitInteractModelBody {

    /**
     * 班级ID、商品ID
     */
    private String classId;

    /**
     * 产品ID
     */
    private String productId;

    /**
     * 互动模型
     */
    private Integer interactModel;
}
