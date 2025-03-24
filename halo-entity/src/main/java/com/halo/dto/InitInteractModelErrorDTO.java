package com.halo.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import lombok.Builder;
import lombok.Data;

import java.util.Map;

/**
 * @Description:
 * @Author: halo_ry
 * @Date: 2025/3/22 21:22
 */
@Data
@Builder
@HeadRowHeight(value = 40)
public class InitInteractModelErrorDTO {

    /**
     * 班级ID
     */
    @ExcelProperty(value = "序号", index = 0)
    @ColumnWidth(value = 10)
    private Integer rowIndex;

    /**
     * 班级ID
     */
    @ExcelProperty(value = "班级ID", index = 1)
    @ColumnWidth(value = 10)
    private String classId;

    /**
     * 产品ID
     */
    @ExcelProperty(value = "产品ID", index = 2)
    @ColumnWidth(value = 40)
    private String productId;

    /**
     * 互动模型
     */
    @ExcelProperty(value = "交互模式", index = 3)
    @ColumnWidth(value = 10)
    private Integer interactModel;

    /**
     * 错误信息
     */
    @ExcelProperty(value = "错误信息", index = 4)
    @ColumnWidth(value = 100)
    private String errorMsg;

    private Map<String, Object> map;


}
