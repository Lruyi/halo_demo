package com.halo.service;

import com.halo.dto.CallbackRequestDTO;
import com.halo.enums.CallbackStatusEnum;
import com.halo.exception.CallbackException;
import com.halo.mapper.FfmpegTaskMapper;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

/**
 * @author:
 * @date: 2026/04/17 18:43
 * @description: 回调服务。使用 RestClient + @Retryable 实现指数退避重试（初始延迟 5s，multiplier=3，最多尝试 5 次，含首次调用）。
 */
@Slf4j
@Service
public class CallbackService {

    @Resource
    private RestClient callbackRestClient;

    @Resource
    private FfmpegTaskMapper taskMapper;

    /**
     * 执行回调（入口方法）
     */
    public void executeCallback(Long taskId, String callbackUrl, CallbackRequestDTO body) {
        try {
            doCallbackWithRetry(callbackUrl, body);
            taskMapper.updateCallbackStatus(taskId, CallbackStatusEnum.SUCCESS.getCode());
            log.info("[回调] taskId={}, 回调成功", taskId);
        } catch (Exception e) {
            log.error("[回调] taskId={}, 重试耗尽仍失败, url={}, error={}", taskId, callbackUrl, e.getMessage());
            taskMapper.updateCallbackStatus(taskId, CallbackStatusEnum.FAILED.getCode());
            // TODO: 接入告警通知
        }
    }

    /**
     * 带重试的 HTTP POST 回调
     * <p>
     * 重试策略：最多尝试 5 次（含首次调用），重试间隔依次为 5s → 15s → 45s → 135s
     */
    @Retryable(
            retryFor = CallbackException.class,
            maxAttempts = 5,
            backoff = @Backoff(delay = 5000, multiplier = 3, maxDelay = 900_000)
    )
    public void doCallbackWithRetry(String callbackUrl, CallbackRequestDTO body) {
        log.info("[回调] 发送回调请求, url={}, taskId={}", callbackUrl, body.getTaskId());
        try {
            ResponseEntity<Void> resp = callbackRestClient.post()
                    .uri(callbackUrl)
                    .body(body)
                    .retrieve()
                    .toBodilessEntity();

            if (!resp.getStatusCode().is2xxSuccessful()) {
                throw new CallbackException("回调返回非 2xx: HTTP " + resp.getStatusCode());
            }
            log.info("[回调] taskId={}, 回调响应状态: {}", body.getTaskId(), resp.getStatusCode());
        } catch (RestClientException e) {
            log.warn("[回调] taskId={}, 请求失败, 将重试: {}", body.getTaskId(), e.getMessage());
            throw new CallbackException(e.getMessage(), e);
        }
    }

    @Recover
    public void onCallbackExhausted(CallbackException e, String callbackUrl, CallbackRequestDTO body) {
        // 重新抛出，由 executeCallback 的 catch 统一处理
        throw e;
    }
}
