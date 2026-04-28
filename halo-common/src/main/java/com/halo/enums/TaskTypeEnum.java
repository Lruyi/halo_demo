package com.halo.enums;

//import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.Getter;

/**
 * @author: wangweichang@tal.com
 * @date: 2026/04/17 18:43
 * @description:
 */

@Getter
public enum TaskTypeEnum {

    VIDEO_OVERLAY("VIDEO_OVERLAY", "视频叠加"),
    VIDEO_CONCAT("VIDEO_CONCAT", "视频拼接"),
    VIDEO_CONCAT_IMAGE_OVERLAY("VIDEO_CONCAT_IMAGE_OVERLAY", "视频拼接并叠加图片"),
    VIDEO_TRIM("VIDEO_TRIM", "视频裁剪"),
    VIDEO_SPEED_ADJUST("VIDEO_SPEED_ADJUST", "视频调速"),
    MERGE_AUDIO_VIDEO("MERGE_AUDIO_VIDEO", "音视频合并"),
    BURN_SUBTITLES("BURN_SUBTITLES", "字幕烧录"),
    EXTRACT_FRAME("EXTRACT_FRAME", "提取视频帧"),
    AUDIO_CONCAT("AUDIO_CONCAT", "音频拼接"),
    PAGE_COMPOSE("PAGE_COMPOSE", "页面合成"),
    VIDEO_SEGMENT_REPLACE("VIDEO_SEGMENT_REPLACE", "视频片段替换"),
    ;
// TODO
//    @EnumValue
    private final String code;
    private final String desc;

    TaskTypeEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static TaskTypeEnum getByCode(String code) {
        for (TaskTypeEnum type : values()) {
            if (type.code.equals(code)) {
                return type;
            }
        }
        return null;
    }
}
