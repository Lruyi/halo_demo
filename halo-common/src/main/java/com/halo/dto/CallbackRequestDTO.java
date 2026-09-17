package com.halo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author:
 * @date: 2026/04/17 18:43
 * @description:
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CallbackRequestDTO {

    private Long taskId;

    private String bizTaskId;

    private String taskType;

    /** SUCCESS / FAILED / TIMEOUT */
    private String status;

    private String resultUrl;

    /** 输出文件时长（ms） */
    private Long durationMs;

    private String failReason;
}
