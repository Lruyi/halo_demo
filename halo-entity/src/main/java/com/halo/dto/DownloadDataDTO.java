package com.halo.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * @Description:
 * @author: halo_ry
 * @Date: 2022/10/22 17:11
 */
@Getter
@Setter
@HeadRowHeight(value = 40)
public class DownloadDataDTO {

    @ExcelProperty("序号")
    @ColumnWidth(value = 10)
    private String sort;

    @ExcelProperty("日期标题")
    @ColumnWidth(value = 30)
    private Date date;

    @ExcelProperty("数字标题")
    @ColumnWidth(value = 20)
    private Double doubleData;
}
