package com.halo.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * @author:
 * @date: 2026/04/17 18:43
 * @description:
 */

@Data
public class TaskOutput {

    /** COS 输出路径前缀或期望路径，服务端会结合 taskId/时间戳生成最终对象 key */
    @NotBlank(message = "output.path 不能为空")
    private String path;

    /** 输出格式，严格按入参生成，如 mp4 / jpg */
    @NotBlank(message = "output.format 不能为空")
    private String format;
}
