package com.halo.utils;

import com.halo.dto.GameContentDTO;
import com.halo.dto.resp.GameContentHierarchyResp;
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
    public static GameContentHierarchyResp parseToHierarchy(List<GameContentDTO> dataList) {
        GameContentHierarchyResp result = new GameContentHierarchyResp();
        
        if (dataList == null || dataList.isEmpty()) {
            return result;
        }

        // 用于存储当前层级的信息
        GameContentHierarchyResp.ProjectInfo currentProject = null;
        GameContentHierarchyResp.GameTemplateInfo currentGameTemplate = null;
        GameContentHierarchyResp.gameNumInfo currentGameNumInfo = null;
        GameContentHierarchyResp.LevelInfo currentLevel = null;
        GameContentHierarchyResp.GameInfo currentGameInfo = null;

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
                currentGameNumInfo = null; // 重置游戏序号
                currentLevel = null; // 重置关卡
                currentGameInfo = null; // 重置游戏信息
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
                    dto.getSeasonLectureOrder()
                );
                currentGameNumInfo = null; // 重置游戏序号
                currentLevel = null; // 重置关卡
                currentGameInfo = null; // 重置游戏信息
            }

            if (currentGameTemplate == null) {
                continue; // 如果没有游戏模版，跳过
            }

            // 处理游戏序号层级
            if (StringUtils.isNotBlank(dto.getGameNumber())) {
                // 查找或创建游戏序号信息
                currentGameNumInfo = findOrCreateGameNumInfo(
                    currentGameTemplate.getGameNumInfos(),
                    dto.getGameNumber()
                );
                currentLevel = null; // 重置关卡
                currentGameInfo = null; // 重置游戏信息
            }

            if (currentGameNumInfo == null) {
                continue; // 如果没有游戏序号，跳过
            }

            // 处理关卡层级
            if (StringUtils.isNotBlank(dto.getLevelQuantity())) {
                // 查找或创建关卡
                currentLevel = findOrCreateLevel(
                    currentGameNumInfo.getLevels(),
                    dto.getLevelQuantity()
                );
                currentGameInfo = null; // 重置游戏信息
            }

            if (currentLevel == null) {
                continue; // 如果没有关卡，跳过
            }

            // 处理游戏信息（关卡下的游戏信息）
            if (StringUtils.isNotBlank(dto.getGameInfo())) {
                // 获取或创建GameInfo
                if (currentLevel.getGameInfo() == null) {
                    GameContentHierarchyResp.GameInfo gameInfo = new GameContentHierarchyResp.GameInfo();
                    gameInfo.setGameName(dto.getGameInfo());
                    currentLevel.setGameInfo(gameInfo);
                }
                currentGameInfo = currentLevel.getGameInfo();
            }

            if (currentGameInfo == null) {
                // 如果还没有GameInfo，创建一个默认的
                GameContentHierarchyResp.GameInfo gameInfo = new GameContentHierarchyResp.GameInfo();
                currentLevel.setGameInfo(gameInfo);
                currentGameInfo = gameInfo;
            }

            // 处理题干层级
            if (StringUtils.isNotBlank(dto.getQuestionStemNumber())) {
                // 解析题干类型和序号
                String[] stemParts = parseTypeAndNumber(dto.getQuestionStemNumber());
                String stemType = stemParts[0];
                String stemNumber = stemParts[1];
                
                // 查找或创建题干
                GameContentHierarchyResp.QuestionStemInfo questionStem = findOrCreateQuestionStem(
                    currentGameInfo.getQuestionStems(),
                    stemType,
                    stemNumber
                );
                
                // 设置题干内容
                if (StringUtils.isNotBlank(dto.getQuestionStemInfo())) {
                    questionStem.setContent(dto.getQuestionStemInfo());
                }
            }

            // 处理选项层级（选项属于GameInfo，不是QuestionStemInfo）
            if (StringUtils.isNotBlank(dto.getOptionNumber())) {
                // 解析选项类型和序号
                String[] optionParts = parseTypeAndNumber(dto.getOptionNumber());
                String optionType = optionParts[0];
                String optionNumber = optionParts[1];
                
                // 创建选项
                GameContentHierarchyResp.OptionInfo option = new GameContentHierarchyResp.OptionInfo();
                option.setType(optionType);
                option.setNumber(optionNumber);
                
                // 设置选项内容
                if (StringUtils.isNotBlank(dto.getOptionInfo())) {
                    option.setContent(dto.getOptionInfo());
                }
                
                // 设置AI提示词
                if (StringUtils.isNotBlank(dto.getAiPrompt())) {
                    option.setAiPrompt(dto.getAiPrompt());
                }
                
                currentGameInfo.getOptions().add(option);
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
            String templateName, String seasonCode) {
        for (GameContentHierarchyResp.GameTemplateInfo template : gameTemplates) {
            if (templateName.equals(template.getTemplateName())
                && equalsIgnoreEmpty(seasonCode, template.getSeasonCode())) {
                return template;
            }
        }
        GameContentHierarchyResp.GameTemplateInfo template = new GameContentHierarchyResp.GameTemplateInfo();
        template.setTemplateName(templateName);
        template.setSeasonCode(seasonCode);
        gameTemplates.add(template);
        return template;
    }

    /**
     * 查找或创建游戏序号信息
     */
    private static GameContentHierarchyResp.gameNumInfo findOrCreateGameNumInfo(
            List<GameContentHierarchyResp.gameNumInfo> gameNumInfos,
            String gameNumber) {
        for (GameContentHierarchyResp.gameNumInfo gameNumInfo : gameNumInfos) {
            if (equalsIgnoreEmpty(gameNumber, gameNumInfo.getGameNumber())) {
                return gameNumInfo;
            }
        }
        GameContentHierarchyResp.gameNumInfo gameNumInfo = new GameContentHierarchyResp.gameNumInfo();
        gameNumInfo.setGameNumber(gameNumber);
        gameNumInfos.add(gameNumInfo);
        return gameNumInfo;
    }

    /**
     * 查找或创建关卡
     */
    private static GameContentHierarchyResp.LevelInfo findOrCreateLevel(
            List<GameContentHierarchyResp.LevelInfo> levels,
            String levelNumber) {
        for (GameContentHierarchyResp.LevelInfo level : levels) {
            if (equalsIgnoreEmpty(levelNumber, level.getLevelNumber())) {
                return level;
            }
        }
        GameContentHierarchyResp.LevelInfo level = new GameContentHierarchyResp.LevelInfo();
        level.setLevelNumber(levelNumber);
        levels.add(level);
        return level;
    }

    /**
     * 查找或创建题干
     */
    private static GameContentHierarchyResp.QuestionStemInfo findOrCreateQuestionStem(
            List<GameContentHierarchyResp.QuestionStemInfo> questionStems,
            String type, String number) {
        for (GameContentHierarchyResp.QuestionStemInfo stem : questionStems) {
            if (equalsIgnoreEmpty(type, stem.getType())
                && equalsIgnoreEmpty(number, stem.getNumber())) {
                return stem;
            }
        }
        GameContentHierarchyResp.QuestionStemInfo stem = new GameContentHierarchyResp.QuestionStemInfo();
        stem.setType(type);
        stem.setNumber(number);
        questionStems.add(stem);
        return stem;
    }

    /**
     * 解析类型和序号
     * 例如：t1 -> ["t", "1"], w2 -> ["w", "2"], tw1 -> ["tw", "1"]
     * 
     * @param typeAndNumber 类型和序号的组合字符串
     * @return [类型, 序号]
     */
    private static String[] parseTypeAndNumber(String typeAndNumber) {
        if (StringUtils.isBlank(typeAndNumber)) {
            return new String[]{"", ""};
        }
        
        String trimmed = typeAndNumber.trim();
        // 尝试提取类型（字母部分）和序号（数字部分）
        // 支持：t, w, y, tw, ty, wy 等类型
        int lastLetterIndex = -1;
        for (int i = trimmed.length() - 1; i >= 0; i--) {
            if (Character.isLetter(trimmed.charAt(i))) {
                lastLetterIndex = i;
                break;
            }
        }
        
        if (lastLetterIndex >= 0) {
            String type = trimmed.substring(0, lastLetterIndex + 1);
            String number = trimmed.substring(lastLetterIndex + 1);
            return new String[]{type, number};
        }
        
        // 如果没有找到字母，全部作为序号
        return new String[]{"", trimmed};
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
