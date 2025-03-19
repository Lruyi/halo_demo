package com.halo.controller;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.halo.common.Result;
import com.halo.constant.ErrorCode;
import com.halo.dto.req.InitInteractModelBody;
import com.halo.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Description:
 * @Author: halo_ry
 * @Date: 2025/3/17 16:31
 */
@Slf4j
@RestController
@RequestMapping("/initData")
public class processCSVController {

    /**
     * 处理大文件
     * @return
     */
    @GetMapping("/processLargeCSV")
    public Result<Object> processLargeCSV() {
        // 路径
//        String filePath = "/Users/liuruyi/Downloads/交互模式初始化-班级_副本.csv";
        String filePath = "/Users/liuruyi/Downloads/班级.csv";

        ExecutorService executor = Executors.newFixedThreadPool(1);
        CompletableFuture.runAsync(() -> {
            try (CSVReader csvReader = new CSVReader(new FileReader(filePath))) {
                String[] values;
                csvReader.readNext(); // 如果CSV包含表头，跳过第一行
                while ((values = csvReader.readNext()) != null) {
                    String classId = values[0];
                    String productId = values[1];
                    String interactModel = values[2];

                    // 创建请求
                    // 预发布环境
//                    String postUrl = "http://lesson-release.speiyou.cn/initData/initSendMessage";
                    // 仿真环境
                    String postUrl = "http://lesson-beta-inner.speiyou.cn/initData/initSendMessage";
                    InitInteractModelBody body = new InitInteractModelBody();
                    body.setClassId(classId);
                    body.setProductId(productId);
                    body.setInteractModel(Integer.parseInt(interactModel));

                    // 发送请求
                    try {
                        String response = HttpUtil.post(postUrl, JSONUtil.toJsonStr(body));
                        log.info("response: {}", response);
                    } catch (Exception e) {
                        log.error("initInteractModel remote error . {}", e.getMessage());
                        throw new BusinessException(ErrorCode.REMOTE_CALL_ERROR, "调用接口失败");
                    }
                }
            } catch (IOException | CsvValidationException e) {
                log.error("initInteractModel error . {}", e.getMessage());
                throw new BusinessException(ErrorCode.BUSINESS_ERROR, "初始化互动模式失败");
            }
        }, executor);

        return Result.getSuccess("文件解析成功，后台处理中...");
    }


    public static void main(String[] args) {

        // 路径
//        String filePath = "/Users/liuruyi/Downloads/交互模式初始化-班级_副本.csv";
//        String filePath = "/Users/liuruyi/Downloads/交互模式初始化-班级.csv";
        String filePath = "/Users/liuruyi/Downloads/交互模式初始化-商品.csv";
//        String filePath = "/Users/liuruyi/Downloads/班级.csv";

        try (CSVReader csvReader = new CSVReader(new FileReader(filePath))) {
            String[] values;
            csvReader.readNext(); // 如果CSV包含表头，跳过第一行
            while ((values = csvReader.readNext()) != null) {
                String classId = values[0];
                String productId = values[1];
                String interactModel = values[2];

                // 创建请求  替换接口地址
                // 预发布环境
                    String postUrl = "http://lesson-release.speiyou.cn/initData/initSendMessage";
                // 仿真环境
//                String postUrl = "http://lesson-beta-inner.speiyou.cn/initData/initSendMessage";
                InitInteractModelBody body = new InitInteractModelBody();
                body.setClassId(classId);
                body.setProductId(productId);
                body.setInteractModel(Integer.parseInt(interactModel));
                System.out.println("body:"+ JSONUtil.toJsonStr(body));
                // 发送请求
                try {
                    String response = HttpUtil.post(postUrl, JSONUtil.toJsonStr(body));
                    log.info("response: {}", response);
                } catch (Exception e) {
                    log.error("initInteractModel remote error . {}", e.getMessage());
                    throw new BusinessException(ErrorCode.REMOTE_CALL_ERROR, "调用接口失败");
                }
            }
        } catch (IOException | CsvValidationException e) {
            log.error("initInteractModel error . {}", e.getMessage());
            throw new BusinessException(ErrorCode.BUSINESS_ERROR, "初始化互动模式失败");
        }
    }
}
