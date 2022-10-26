package com.halo.controller;

import cn.hutool.core.lang.UUID;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import com.alibaba.excel.write.style.HorizontalCellStyleStrategy;
import com.alibaba.excel.write.style.column.LongestMatchColumnWidthStyleStrategy;
import com.google.common.collect.Lists;
import com.halo.common.Result;
import com.halo.constant.ErrorCode;
import com.halo.convert.StringConverter;
import com.halo.dto.DownloadDataDTO;
import com.halo.dto.ProductIdDTO;
import com.halo.exception.BusinessException;
import com.halo.listener.ProductIdImportListener;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Description:
 * @author: halo_ry
 * @Date: 2022/10/22 16:47
 */
@Slf4j
@RestController
@RequestMapping("file")
public class EasyExcelController {


    /**
     * xls的ContentType
     */
    public static final String XLS_CONTENT_TYPE = "application/vnd.ms-excel";

    /**
     * xlsx的ContentType
     */
    public static final String XLSX_CONTENT_TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";

    /**
     * 下载demo excel模板
     * @param response
     * @throws IOException
     */
    @GetMapping("downloadTemplateExcel")
    public void downloadTemplateExcel(HttpServletResponse response) throws IOException {
        // 这里注意 有同学反应使用swagger 会导致各种问题，请直接用浏览器或者用postman
        // xlsx的ContentType
        response.setContentType(XLSX_CONTENT_TYPE);
        response.setCharacterEncoding("utf-8");
        // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
        String fileName = URLEncoder.encode("测试", "UTF-8").replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
        EasyExcel.write(response.getOutputStream(), DownloadDataDTO.class).sheet("模版").doWrite(data());
    }

    /**
     * 导出Excel文件
     * 下载商品ID导入模板
     * @param response
     */
    @GetMapping("downloadProductIdTemplateExcel")
    public void downloadProductIdTemplateExcel(HttpServletResponse response) {
        try {
            // xlsx的ContentType
            response.setContentType(XLSX_CONTENT_TYPE);
            response.setCharacterEncoding("utf-8");
            // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
            String sheetname = "商品ID导入模板";
            String filename = "商品ID导入模板";
            String fileName = URLEncoder.encode(filename, "UTF-8").replaceAll("\\+", "%20");
            List<ProductIdDTO> data = new ArrayList<>();
            data.add(new ProductIdDTO(1, "4ceba22b86034b39a780e83f8ff464ae"));
            response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");

            //表头样式
            WriteCellStyle headWriteCellStyle = new WriteCellStyle();
            //设置表头居中对齐
            headWriteCellStyle.setHorizontalAlignment(HorizontalAlignment.CENTER);
            headWriteCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            headWriteCellStyle.setBorderBottom(BorderStyle.THIN);
            headWriteCellStyle.setBorderLeft(BorderStyle.THIN);
            headWriteCellStyle.setBorderRight(BorderStyle.THIN);
            headWriteCellStyle.setBorderTop(BorderStyle.THIN);
            headWriteCellStyle.setFillForegroundColor(IndexedColors.SEA_GREEN.getIndex());
            headWriteCellStyle.setFillPatternType(FillPatternType.SOLID_FOREGROUND);
            //内容样式
            WriteCellStyle contentWriteCellStyle = new WriteCellStyle();
            //设置内容靠左对齐
            contentWriteCellStyle.setHorizontalAlignment(HorizontalAlignment.CENTER);
            HorizontalCellStyleStrategy horizontalCellStyleStrategy = new HorizontalCellStyleStrategy(headWriteCellStyle, contentWriteCellStyle);

            EasyExcel.write(response.getOutputStream(), ProductIdDTO.class).sheet(sheetname).registerWriteHandler(horizontalCellStyleStrategy).doWrite(data);
        } catch (IOException e) {
            log.error("productIdTemplateExcel error .", e);
            throw new BusinessException(ErrorCode.FAILURE, "下载商品ID导入模板异常");
        }

    }

    private List<DownloadDataDTO> data() {
        List<DownloadDataDTO> list = Lists.newArrayList();
        for (int i = 1; i < 5; i++) {
            DownloadDataDTO data = new DownloadDataDTO();
            data.setSort("第" + i);
            data.setDate(new Date());
            data.setDoubleData(0.56);
            list.add(data);
        }
        return list;
    }

    /**
     * 上传文件
     * @param file
     * @return
     */
    @PostMapping(value = "importProductId", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Result<List<ProductIdDTO>> importProductId(@RequestBody MultipartFile file) {
        List<ProductIdDTO> list;
        try {
            /**
             * 只需要在实体对象使用@ExcelProperty注解，读取时指定该class，即可读取，并且自动过滤了空行，对于excel的读取及其简单
             *
             * sheet    // 设置sheet，默认读取第一个
             * headRowNumber    // 设置标题所在行数
             */
            list = EasyExcel.read(file.getInputStream()).head(ProductIdDTO.class).sheet().headRowNumber(1).doReadSync();
        } catch (IOException e) {
            log.error("importProductIdExcel error .", e);
            throw new BusinessException(ErrorCode.FAILURE, "Excel上传商品ID异常");
        }
        return Result.getSuccess(list);
    }

    /**
     * 上传文件+校验字段有效性
     * @param file
     * @return
     */
    @PostMapping("importProductIdV2")
    public Result<List<ProductIdDTO>> importProductIdV2 (@RequestBody MultipartFile file) {
        List<ProductIdDTO> productIdDTOList;
        try {
            /**
             * registerReadListener  // 注册监听器，可以在这里校验字段
             * headRowNumber // 设置标题所在行数
             */
            productIdDTOList = EasyExcel.read(file.getInputStream())
                    .registerConverter(new StringConverter())
                    .registerReadListener(new ProductIdImportListener())
                    .head(ProductIdDTO.class)
                    .sheet()
                    .headRowNumber(1)
                    .doReadSync();
        } catch (IOException e) {
            log.error("importProductIdExcel error .", e);
            throw new BusinessException(ErrorCode.FAILURE, "Excel上传商品ID异常");
        }
        return Result.getSuccess(productIdDTOList);
    }

    /**
     * 导出Excel文件
     * @param response
     * @return
     */
    @GetMapping("exportProductId")
    public Result<Object> exportProductId(HttpServletResponse response) {
        // 读取数据，这里mock一些数据
        List<ProductIdDTO> productIds = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            ProductIdDTO dto = ProductIdDTO.builder()
                    .productId(UUID.fastUUID().toString(true))
                    .index(i)
                    .build();
            productIds.add(dto);
        }

        try {
            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding("utf-8");
            // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
            String fileName = URLEncoder.encode("导出商品ID", "UTF-8");
            response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");

//            //表头样式
//            WriteCellStyle headWriteCellStyle = new WriteCellStyle();
//            //设置表头居中对齐
//            headWriteCellStyle.setHorizontalAlignment(HorizontalAlignment.CENTER);
//            headWriteCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
//            headWriteCellStyle.setBorderBottom(BorderStyle.THIN);
//            headWriteCellStyle.setBorderLeft(BorderStyle.THIN);
//            headWriteCellStyle.setBorderRight(BorderStyle.THIN);
//            headWriteCellStyle.setBorderTop(BorderStyle.THIN);
//            headWriteCellStyle.setFillForegroundColor(IndexedColors.SEA_GREEN.getIndex());
//            headWriteCellStyle.setFillPatternType(FillPatternType.SOLID_FOREGROUND);
//            //内容样式
//            WriteCellStyle contentWriteCellStyle = new WriteCellStyle();
//            //设置内容靠左对齐
//            contentWriteCellStyle.setHorizontalAlignment(HorizontalAlignment.CENTER);
//            HorizontalCellStyleStrategy horizontalCellStyleStrategy = new HorizontalCellStyleStrategy(headWriteCellStyle, contentWriteCellStyle);


            /**
             * sheet    // sheet0 写第一个sheet
             * sheetName    // 给sheet0设置name，默认不设置时，sheet0
             * registerWriteHandler     // 设置字段宽度为自动调整，不太精确
             * registerWriteHandler     // 设置表格样式
             */
            EasyExcel.write(response.getOutputStream(), ProductIdDTO.class)
                    .sheet("这是个sheet页")
                    .registerWriteHandler(new LongestMatchColumnWidthStyleStrategy())
//                    .registerWriteHandler(horizontalCellStyleStrategy)
                    .doWrite(productIds);
        } catch (IOException e) {
            log.error("exportProductId error:", e);
            throw new BusinessException(ErrorCode.BUSINESS_EXCEL_EXPORT_ERROR, "导出商品ID异常");
        }
        return Result.getSuccess("EXPORT EXCEL SUCCESS");
    }
}
