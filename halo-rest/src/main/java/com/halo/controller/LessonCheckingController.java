package com.halo.controller;

import com.alibaba.fastjson2.JSON;
import com.halo.common.Result;
import com.halo.dto.req.LessonCheckingReq;
import com.halo.dto.resp.LessonCheckingApiResp;
import com.halo.dto.resp.LessonRepairApiResp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * 在读班对账和修复接口
 * 
 * @Description:
 * @Author: liuruyi
 * @Date: 2026/1/31 16:59
 */
@RestController
@RequestMapping("/lessonChecking")
@Slf4j
public class LessonCheckingController {

    @Resource
    private RestTemplate restTemplate;

    /**
     * 在读班对账接口地址
     */
    @Value("${lesson.checking.url:http://lesson-checking-inner.speiyou.com/studentinstudyclass/checking/checkData}")
//    @Value("${lesson.checking.url:http://lesson-checking-beta.speiyou.cn/studentinstudyclass/checking/checkData}")
    private String checkingUrl;

    /**
     * 在读班修复接口地址
     */
    @Value("${lesson.repair.url:http://lesson-inner.speiyou.cn/studentInStudyClass/repairData}")
//    @Value("${lesson.repair.url:http://lesson-beta-inner.speiyou.cn/studentInStudyClass/repairData}")
    private String repairUrl;

    /**
     * 在读班对账接口（仅对账，不修复）
     * 用于查看有多少数据需要修复
     * 返回结果包含入参，方便Postman批量处理CSV时查看每条记录
     * 
     * @param request 请求参数
     * @return 对账结果（包含入参）
     */
    @PostMapping("/studentInStudyClass/check")
    public Result<Map<String, Object>> check(@RequestBody LessonCheckingReq request) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            log.info("开始对账（仅对账不修复），请求参数: {}", JSON.toJSONString(request));
            
            // 将入参添加到返回结果中
            result.put("request", request);
            
            // 调用对账接口
            LessonCheckingApiResp checkingResp = callCheckingApi(request);
            
            if (checkingResp == null || checkingResp.getData() == null) {
                result.put("error", "对账接口返回数据异常");
                return Result.getFail("对账接口返回数据异常");
            }
            
            // 将对账结果添加到返回结果中
            result.put("checkingResult", checkingResp);
            
            Boolean needRepair = checkingResp.getData().getNeedRepair();
            Boolean passed = checkingResp.getData().getPassed();
            
            log.info("对账结果 - passed: {}, needRepair: {}", passed, needRepair);
            
            // 如果需要修复，打印日志
            if (Boolean.TRUE.equals(needRepair)) {
                log.warn("【需要修复】对账发现数据需要修复 - studentUid: {}, studentId: {}, classId: {}, registId: {}, passed: {}", 
                        request.getStudentUid(), 
                        request.getStudentId(), 
                        request.getClassId(), 
                        request.getRegistId(), 
                        passed);
            }
            
            return Result.getSuccess(result, "对账完成");
            
        } catch (Exception e) {
            log.error("对账过程发生异常", e);
            result.put("request", request);
            result.put("error", e.getMessage());
            return Result.getFail("对账失败: " + e.getMessage());
        }
    }

    /**
     * 在读班对账和修复接口
     * 先调用对账接口，如果needRepair=true则调用修复接口
     * 
     * @param request 请求参数
     * @return 处理结果
     */
    @PostMapping("/studentInStudyClass/checkAndRepair")
    public Result<Map<String, Object>> checkAndRepair(@RequestBody LessonCheckingReq request) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            log.info("开始对账，请求参数: {}", JSON.toJSONString(request));
            
            // 1. 调用对账接口
            LessonCheckingApiResp checkingResp = callCheckingApi(request);
            result.put("checkingResult", checkingResp);
            
            if (checkingResp == null || checkingResp.getData() == null) {
                return Result.getFail("对账接口返回数据异常");
            }
            
            // 2. 判断是否需要修复
            Boolean needRepair = checkingResp.getData().getNeedRepair();
            log.info("对账结果 - passed: {}, needRepair: {}", 
                    checkingResp.getData().getPassed(), needRepair);
            
            // 3. 如果需要修复，调用修复接口
            if (Boolean.TRUE.equals(needRepair)) {
                log.info("需要修复，开始调用修复接口");
                LessonRepairApiResp repairResult = callRepairApi(request);
                result.put("repairResult", repairResult);
                result.put("repairCalled", true);
                // 如果需要修复，将入参也添加到返回结果中，方便Postman批量处理CSV时查看
                result.put("request", request);
            } else {
                log.info("不需要修复，跳过修复接口");
                result.put("repairCalled", false);
            }
            
            return Result.getSuccess(result, "处理完成");
            
        } catch (Exception e) {
            log.error("对账或修复过程发生异常", e);
            result.put("error", e.getMessage());
            return Result.getFail("处理失败: " + e.getMessage());
        }
    }

    /**
     * 调用对账接口
     * 
     * @param request 请求参数
     * @return 对账接口响应
     */
    private LessonCheckingApiResp callCheckingApi(LessonCheckingReq request) {
        try {
            // 构建请求头
            HttpHeaders headers = new HttpHeaders();
            headers.set("xes-origin", "mplat-trade-lesson");
            headers.set("Content-Type", "application/json");
            
            // 构建请求体
            HttpEntity<LessonCheckingReq> httpEntity = new HttpEntity<>(request, headers);
            
            // 调用对账接口
            ResponseEntity<LessonCheckingApiResp> response = restTemplate.exchange(
                    checkingUrl,
                    HttpMethod.POST,
                    httpEntity,
                    LessonCheckingApiResp.class
            );
            
            log.info("对账接口返回: {}", JSON.toJSONString(response.getBody()));
            return response.getBody();
            
        } catch (Exception e) {
            log.error("调用对账接口失败", e);
            throw new RuntimeException("调用对账接口失败: " + e.getMessage(), e);
        }
    }

    /**
     * 调用修复接口
     * 
     * @param request 请求参数
     * @return 修复接口响应
     */
    private LessonRepairApiResp callRepairApi(LessonCheckingReq request) {
        try {
            // 构建请求头
            HttpHeaders headers = new HttpHeaders();
            headers.set("xes-origin", "mplat-trade-lesson");
            headers.set("Content-Type", "application/json");
            
            // 构建请求体
            HttpEntity<LessonCheckingReq> httpEntity = new HttpEntity<>(request, headers);
            
            // 调用修复接口
            ResponseEntity<LessonRepairApiResp> response = restTemplate.exchange(
                    repairUrl,
                    HttpMethod.POST,
                    httpEntity,
                    LessonRepairApiResp.class
            );
            
            log.info("修复接口返回: {}", JSON.toJSONString(response.getBody()));
            return response.getBody();
            
        } catch (Exception e) {
            log.error("调用修复接口失败", e);
            throw new RuntimeException("调用修复接口失败: " + e.getMessage(), e);
        }
    }
}
