package com.halo.service;

import com.halo.config.FfmpegProperties;
import com.halo.entity.FfmpegTask;
import com.halo.enums.PriorityEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.Objects;
import java.util.concurrent.PriorityBlockingQueue;

/**
 * @author:
 * @date: 2026/04/17 18:43
 * @description: 任务队列管理器。基于 PriorityBlockingQueue 实现，HIGH 优先级任务排在 NORMAL 之前，同优先级按任务 ID 排序。
 */
@Slf4j
@Component
public class TaskQueueManager {

    private final PriorityBlockingQueue<FfmpegTask> queue;
    private final int maxQueueSize;

    public TaskQueueManager(FfmpegProperties ffmpegProperties) {
        this.maxQueueSize = ffmpegProperties.getMaxQueueSize();
        this.queue = new PriorityBlockingQueue<>(maxQueueSize, Comparator
                .comparingInt((FfmpegTask t) -> Objects.equals(t.getPriority(), PriorityEnum.HIGH.getCode()) ? 0 : 1)
                .thenComparingLong(FfmpegTask::getId));
    }

    /**
     * 入队
     *
     * @return true 入队成功，false 队列已满
     */
    public boolean enqueue(FfmpegTask task) {
        if (queue.size() >= maxQueueSize) {
            log.warn("[队列] 队列已满({}/{}), 拒绝任务 taskId={}", queue.size(), maxQueueSize, task.getId());
            return false;
        }
        queue.offer(task);
        log.info("[队列] 任务入队 taskId={}, priority={}, 当前队列大小={}", task.getId(), task.getPriority(), queue.size());
        return true;
    }

    /** 取出队首任务（阻塞） */
    public FfmpegTask take() throws InterruptedException {
        return queue.take();
    }

    /** 非阻塞取出 */
    public FfmpegTask poll() {
        return queue.poll();
    }

    /** 从队列中移除指定任务 */
    public boolean remove(FfmpegTask task) {
        return queue.remove(task);
    }

    /** 根据任务 ID 移除 */
    public boolean removeById(Long taskId) {
        return queue.removeIf(t -> t.getId().equals(taskId));
    }

    public int size() {
        return queue.size();
    }

    public boolean isFull() {
        return queue.size() >= maxQueueSize;
    }

    public int getMaxQueueSize() {
        return maxQueueSize;
    }
}
