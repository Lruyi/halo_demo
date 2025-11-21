package com.halo.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import lombok.Data;

/**
 * 游戏内容Excel数据DTO
 * 对应Excel表格的11列数据
 * 
 * @author halo_ry
 * @Date: 2025/01/XX
 */
@Data
@HeadRowHeight(value = 30)
public class GameContentDTO {

    /**
     * 项目
     */
    @ExcelProperty(value = "项目", index = 0)
    @ColumnWidth(value = 20)
    private String project;

    /**
     * 游戏模版
     */
    @ExcelProperty(value = "游戏模版", index = 1)
    @ColumnWidth(value = 25)
    private String gameTemplate;

    /**
     * 季节讲次
     */
    @ExcelProperty(value = "季节讲次", index = 2)
    @ColumnWidth(value = 15)
    private String seasonLectureOrder;

    /**
     * 游戏序号
     */
    @ExcelProperty(value = "游戏序号", index = 3)
    @ColumnWidth(value = 12)
    private String gameNumber;

    /**
     * 关卡数量
     */
    @ExcelProperty(value = "关卡数量", index = 4)
    @ColumnWidth(value = 12)
    private String levelQuantity;

    /**
     * 游戏信息
     */
    @ExcelProperty(value = "游戏信息", index = 5)
    @ColumnWidth(value = 20)
    private String gameInfo;

    /**
     * 题干序号 (tg)
     */
    @ExcelProperty(value = "题干序号", index = 6)
    @ColumnWidth(value = 15)
    private String questionStemNumber;

    /**
     * 题干信息
     */
    @ExcelProperty(value = "题干信息", index = 7)
    @ColumnWidth(value = 40)
    private String questionStemInfo;

    /**
     * 选项序号 (xx)
     */
    @ExcelProperty(value = "选项序号", index = 8)
    @ColumnWidth(value = 15)
    private String optionNumber;

    /**
     * 选项信息
     */
    @ExcelProperty(value = "选项信息", index = 9)
    @ColumnWidth(value = 40)
    private String optionInfo;

    /**
     * AI提示词 (需求描述)
     */
    @ExcelProperty(value = "AI提示词", index = 10)
    @ColumnWidth(value = 50)
    private String aiPrompt;
}

