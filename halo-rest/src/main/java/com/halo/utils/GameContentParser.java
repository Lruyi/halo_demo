package com.halo.utils;

import com.halo.dto.GameContentDTO;
import com.halo.dto.resp.GameContentHierarchyV2Resp;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * 游戏内容解析工具类
 * 将扁平化的Excel数据转换为层级结构
 * 
 * @author halo_ry
 */
public class GameContentParser {

    /**
     * 将扁平化的GameContentDTO列表转换为层级结构
     * 
     * @param dataList Excel解析后的数据列表
     * @return 层级结构数据
     */
    public static GameContentHierarchyV2Resp parseToHierarchy(List<GameContentDTO> dataList) {
        GameContentHierarchyResp result = new GameContentHierarchyResp();
        
        if (dataList == null || dataList.isEmpty()) {
            return result;
        }

        // 用于存储当前层级的信息
        GameContentHierarchyResp.ProjectInfo currentProject = null;
        GameContentHierarchyResp.GameTemplateInfo currentGameTemplate = null;
        GameContentHierarchyResp.LevelInfo currentLevel = null;
        GameContentHierarchyResp.QuestionStemInfo currentQuestionStem = null;

        for (GameContentDTO dto : dataList) {
            // 跳过完全空白的行
            if (isBlankRow(dto)) {
                continue;
            }

            // 处理项目层级
            if (StringUtils.isNotBlank(dto.getProject())) {
                // 查找或创建项目
                currentProject = findOrCreateProject(result.getProjects(), dto.getProject());
                currentGameTemplate = null; // 重置游戏模版
                currentLevel = null; // 重置关卡
                currentQuestionStem = null; // 重置题干
            }

            if (currentProject == null) {
                continue; // 如果没有项目，跳过
            }

            // 处理游戏模版层级
            if (StringUtils.isNotBlank(dto.getGameTemplate())) {
                // 查找或创建游戏模版
                currentGameTemplate = findOrCreateGameTemplate(
                    currentProject.getGameTemplates(),
                    dto.getGameTemplate(),
                    dto.getSeasonLectureOrder(),
                    dto.getGameNumber()
                );
                currentLevel = null; // 重置关卡
                currentQuestionStem = null; // 重置题干
            }

            if (currentGameTemplate == null) {
                continue; // 如果没有游戏模版，跳过
            }

            // 处理关卡层级
            if (StringUtils.isNotBlank(dto.getLevelQuantity())) {
                // 查找或创建关卡
                currentLevel = findOrCreateLevel(
                    currentGameTemplate.getLevels(),
                    dto.getLevelQuantity(),
                    dto.getGameInfo()
                );
                currentQuestionStem = null; // 重置题干
            }

            if (currentLevel == null) {
                continue; // 如果没有关卡，跳过
            }

            // 处理题干层级
            if (StringUtils.isNotBlank(dto.getQuestionStemNumber())) {
                // 查找或创建题干
                currentQuestionStem = findOrCreateQuestionStem(
                    currentLevel.getQuestionStems(),
                    dto.getQuestionStemNumber(),
                    dto.getQuestionStemInfo()
                );
            }

            if (currentQuestionStem == null) {
                continue; // 如果没有题干，跳过
            }

            // 处理选项（每个选项都添加到当前题干下）
            if (StringUtils.isNotBlank(dto.getOptionNumber())) {
                GameContentHierarchyResp.OptionInfo option = new GameContentHierarchyResp.OptionInfo();
                option.setOptionNumber(dto.getOptionNumber());
                option.setOptionInfo(dto.getOptionInfo());
                option.setAiPrompt(dto.getAiPrompt());
                currentQuestionStem.getOptions().add(option);
            }
        }

        return result;
    }

    /**
     * 判断是否为空白行
     */
    private static boolean isBlankRow(GameContentDTO dto) {
        return StringUtils.isBlank(dto.getProject())
            && StringUtils.isBlank(dto.getGameTemplate())
            && StringUtils.isBlank(dto.getSeasonLectureOrder())
            && StringUtils.isBlank(dto.getGameNumber())
            && StringUtils.isBlank(dto.getLevelQuantity())
            && StringUtils.isBlank(dto.getGameInfo())
            && StringUtils.isBlank(dto.getQuestionStemNumber())
            && StringUtils.isBlank(dto.getQuestionStemInfo())
            && StringUtils.isBlank(dto.getOptionNumber())
            && StringUtils.isBlank(dto.getOptionInfo())
            && StringUtils.isBlank(dto.getAiPrompt());
    }

    /**
     * 查找或创建项目
     */
    private static GameContentHierarchyResp.ProjectInfo findOrCreateProject(
            List<GameContentHierarchyResp.ProjectInfo> projects, String projectName) {
        for (GameContentHierarchyResp.ProjectInfo project : projects) {
            if (projectName.equals(project.getProjectName())) {
                return project;
            }
        }
        GameContentHierarchyResp.ProjectInfo project = new GameContentHierarchyResp.ProjectInfo();
        project.setProjectName(projectName);
        projects.add(project);
        return project;
    }

    /**
     * 查找或创建游戏模版
     */
    private static GameContentHierarchyResp.GameTemplateInfo findOrCreateGameTemplate(
            List<GameContentHierarchyResp.GameTemplateInfo> gameTemplates,
            String templateName, String seasonLectureOrder, String gameNumber) {
        for (GameContentHierarchyResp.GameTemplateInfo template : gameTemplates) {
            if (templateName.equals(template.getTemplateName())
                && equalsIgnoreEmpty(seasonLectureOrder, template.getSeasonLectureOrder())
                && equalsIgnoreEmpty(gameNumber, template.getGameNumber())) {
                return template;
            }
        }
        GameContentHierarchyResp.GameTemplateInfo template = new GameContentHierarchyResp.GameTemplateInfo();
        template.setTemplateName(templateName);
        template.setSeasonLectureOrder(seasonLectureOrder);
        template.setGameNumber(gameNumber);
        gameTemplates.add(template);
        return template;
    }

    /**
     * 查找或创建关卡
     */
    private static GameContentHierarchyResp.LevelInfo findOrCreateLevel(
            List<GameContentHierarchyResp.LevelInfo> levels,
            String levelQuantity, String gameInfo) {
        for (GameContentHierarchyResp.LevelInfo level : levels) {
            if (equalsIgnoreEmpty(levelQuantity, level.getLevelQuantity())
                && equalsIgnoreEmpty(gameInfo, level.getGameInfo())) {
                return level;
            }
        }
        GameContentHierarchyResp.LevelInfo level = new GameContentHierarchyResp.LevelInfo();
        level.setLevelQuantity(levelQuantity);
        level.setGameInfo(gameInfo);
        levels.add(level);
        return level;
    }

    /**
     * 查找或创建题干
     */
    private static GameContentHierarchyResp.QuestionStemInfo findOrCreateQuestionStem(
            List<GameContentHierarchyResp.QuestionStemInfo> questionStems,
            String questionStemNumber, String questionStemInfo) {
        for (GameContentHierarchyResp.QuestionStemInfo stem : questionStems) {
            if (equalsIgnoreEmpty(questionStemNumber, stem.getQuestionStemNumber())) {
                return stem;
            }
        }
        GameContentHierarchyResp.QuestionStemInfo stem = new GameContentHierarchyResp.QuestionStemInfo();
        stem.setQuestionStemNumber(questionStemNumber);
        stem.setQuestionStemInfo(questionStemInfo);
        questionStems.add(stem);
        return stem;
    }

    /**
     * 比较两个字符串（忽略空值）
     */
    private static boolean equalsIgnoreEmpty(String str1, String str2) {
        if (StringUtils.isBlank(str1) && StringUtils.isBlank(str2)) {
            return true;
        }
        if (StringUtils.isBlank(str1) || StringUtils.isBlank(str2)) {
            return false;
        }
        return str1.equals(str2);
    }
}

