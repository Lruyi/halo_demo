package com.halo.config;

import jakarta.annotation.Resource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author:
 * @date: 2026/04/17 18:43
 * @description:
 */

@Configuration
public class ThreadPoolConfig {

    @Resource
    private FfmpegProperties ffmpegProperties;

    /**
     * FFmpeg 任务执行线程池
     * 核心线程数 = maxConcurrency，不使用线程池队列（由 TaskQueueManager 管理）
     */
    @Bean("ffmpegExecutor")
    public ExecutorService ffmpegExecutor() {
        int poolSize = ffmpegProperties.getMaxConcurrency();
        return new ThreadPoolExecutor(
                poolSize,
                poolSize,
                60L, TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(1),
                r -> {
                    Thread t = new Thread(r, "ffmpeg-executor");
                    t.setDaemon(true);
                    return t;
                },
                new ThreadPoolExecutor.CallerRunsPolicy()
        );
    }
}
