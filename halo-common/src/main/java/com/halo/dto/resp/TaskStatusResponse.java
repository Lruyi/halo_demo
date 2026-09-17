package com.halo.dto.resp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskStatusResponse {

    private Long taskId;

    private String bizTaskId;

    private String taskType;

    private String status;

    private String resultUrl;

    private String failReason;

    private Integer retryCount;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private LocalDateTime createTime;
}
