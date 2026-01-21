package com.halo.enums;

import lombok.Getter;

/**
 * 图片方向枚举
 * 用于标识图片的宽高比例方向
 *
 * @author Generated
 * @date 2026/01/21
 */
@Getter
public enum ImageOrientation {
    
    /**
     * 横版（宽度 > 高度）
     */
    LANDSCAPE("横版"),
    
    /**
     * 竖版（宽度 < 高度）
     */
    PORTRAIT("竖版"),
    
    /**
     * 正方形（宽度 == 高度）
     */
    SQUARE("正方形");

    /**
     * -- GETTER --
     *  获取方向描述
     *
     * @return 方向描述
     */
    private final String description;
    
    ImageOrientation(String description) {
        this.description = description;
    }

}
