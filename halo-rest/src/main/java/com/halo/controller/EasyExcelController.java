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
import com.halo.dto.*;
import com.halo.dto.resp.GameContentHierarchyResp;
import com.halo.exception.BusinessException;
import com.halo.listener.AnalysisInteractModelListener;
import com.halo.listener.AnalysisInteractModelListenerV2;
import com.halo.listener.GameContentListener;
import com.halo.listener.ProductIdImportListener;
import com.halo.utils.GameContentParser;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
             * doReadSync   // 同步读取excel
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
             * doReadSync   // 同步读取excel
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
     * 上传文件+校验字段有效性
     * @param file
     * @return
     */
    @PostMapping("importProductIdV3")
    public Result<Object> importProductIdV3 (@RequestBody MultipartFile file) {
        try {
            /**
             * registerReadListener  // 注册监听器，可以在这里校验字段
             * headRowNumber // 设置标题所在行数
             * doRead   // 异步读取，读取完毕后会回调监听器
             */
            EasyExcel.read(file.getInputStream())
                    .registerConverter(new StringConverter())
                    .registerReadListener(new ProductIdImportListener())
                    .head(ProductIdDTO.class)
                    .sheet()
                    .headRowNumber(1)
                    .doRead();
        } catch (IOException e) {
            log.error("importProductIdExcel error .", e);
            throw new BusinessException(ErrorCode.FAILURE, "Excel上传商品ID异常");
        }
        return Result.getSuccess("解析完全部回调");
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


    @PostMapping(value = "importCouponCode", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Result<List<CouponDTO>> importCouponCode(@RequestBody MultipartFile file) {
        List<CouponDTO> list;
        try {
            /**
             * 只需要在实体对象使用@ExcelProperty注解，读取时指定该class，即可读取，并且自动过滤了空行，对于excel的读取及其简单
             *
             * sheet    // 设置sheet，默认读取第一个
             * headRowNumber    // 设置标题所在行数
             * doReadSync   // 同步读取excel
             */
            list = EasyExcel.read(file.getInputStream()).head(CouponDTO.class).sheet().headRowNumber(1).doReadSync();
        } catch (IOException e) {
            log.error("importCouponCodeExcel error .", e);
            throw new BusinessException(ErrorCode.FAILURE, "Excel上传商品ID异常");
        }

        return Result.getSuccess(list);
    }

    /**
     * 初始化交互模式，如果校验不通过，会在errorMsg中返回错误信息，导出一份错误信息Excel
     *
     * 备注：并不是在原来的Excel中，而是单独导出一个errorExcel
     *
     * @param file
     * @return
     */
    @PostMapping("importInteractModel")
    public Result<Object> InitInteractModel(@RequestBody MultipartFile file) {
        try {
            AnalysisInteractModelListener interactModelListener = new AnalysisInteractModelListener();


            /**
             * registerReadListener  // 注册监听器，可以在这里校验字段
             * headRowNumber // 设置标题所在行数
             * doRead   // 异步读取，读取完毕后会回调监听器
             */
            EasyExcel.read(file.getInputStream())
                    .registerReadListener(interactModelListener)
                    .head(InitInteractModelDTO.class)
                    .sheet()
                    .headRowNumber(1)
                    .doRead();

            // 获取校验结果
            List<InitInteractModelErrorDTO> errorDataList = interactModelListener.getErrorDataList();
            List<InitInteractModelDTO> successDataList = interactModelListener.getSuccessDataList();

            // 如果有错误数据，生成错误报告
            if (!errorDataList.isEmpty()) {
                String errorFileName = generateErrorReport(errorDataList);
                return Result.getSuccess(String.format("数据校验完成。成功：%d条，失败：%d条。错误报告已导出：%s",
                        successDataList.size(), errorDataList.size(), errorFileName));
            }

            // 处理正确的数据
            processValidData(successDataList);
            return Result.getSuccess(String.format("数据处理完成，共处理%d条数据", successDataList.size()));
        } catch (IOException e) {
            log.error("importInteractModel error .", e);
            throw new BusinessException(ErrorCode.FAILURE, "Excel上传交互模式异常");
        }
    }


    private String generateErrorReport(List<InitInteractModelErrorDTO> errorDataList) {
        // 生成错误报告文件名
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        String errorFileName = "error_report_" + timestamp + ".xlsx";

        // 导出错误数据到Excel
        String filePath = System.getProperty("java.io.tmpdir") + File.separator + errorFileName;
        EasyExcel.write(filePath, InitInteractModelErrorDTO.class)
                .sheet("错误数据")
                .doWrite(errorDataList);
        return filePath;
    }

    private void processValidData(List<InitInteractModelDTO> validDataList) {
        // 处理验证通过的数据
        for (InitInteractModelDTO data : validDataList) {
            try {
                // TODO: 实现你的业务逻辑
                log.info("Processing valid data: {}", data);
            } catch (Exception e) {
                log.error("处理数据失败: {}", data, e);
            }
        }
    }


    /**
     * 初始化交互模式，如果校验不通过，会在errorMsg中返回错误信息，导出一份错误信息Excel
     *
     * 备注：并不是在原来的Excel中，而是单独导出一个errorExcel
     *
     * @param file
     * @return
     */
    @PostMapping("importInteractModel2")
    public Result<Object> InitInteractModel2(@RequestBody MultipartFile file) {
        try {
            AnalysisInteractModelListenerV2 interactModelListener = new AnalysisInteractModelListenerV2();


            /**
             * registerReadListener  // 注册监听器，可以在这里校验字段
             * headRowNumber // 设置标题所在行数
             * doRead   // 异步读取，读取完毕后会回调监听器
             */
            EasyExcel.read(file.getInputStream())
                    .registerReadListener(interactModelListener)
                    .head(InitInteractModelDTO.class)
                    .sheet()
                    .headRowNumber(1)
                    .doRead();

            // 获取所有数据
            List<InitInteractModelDTO> allDataList = interactModelListener.getDataList();
            // 获取成功的数据
            List<InitInteractModelDTO> successDataList = interactModelListener.getSuccessDataList();

            // 生成错误报告（包含所有数据，带错误信息）
            String errorFileName = generateErrorReport2(allDataList);

            // 如果有错误数据，返回错误信息
            if (successDataList.size() < allDataList.size()) {
                return Result.getSuccess(String.format("数据校验完成。成功：%d条，失败：%d条。详细信息已导出：%s",
                        successDataList.size(),
                        allDataList.size() - successDataList.size(),
                        errorFileName));
            }

            // 处理正确的数据
            processValidData(successDataList);
            return Result.getSuccess(String.format("数据处理完成，共处理%d条数据", successDataList.size()));
        } catch (IOException e) {
            log.error("importInteractModel error .", e);
            throw new BusinessException(ErrorCode.FAILURE, "Excel上传交互模式异常");
        }
    }

    private String generateErrorReport2(List<InitInteractModelDTO> dataList) {
        // 生成报告文件名
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        String fileName = "excel_report_" + timestamp + ".xlsx";

        // 导出数据到Excel
        String filePath = System.getProperty("java.io.tmpdir") + File.separator + fileName;
        EasyExcel.write(filePath, InitInteractModelDTO.class)
                .sheet("数据报告")
                .doWrite(dataList);
        return filePath;
    }

    /**
     * 解析游戏内容Excel表格
     * 支持合并单元格和层级关系解析
     * 
     * @param file Excel文件
     * @return 解析后的层级结构数据
     */
    @PostMapping(value = "importGameContent", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Result<Object> importGameContent(@RequestBody MultipartFile file) {
        try {
            GameContentListener listener = new GameContentListener();

            /**
             * registerReadListener  // 注册监听器，处理合并单元格
             * headRowNumber // 设置标题所在行数（通常是第1行）
             * doRead   // 异步读取，读取完毕后会回调监听器
             */
            EasyExcel.read(file.getInputStream())
                    .registerReadListener(listener)
                    .head(GameContentDTO.class)
                    .sheet()
                    .headRowNumber(1)
                    .doRead();

            // 获取解析后的数据列表
            List<GameContentDTO> dataList = listener.getDataList();

            // 转换为层级结构
            GameContentHierarchyResp hierarchyResp = GameContentParser.parseToHierarchy(dataList);

            return Result.getSuccess(hierarchyResp);
        } catch (IOException e) {
            log.error("importGameContent error .", e);
            throw new BusinessException(ErrorCode.FAILURE, "Excel上传游戏内容异常: " + e.getMessage());
        }
    }

    /**
     * 解析游戏内容Excel表格（返回原始扁平数据）
     * 
     * @param file Excel文件
     * @return 解析后的原始数据列表
     */
    @PostMapping(value = "importGameContentFlat", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Result<List<GameContentDTO>> importGameContentFlat(@RequestBody MultipartFile file) {
        try {
            GameContentListener listener = new GameContentListener();

            EasyExcel.read(file.getInputStream())
                    .registerReadListener(listener)
                    .head(GameContentDTO.class)
                    .sheet()
                    .headRowNumber(1)
                    .doRead();

            List<GameContentDTO> dataList = listener.getDataList();
            return Result.getSuccess(dataList);
        } catch (IOException e) {
            log.error("importGameContentFlat error .", e);
            throw new BusinessException(ErrorCode.FAILURE, "Excel上传游戏内容异常: " + e.getMessage());
        }
    }
}
