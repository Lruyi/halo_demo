package com.halo.utils;

import com.halo.dto.GameContentDTO;
import com.halo.dto.resp.GameContentHierarchyResp;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Objects;

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
            if (Objects.nonNull(dto.getGameNumber())) {
                // 将字符串转换为Integer
                Integer gameNumber = dto.getGameNumber();
                if (gameNumber != null) {
                    // 查找或创建游戏序号信息
                    currentGameNumInfo = findOrCreateGameNumInfo(
                        currentGameTemplate.getGameNumInfos(),
                        gameNumber
                    );
                    currentLevel = null; // 重置关卡
                    currentGameInfo = null; // 重置游戏信息
                }
            }

            if (currentGameNumInfo == null) {
                continue; // 如果没有游戏序号，跳过
            }

            // 处理关卡层级
            if (Objects.nonNull(dto.getLevelQuantity())) {
                // 将字符串转换为Integer
                Integer levelNumber = dto.getLevelQuantity();
                if (levelNumber != null) {
                    // 查找或创建关卡
                    currentLevel = findOrCreateLevel(
                        currentGameNumInfo.getLevels(),
                        levelNumber
                    );
                    currentGameInfo = null; // 重置游戏信息
                }
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
                Integer stemNumber = parseInteger(stemParts[1]);
                
                if (stemNumber != null) {
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
            }

            // 处理选项层级（选项属于GameInfo，不是QuestionStemInfo）
            if (StringUtils.isNotBlank(dto.getOptionNumber())) {
                // 解析选项类型和序号
                String[] optionParts = parseTypeAndNumber(dto.getOptionNumber());
                String optionType = optionParts[0];
                Integer optionNumber = parseInteger(optionParts[1]);
                
                if (optionNumber != null) {
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
            && Objects.isNull(dto.getLevelQuantity())
            && Objects.isNull(dto.getGameNumber())
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
            Integer gameNumber) {
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
            Integer levelNumber) {
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
            String type, Integer number) {
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
        int length = trimmed.length();
        
        // 从前往后查找第一个数字的位置
        int firstDigitIndex = -1;
        for (int i = 0; i < length; i++) {
            if (Character.isDigit(trimmed.charAt(i))) {
                firstDigitIndex = i;
                break;
            }
        }
        
        // 如果找到数字，分割类型和序号
        if (firstDigitIndex > 0) {
            return new String[]{
                trimmed.substring(0, firstDigitIndex),  // 类型（字母部分）
                trimmed.substring(firstDigitIndex)      // 序号（数字部分）
            };
        }
        
        // 如果没有数字，检查是否全是字母（作为类型）或全是数字（作为序号）
        if (firstDigitIndex == 0) {
            // 第一个字符就是数字，全部作为序号
            return new String[]{"", trimmed};
        } else {
            // 没有数字，全部作为类型（虽然这种情况不太常见）
            return new String[]{trimmed, ""};
        }
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

    /**
     * 将字符串转换为Integer
     * 
     * @param str 字符串
     * @return Integer值，如果转换失败返回null
     */
    private static Integer parseInteger(String str) {
        if (StringUtils.isBlank(str)) {
            return null;
        }
        try {
            return Integer.parseInt(str.trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    /**
     * 比较两个数字（忽略空值）
     */
    private static boolean equalsIgnoreEmpty(Integer num1, Integer num2) {
        if (Objects.isNull(num1) && Objects.isNull(num2)) {
            return true;
        }
        if (Objects.isNull(num1) || Objects.isNull(num2)) {
            return false;
        }
        return Objects.equals(num1, num2);
    }
}
