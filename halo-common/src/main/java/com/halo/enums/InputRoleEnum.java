package com.halo.enums;

import java.util.Locale;
import java.util.Map;

/**
 * @author:
 * @date: 2026/04/21 10:05
 * @description: 任务输入项角色，与 {@link com.xes.mplat.ffmpeg.dto.TaskInput#role} 及 params JSON 中 inputs[].role 对应。
 */
public enum InputRoleEnum {

    /** 底层主视频（如 VIDEO_OVERLAY 底轨） */
    BASE_VIDEO,

    /** 叠加层视频 */
    OVERLAY_VIDEO,

    /** 普通视频输入（拼接、调速等） */
    VIDEO,

    /** 图片输入（静态图叠加、水印等） */
    IMAGE,

    /** 音频输入 */
    AUDIO,

    /** 字幕文件 */
    SUBTITLE,

    /** 字体目录（特殊处理，下载至 fonts） */
    FONT,

    /** 转场视频（MOV），VIDEO_TRANSITION MOV 模式使用，每次任务仅允许一个 */
    TRANSITION_VIDEO;

    /**
     * 从 params 中的原始值解析（兼容 DB 中存 String、以及运行期 Map 反序列化结果）。
     */
    public static InputRoleEnum fromObject(Object role) {
        if (role == null) {
            return null;
        }
        if (role instanceof InputRoleEnum r) {
            return r;
        }
        if (role instanceof String s) {
            String t = s.trim();
            if (t.isEmpty()) {
                return null;
            }
            try {
                return InputRoleEnum.valueOf(t);
            } catch (IllegalArgumentException ignored) {
                return null;
            }
        }
        return null;
    }

    /**
     * 从一行 input Map 读取 role。
     */
    public static InputRoleEnum fromRow(Map<String, ?> row) {
        return fromObject(row != null ? row.get("role") : null);
    }

    /**
     * 无法匹配已知枚举时，用于与旧版「自定义 role 字符串」逻辑兼容：小写 + "_0"。
     */
    public static String legacyKeySuffix(Object role) {
        String raw = role != null ? role.toString().trim() : "";
        if (raw.isEmpty()) {
            return null;
        }
        return raw.toLowerCase(Locale.ROOT) + "_0";
    }
}
