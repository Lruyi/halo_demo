package com.halo.dto;

import lombok.Data;

/**
 * @author: wangweichang@tal.com
 * @date: 2026/04/17 18:43
 * @description:
 */

@Data
public class TaskOptions {

    /** 裁剪起始时间（ms），VIDEO_TRIM 使用 */
    private Integer startMs;

    /** 裁剪结束时间（ms），VIDEO_TRIM 使用 */
    private Integer endMs;

    /** 目标时长（ms），VIDEO_SPEED_ADJUST 使用 */
    private Integer targetDurationMs;

    /** 提取的帧序号（从 1 开始），EXTRACT_FRAME 使用 */
    private Integer frameNumber;

    /** 图片缩放边长上限（px），VIDEO_CONCAT_IMAGE_OVERLAY 使用；不传则使用全局默认值 */
    private Integer imageMaxSize;

    /**
     * 是否使用 GPU 硬件编码（仅对需重编码 H.264 的任务生效）。null 表示遵循全局 ffmpeg.gpu-accel-enabled；
     * false 强制软编；true 在全局允许时使用 ffmpeg.video-hw-encoder。
     */
    private Boolean useGpuAccel;

    /** 转场效果名称（xfade filter 名，如 fade、fadeblack、wipeleft、dissolve、circleopen 等），VIDEO_TRANSITION 使用 */
    private String transitionType;

    /** 每段转场时长（ms），VIDEO_TRANSITION 使用 */
    private Integer transitionDurationMs;
}
