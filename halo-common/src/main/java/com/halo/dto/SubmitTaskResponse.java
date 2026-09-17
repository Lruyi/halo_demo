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
public class SubmitTaskResponse {

    private Long taskId;

    private String status;

    /** 预估等待时间（秒） */
    private Integer estimatedWaitSeconds;
}
