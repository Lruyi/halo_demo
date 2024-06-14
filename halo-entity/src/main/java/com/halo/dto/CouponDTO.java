package com.halo.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description:
 * @author: halo_ry
 * @Date: 2024/3/1 16:42
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@HeadRowHeight(value = 40)
public class CouponDTO {

    @ExcelProperty(value = "优惠劵码", index = 0)
    @ColumnWidth(value = 40)
    private String couponCode;

    @ExcelProperty(value = "使用时间", index = 1)
    @ColumnWidth(value = 20)
    private Integer useTime;

    @ExcelProperty(value = "业务ID", index = 2)
    @ColumnWidth(value = 40)
    private String businessId;
}
