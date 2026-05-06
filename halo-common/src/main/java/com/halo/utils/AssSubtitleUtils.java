package com.halo.utils;

import com.halo.utils.ffmpeg.FFmpegCommandRunner;
import com.halo.utils.ffmpeg.FFmpegUtils;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.regex.Pattern;

/**
 * ASS/SSA字幕工具类
 * 提供ASS格式字幕的生成、转换和视频烧录功能
 *
 * @author: wangweichang@tal.com
 * @date: 2025/12/26
 */
@Slf4j
public class AssSubtitleUtils {

    /**
     * 匹配[xxx]格式标签的正则表达式
     */
    private static final Pattern BRACKET_TAG_PATTERN = Pattern.compile("\\[.*?]");

    /**
     * 默认每行最大字符数（用于智能换行）
     */
    public static final int DEFAULT_MAX_CHARS_PER_LINE = 40;

    /**
     * 默认视频宽度（用于ASS字幕）
     */
    public static final int DEFAULT_VIDEO_WIDTH = 1920;

    /**
     * 默认视频高度（用于ASS字幕）
     */
    public static final int DEFAULT_VIDEO_HEIGHT = 1080;

    private AssSubtitleUtils() {
        // 私有构造函数，防止实例化
    }

    /**
     * 字幕条目接口
     * 定义字幕条目需要实现的基本方法
     */
    public interface SubtitleEntry {
        /**
         * 获取开始时间（毫秒）
         */
        Integer getStartTime();

        /**
         * 获取结束时间（毫秒）
         */
        Integer getEndTime();

        /**
         * 获取字幕文本
         */
        String getText();
    }

    /**
     * 默认字体路径（classpath资源）
     */
    private static final String DEFAULT_FONT_PATH = "classpath:fonts/wordsong/rammetto-one.regular.ttf";

    /**
     * 默认字体名称（字体族名称，用于ASS样式）
     */
    private static final String DEFAULT_FONT_NAME = "Rammetto One";

    /**
     * ASS样式配置类
     */
    @Data
    @Builder
    public static class AssStyle {
        /**
         * 样式名称
         */
        @Builder.Default
        private String name = "Default";

        /**
         * 字体名称（字体族名称，用于ASS样式，如 "Arial", "Rammetto One"）
         */
        @Builder.Default
        private String fontName = DEFAULT_FONT_NAME;

        /**
         * 字体文件路径（支持classpath:前缀或绝对路径）
         * 例如: "classpath:fonts/wordsong/rammetto-one.regular.ttf" 或 "/path/to/font.ttf"
         */
        @Builder.Default
        private String fontPath = DEFAULT_FONT_PATH;

        /**
         * 字体大小
         */
        @Builder.Default
        private int fontSize = 88;

        /**
         * 主要颜色（AABBGGRR格式，如 &H00FFFFFF 为白色）
         */
        @Builder.Default
        private String primaryColor = "&H00FFFFFF";

        /**
         * 次要颜色
         */
        @Builder.Default
        private String secondaryColor = "&H00FFFFFF";

        /**
         * 描边颜色
         */
        @Builder.Default
        private String outlineColor = "&H00000000";

        /**
         * 背景颜色
         */
        @Builder.Default
        private String backColor = "&H80000000";

        /**
         * 是否粗体
         */
        @Builder.Default
        private boolean bold = false;

        /**
         * 描边宽度
         */
        @Builder.Default
        private int outline = 4;

        /**
         * 阴影大小
         */
        @Builder.Default
        private int shadow = 0;

        /**
         * 对齐方式（数字小键盘位置：1-9，2=底部居中）
         */
        @Builder.Default
        private int alignment = 2;

        /**
         * 底部边距
         */
        @Builder.Default
        private int marginV = 80;

        /**
         * 左边距
         */
        @Builder.Default
        private int marginL = 20;

        /**
         * 右边距
         */
        @Builder.Default
        private int marginR = 20;

        /**
         * 获取默认样式
         */
        public static AssStyle defaultStyle() {
            return AssStyle.builder().build();
        }

        /**
         * 转换为ASS样式行格式
         */
        public String toStyleLine() {
            return String.format(
                    "Style: %s,%s,%d,%s,%s,%s,%s,%d,0,0,0,100,100,0,0,1,%d,%d,%d,%d,%d,%d,1",
                    name, fontName, fontSize,
                    primaryColor, secondaryColor, outlineColor, backColor,
                    bold ? 1 : 0,
                    outline, shadow, alignment,
                    marginL, marginR, marginV
            );
        }

        /**
         * 判断字体路径是否为classpath资源
         */
        public boolean isClasspathFont() {
            return StringUtils.isNotBlank(fontPath) && fontPath.startsWith("classpath:");
        }

        /**
         * 获取classpath资源路径（去除classpath:前缀）
         */
        public String getClasspathResourcePath() {
            if (isClasspathFont()) {
                return fontPath.substring("classpath:".length());
            }
            return fontPath;
        }
    }

    /**
     * 将字幕条目列表转换为ASS格式字符串
     *
     * @param entries 字幕条目列表
     * @param <T>     实现SubtitleEntry接口的类型
     * @return ASS格式的字幕字符串
     */
    public static <T extends SubtitleEntry> String convertToAss(List<T> entries) {
        return convertToAss(entries, DEFAULT_VIDEO_WIDTH, DEFAULT_VIDEO_HEIGHT,
                DEFAULT_MAX_CHARS_PER_LINE, AssStyle.defaultStyle(), true);
    }

    /**
     * 将字幕条目列表转换为ASS格式字符串
     *
     * @param entries         字幕条目列表
     * @param videoWidth      视频宽度
     * @param videoHeight     视频高度
     * @param maxCharsPerLine 每行最大字符数
     * @param <T>             实现SubtitleEntry接口的类型
     * @return ASS格式的字幕字符串
     */
    public static <T extends SubtitleEntry> String convertToAss(List<T> entries,
                                                                 int videoWidth, int videoHeight, int maxCharsPerLine) {
        return convertToAss(entries, videoWidth, videoHeight, maxCharsPerLine,
                AssStyle.defaultStyle(), true);
    }

    /**
     * 将字幕条目列表转换为ASS格式字符串（完整参数版本）
     *
     * @param entries           字幕条目列表
     * @param videoWidth        视频宽度
     * @param videoHeight       视频高度
     * @param maxCharsPerLine   每行最大字符数
     * @param style             ASS样式配置
     * @param filterBracketTags 是否过滤[xxx]格式标签
     * @param <T>               实现SubtitleEntry接口的类型
     * @return ASS格式的字幕字符串
     */
    public static <T extends SubtitleEntry> String convertToAss(List<T> entries,
                                                                 int videoWidth, int videoHeight,
                                                                 int maxCharsPerLine,
                                                                 AssStyle style,
                                                                 boolean filterBracketTags) {
        if (CollectionUtils.isEmpty(entries)) {
            return "";
        }

        StringBuilder ass = new StringBuilder();

        // Script Info部分
        appendScriptInfo(ass, videoWidth, videoHeight);

        // V4+ Styles部分
        appendStyles(ass, style);

        // Events部分
        appendEvents(ass, entries, maxCharsPerLine, filterBracketTags);

        return ass.toString();
    }

    /**
     * 添加Script Info部分
     */
    private static void appendScriptInfo(StringBuilder ass, int videoWidth, int videoHeight) {
        ass.append("[Script Info]\n");
        ass.append("Title: Generated ASS Subtitle\n");
        ass.append("ScriptType: v4.00+\n");
        ass.append("Collisions: Normal\n");
        ass.append("PlayDepth: 0\n");
        ass.append(String.format("PlayResX: %d\n", videoWidth));
        ass.append(String.format("PlayResY: %d\n", videoHeight));
        ass.append("\n");
    }

    /**
     * 添加V4+ Styles部分
     */
    private static void appendStyles(StringBuilder ass, AssStyle style) {
        ass.append("[V4+ Styles]\n");
        ass.append("Format: Name, Fontname, Fontsize, PrimaryColour, SecondaryColour, OutlineColour, BackColour, Bold, Italic, Underline, StrikeOut, ScaleX, ScaleY, Spacing, Angle, BorderStyle, Outline, Shadow, Alignment, MarginL, MarginR, MarginV, Encoding\n");
        ass.append(style.toStyleLine()).append("\n");
        ass.append("\n");
    }

    /**
     * 添加Events部分
     */
    private static <T extends SubtitleEntry> void appendEvents(StringBuilder ass, List<T> entries,
                                                                int maxCharsPerLine, boolean filterBracketTags) {
        ass.append("[Events]\n");
        ass.append("Format: Layer, Start, End, Style, Name, MarginL, MarginR, MarginV, Effect, Text\n");

        for (T entry : entries) {
            if (entry == null || entry.getStartTime() == null || entry.getEndTime() == null) {
                continue;
            }

            String text = entry.getText();
            if (StringUtils.isBlank(text)) {
                continue;
            }

            // 可选过滤[xxx]格式标签
            if (filterBracketTags && containsBracketTag(text)) {
                continue;
            }

            String startTime = formatAssTime(entry.getStartTime());
            String endTime = formatAssTime(entry.getEndTime());

            // 智能换行处理
            String wrappedText = smartWrapText(text.trim(), maxCharsPerLine);

            // 转义ASS格式中的特殊字符
            String escapedText = escapeAssText(wrappedText);

            ass.append(String.format("Dialogue: 0,%s,%s,Default,,0,0,0,,%s\n",
                    startTime, endTime, escapedText));
        }
    }

    /**
     * 将字幕烧录到视频中
     *
     * @param videoFile  原始视频文件
     * @param entries    字幕条目列表
     * @param outputFile 输出视频文件
     * @param <T>        实现SubtitleEntry接口的类型
     * @return 烧录字幕后的视频文件，失败返回null
     */
    public static <T extends SubtitleEntry> File burnSubtitlesToVideo(File videoFile,
                                                                       List<T> entries,
                                                                       File outputFile) {
        return burnSubtitlesToVideo(videoFile, entries, outputFile,
                DEFAULT_VIDEO_WIDTH, DEFAULT_VIDEO_HEIGHT, DEFAULT_MAX_CHARS_PER_LINE,
                AssStyle.defaultStyle());
    }

    /**
     * 将字幕烧录到视频中（支持自定义参数）
     *
     * @param videoFile       原始视频文件
     * @param entries         字幕条目列表
     * @param outputFile      输出视频文件
     * @param videoWidth      视频宽度
     * @param videoHeight     视频高度
     * @param maxCharsPerLine 每行最大字符数
     * @param style           ASS样式配置
     * @param <T>             实现SubtitleEntry接口的类型
     * @return 烧录字幕后的视频文件，失败返回null
     */
    public static <T extends SubtitleEntry> File burnSubtitlesToVideo(File videoFile,
                                                                       List<T> entries,
                                                                       File outputFile,
                                                                       int videoWidth,
                                                                       int videoHeight,
                                                                       int maxCharsPerLine,
                                                                       AssStyle style) {
        if (videoFile == null || !videoFile.exists()) {
            log.error("视频文件不存在，无法烧录字幕");
            return null;
        }
        if (CollectionUtils.isEmpty(entries)) {
            log.warn("字幕列表为空，跳过字幕烧录，直接返回原视频");
            return videoFile;
        }
        if (outputFile == null) {
            log.error("输出文件不能为空");
            return null;
        }

        // 过滤掉包含[xxx]标签的条目
        List<T> filteredList = entries.stream()
                .filter(e -> e != null && StringUtils.isNotBlank(e.getText()))
                .filter(e -> !containsBracketTag(e.getText()))
                .toList();

        if (filteredList.isEmpty()) {
            log.warn("过滤后字幕列表为空，跳过字幕烧录，直接返回原视频");
            return videoFile;
        }

        File assFile = null;
        File fontTempDir = null;
        try {
            // 生成ASS字幕内容
            String assContent = convertToAss(filteredList, videoWidth, videoHeight, maxCharsPerLine, style, false);
            if (StringUtils.isBlank(assContent)) {
                log.warn("生成的ASS字幕内容为空，跳过字幕烧录");
                return videoFile;
            }

            // 创建临时目录
            File tmpDir = MediaFileUtil.createDateTempDir();
            assFile = File.createTempFile("subtitle_", ".ass", tmpDir);
            Files.writeString(assFile.toPath(), assContent, StandardCharsets.UTF_8);
            log.info("ASS字幕文件已生成: {}", assFile.getAbsolutePath());

            // 提取字体文件（如果是classpath资源）
            fontTempDir = extractFontToTempDir(style, tmpDir);

            // 使用FFmpeg烧录字幕
            List<String> commands;
            if (fontTempDir != null) {
                commands = FFmpegUtils.addSubtitlesToVideo(videoFile, assFile, outputFile, fontTempDir);
                log.info("使用自定义字体目录: {}", fontTempDir.getAbsolutePath());
            } else {
                commands = FFmpegUtils.addSubtitlesToVideo(videoFile, assFile, outputFile);
            }
            String result = FFmpegCommandRunner.runProcess(commands);

            if (result == null || !outputFile.exists() || outputFile.length() == 0) {
                log.error("FFmpeg烧录字幕失败，videoFile: {}, assFile: {}",
                        videoFile.getAbsolutePath(), assFile.getAbsolutePath());
                return null;
            }

            log.info("字幕烧录成功，outputFile: {}", outputFile.getAbsolutePath());
            return outputFile;

        } catch (IOException e) {
            log.error("烧录字幕时发生IO异常", e);
            return null;
        } finally {
            // 清理临时ASS文件
            if (assFile != null && assFile.exists()) {
                MediaFileUtil.deleteTempFileQuietly(assFile);
            }
            // 清理临时字体目录
            if (fontTempDir != null && fontTempDir.exists()) {
                deleteDirQuietly(fontTempDir);
            }
        }
    }

    /**
     * 提取字体文件到临时目录
     * 如果是classpath资源，将其复制到临时目录；否则返回字体所在目录
     *
     * @param style  ASS样式配置
     * @param tmpDir 临时目录
     * @return 包含字体文件的目录，如果无需处理则返回null
     */
    private static File extractFontToTempDir(AssStyle style, File tmpDir) {
        if (style == null || StringUtils.isBlank(style.getFontPath())) {
            return null;
        }

        try {
            if (style.isClasspathFont()) {
                // classpath资源，需要提取到临时目录
                String resourcePath = style.getClasspathResourcePath();
                ClassPathResource resource = new ClassPathResource(resourcePath);

                if (!resource.exists()) {
                    log.warn("字体资源不存在: {}", style.getFontPath());
                    return null;
                }

                // 创建字体临时目录
                File fontDir = new File(tmpDir, "fonts");
                if (!fontDir.exists() && !fontDir.mkdirs()) {
                    log.error("创建字体临时目录失败: {}", fontDir.getAbsolutePath());
                    return null;
                }

                // 提取字体文件名
                String fileName = resourcePath.contains("/")
                        ? resourcePath.substring(resourcePath.lastIndexOf('/') + 1)
                        : resourcePath;

                File fontFile = new File(fontDir, fileName);
                try (InputStream is = resource.getInputStream()) {
                    Files.copy(is, fontFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                    log.info("字体文件已提取到: {}", fontFile.getAbsolutePath());
                }

                return fontDir;
            } else {
                // 绝对路径，返回字体文件所在目录
                File fontFile = new File(style.getFontPath());
                if (fontFile.exists() && fontFile.isFile()) {
                    return fontFile.getParentFile();
                }
                log.warn("字体文件不存在: {}", style.getFontPath());
                return null;
            }
        } catch (IOException e) {
            log.error("提取字体文件失败: {}", style.getFontPath(), e);
            return null;
        }
    }

    /**
     * 静默删除目录及其内容
     */
    private static void deleteDirQuietly(File dir) {
        if (dir == null || !dir.exists()) {
            return;
        }
        try {
            File[] files = dir.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isDirectory()) {
                        deleteDirQuietly(file);
                    } else {
                        file.delete();
                    }
                }
            }
            dir.delete();
        } catch (Exception e) {
            log.warn("删除临时目录失败: {}", dir.getAbsolutePath(), e);
        }
    }

    /**
     * 将ASS字幕内容写入文件
     *
     * @param assContent ASS字幕内容
     * @param outputFile 输出文件
     * @return 是否写入成功
     */
    public static boolean writeAssFile(String assContent, File outputFile) {
        if (StringUtils.isBlank(assContent) || outputFile == null) {
            return false;
        }
        try {
            Files.writeString(outputFile.toPath(), assContent, StandardCharsets.UTF_8);
            return true;
        } catch (IOException e) {
            log.error("写入ASS文件失败: {}", outputFile.getAbsolutePath(), e);
            return false;
        }
    }

    /**
     * 检查文本是否包含[xxx]格式的标签
     *
     * @param text 待检查的文本
     * @return 如果包含[xxx]格式标签返回true，否则返回false
     */
    public static boolean containsBracketTag(String text) {
        if (StringUtils.isBlank(text)) {
            return false;
        }
        return BRACKET_TAG_PATTERN.matcher(text).find();
    }

    /**
     * 智能换行处理
     * 根据最大字符数限制，在合适的位置插入换行符
     * 优先在单词边界处换行，保持语义完整
     *
     * @param text            原始文本
     * @param maxCharsPerLine 每行最大字符数
     * @return 添加换行符后的文本
     */
    public static String smartWrapText(String text, int maxCharsPerLine) {
        if (StringUtils.isBlank(text) || text.length() <= maxCharsPerLine) {
            return text;
        }

        StringBuilder result = new StringBuilder();
        String[] words = text.split("\\s+");
        StringBuilder currentLine = new StringBuilder();

        for (String word : words) {
            if (currentLine.isEmpty()) {
                currentLine.append(word);
            } else if (currentLine.length() + 1 + word.length() <= maxCharsPerLine) {
                currentLine.append(" ").append(word);
            } else {
                if (!result.isEmpty()) {
                    result.append("\\N"); // ASS换行符
                }
                result.append(currentLine);
                currentLine = new StringBuilder(word);
            }
        }

        if (!currentLine.isEmpty()) {
            if (!result.isEmpty()) {
                result.append("\\N");
            }
            result.append(currentLine);
        }

        return result.toString();
    }

    /**
     * 将毫秒时间戳转换为ASS时间格式 (H:MM:SS.cc)
     * ASS格式使用百分秒（centiseconds），即1/100秒
     *
     * @param milliseconds 毫秒数
     * @return ASS格式的时间字符串
     */
    public static String formatAssTime(Integer milliseconds) {
        if (milliseconds == null || milliseconds < 0) {
            milliseconds = 0;
        }

        long totalSeconds = milliseconds / 1000;
        long hours = totalSeconds / 3600;
        long minutes = (totalSeconds % 3600) / 60;
        long seconds = totalSeconds % 60;
        long centiseconds = (milliseconds % 1000) / 10;

        return String.format("%d:%02d:%02d.%02d", hours, minutes, seconds, centiseconds);
    }

    /**
     * 转义ASS格式中的特殊字符
     * ASS格式中需要转义的字符：{ } 和系统换行符
     *
     * @param text 原始文本
     * @return 转义后的文本
     */
    public static String escapeAssText(String text) {
        if (StringUtils.isBlank(text)) {
            return text;
        }

        // 先将 \N 临时替换为占位符，避免被后续处理影响
        String placeholder = "\u0000ASS_NEWLINE\u0000";
        String result = text.replace("\\N", placeholder);

        // 转义特殊字符
        result = result.replace("\\", "\\\\")
                .replace("{", "\\{")
                .replace("}", "\\}")
                .replace("\n", "\\N")
                .replace("\r", "");

        // 还原 \N 换行标记
        result = result.replace(placeholder, "\\N");

        return result;
    }
}

