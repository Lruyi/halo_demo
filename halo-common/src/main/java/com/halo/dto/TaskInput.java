package com.halo.dto;

import com.halo.enums.InputRoleEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * @author: wangweichang@tal.com
 * @date: 2026/04/17 18:43
 * @description:
 */

@Data
public class TaskInput {

    /** 输入角色 */
    @NotNull(message = "inputs[].role 不能为空")
    private InputRoleEnum role;

    /** COS 访问地址 */
    @NotBlank(message = "inputs[].url 不能为空")
    private String url;

    /** 顺序索引（如视频拼接类任务需要） */
    private Integer seq;

    /** 起始时间（ms），OVERLAY_VIDEO / TRIM 使用 */
    private Integer startMs;

    /** 结束时间（ms），OVERLAY_VIDEO / TRIM 使用 */
    private Integer endMs;

    /** 叠加 X 坐标，OVERLAY_VIDEO / IMAGE 使用 */
    private Integer posX;

    /** 叠加 Y 坐标，OVERLAY_VIDEO / IMAGE 使用 */
    private Integer posY;
}
