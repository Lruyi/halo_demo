package com.halo.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import lombok.*;

/**
 * @Description:
 * @author: halo_ry
 * @Date: 2022/10/22 17:23
 */
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@HeadRowHeight(value = 40)
public class ProductIdDTO {

    @ExcelProperty(value = "序号", index = 0)
    @ColumnWidth(value = 10)
    private Integer index;

    @ExcelProperty(value = "商品ID", index = 1)
    @ColumnWidth(value = 40)
    private String productId;
}
