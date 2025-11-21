package com.halo.dto.resp;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 游戏内容层级结构响应DTO
 * 
 * @author halo_ry
 */
@Data
public class GameContentHierarchyResp {

    /**
     * 项目列表
     */
    private List<ProjectInfo> projects = new ArrayList<>();

    /**
     * 项目信息
     */
    @Data
    public static class ProjectInfo {
        /**
         * 项目名称
         */
        private String projectName;
        
        /**
         * 游戏模版列表
         */
        private List<GameTemplateInfo> gameTemplates = new ArrayList<>();
    }

    /**
     * 游戏模版信息
     */
    @Data
    public static class GameTemplateInfo {
        /**
         * 游戏模版名称
         */
        private String templateName;
        
        /**
         * 季节讲次
         */
        private String seasonCode;
        
        private List<gameNumInfo> gameNumInfos = new ArrayList<>();
    }

    /**
     * 游戏序号信息
     */
    @Data
    public static class gameNumInfo {
        /**
         * 游戏序号
         */
        private String gameNumber;

        /**
         * 关卡列表
         */
        private List<LevelInfo> levels = new ArrayList<>();
    }

    /**
     * 关卡信息
     */
    @Data
    public static class LevelInfo {
        /**
         * 关卡数量（关卡编号）
         */
        private String levelNumber;
        
        /**
         * 游戏信息
         */
        private GameInfo gameInfo;
    }

    /**
     * 游戏信息
     */
    @Data
    public static class GameInfo {
        /**
         * 游戏名称
         */
        private String gameName;

        /**
         * 题干列表
         */
        private List<QuestionStemInfo> questionStems = new ArrayList<>();

        /**
         * 选项列表
         */
        private List<OptionInfo> options = new ArrayList<>();
    }

    /**
     * 题干信息
     */
    @Data
    public static class QuestionStemInfo {

        /**
         * 题干类型：t、w、y、tw、ty、wy
         */
        private String type;

        /**
         * 题干序号
         */
        private String number;
        
        /**
         * 题干信息
         */
        private Content content;
    }

    /**
     * 选项信息
     */
    @Data
    public static class OptionInfo {

        /**
         * 选项类型：t、w、tw
         */
        private String type;

        /**
         * 选项序号
         */
        private String number;
        
        /**
         * 选项信息
         */
        private Content content;
        
        /**
         * AI提示词
         */
        private String aiPrompt;
    }

    /**
     * 内容信息
     */
    @Data
    public static class Content {

        /**
         * 文本
         */
        private String text;

        /**
         * 图片
         */
        private String image;

        /**
         * 音频
         */
        private String audio;
    }
}

