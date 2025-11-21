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
        private String seasonLectureOrder;
        
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
        private String levelQuantity;
        
        /**
         * 游戏信息
         */
        private String gameInfo;
        
        /**
         * 题干列表
         */
        private List<QuestionStemInfo> questionStems = new ArrayList<>();
    }

    /**
     * 题干信息
     */
    @Data
    public static class QuestionStemInfo {
        /**
         * 题干序号
         */
        private String questionStemNumber;
        
        /**
         * 题干信息
         */
        private String questionStemInfo;
        
        /**
         * 选项列表
         */
        private List<OptionInfo> options = new ArrayList<>();
    }

    /**
     * 选项信息
     */
    @Data
    public static class OptionInfo {
        /**
         * 选项序号
         */
        private String optionNumber;
        
        /**
         * 选项信息
         */
        private String optionInfo;
        
        /**
         * AI提示词
         */
        private String aiPrompt;
    }
}

