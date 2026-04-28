package com.halo.utils.ffmpeg;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * FFmpeg 工具类
 * 提供视频信息解析和命令构建等功能
 *
 * @author: wangweichang@tal.com
 * @date: 2025/12/17 15:53
 */
@Slf4j
public class FFmpegUtils {

    private static final String REGEX_DURATION = "Duration: (.*?), start: (.*?), bitrate: (\\d*) kb\\/s";

    /**
     * 视频探测信息（单次探测结果）
     *
     * @param durationMs 视频时长（毫秒）
     * @param hasAudio   是否包含音频流
     * @param rawOutput  原始探测输出（可能已截断）
     */
    public record VideoProbeInfo(Integer durationMs, boolean hasAudio, String rawOutput) {
    }

    /**
     * 正则匹配视频时长（单位：毫秒）
     *
     * @param infoStr FFmpeg 输出信息
     * @return 视频时长（毫秒），解析失败返回 null
     */
    public static Integer regDuration(String infoStr) {
        if (StringUtils.isEmpty(infoStr)) {
            return null;
        }

        try {
            Pattern patternDuration = Pattern.compile(REGEX_DURATION);
            Matcher matcherDuration = patternDuration.matcher(infoStr);
            if (!matcherDuration.find()) {
                log.debug("[ffmpeg] 未找到时长信息");
                return null;
            }

            String duration = matcherDuration.group(1);
            if (StringUtils.isBlank(duration) || !duration.contains(":")) {
                log.debug("[ffmpeg] 时长格式不正确: {}", duration);
                return null;
            }

            String[] timeParts = duration.split(":");
            if (timeParts.length < 3) {
                log.debug("[ffmpeg] 时长格式不完整: {}", duration);
                return null;
            }

            int hour = Integer.parseInt(timeParts[0]);
            int minute = Integer.parseInt(timeParts[1]);
            int second;

            // 处理秒数（可能包含小数）
            String secondStr = timeParts[2];
            if (secondStr.contains(".")) {
                BigDecimal bigDecimal = BigDecimal.valueOf(Double.parseDouble(secondStr) * 1000.0D);
                second = bigDecimal.intValue();
            } else {
                second = Integer.parseInt(secondStr) * 1000;
            }

            int totalMillis = (hour * 60 * 60 * 1000) + (minute * 60 * 1000) + second;
            log.debug("[ffmpeg] 解析时长成功: {} -> {}ms", duration, totalMillis);
            return totalMillis;

        } catch (Exception e) {
            log.warn("[ffmpeg] 解析视频时长失败, infoStr: {}", infoStr, e);
            return null;
        }
    }

    /**
     * 构建 FFmpeg 命令字符串（用于日志输出）
     *
     * @param commands 命令列表
     * @return 命令字符串
     */
    public static String ffmpegCmdLine(List<String> commands) {
        if (commands == null || commands.isEmpty()) {
            return "";
        }

        StringBuilder sb = new StringBuilder();
        for (String command : commands) {
            // TODO jdk21的命令
//            if (!sb.isEmpty()) {
//                sb.append(" ");
//            }
            sb.append(command);
        }
        return sb.toString();
    }

    /**
     * 构建合并视频和音频的FFmpeg命令
     * 策略：如果视频已有音频轨道，则替换；如果没有，则添加
     *
     * @param videoFile 视频文件
     * @param audioFile 音频文件
     * @param outputFile 输出文件
     * @return FFmpeg命令列表
     */
    public static List<String> mergeAudioVideo(File videoFile, File audioFile, File outputFile) {
        if (videoFile == null || !videoFile.exists()) {
            throw new IllegalArgumentException("视频文件不存在");
        }
        if (audioFile == null || !audioFile.exists()) {
            throw new IllegalArgumentException("音频文件不存在");
        }
        if (outputFile == null) {
            throw new IllegalArgumentException("输出文件不能为空");
        }

        List<String> commands = new ArrayList<>();
        commands.add("ffmpeg");
        commands.add("-i");
        commands.add(videoFile.getAbsolutePath());
        commands.add("-i");
        commands.add(audioFile.getAbsolutePath());

        // 使用-map选择视频和音频流
        commands.add("-map");
        commands.add("0:v:0"); // 第一个输入文件的视频流
        commands.add("-map");
        commands.add("1:a:0"); // 第二个输入文件的音频流

        // 如果视频已有音频，替换它；如果没有，添加新音频
        commands.add("-c:v");
        commands.add("copy"); // 视频流直接复制，不重新编码
        commands.add("-c:a");
        commands.add("aac"); // 音频编码为AAC格式

        // 确保音频和视频同步，以较短的流结束（通常以视频长度为准）
        commands.add("-shortest");

        // 覆盖输出文件
        commands.add("-y");
        commands.add(outputFile.getAbsolutePath());

        return commands;
    }

    /**
     * 构建提取视频帧的FFmpeg命令
     * 从视频中提取指定帧号的图像
     *
     * @param videoFile 视频文件
     * @param frameNumber 帧号（从1开始）
     * @param outputFile 输出图像文件
     * @return FFmpeg命令列表
     */
    public static List<String> extractVideoFrame(File videoFile, int frameNumber, File outputFile) {
        if (videoFile == null || !videoFile.exists()) {
            throw new IllegalArgumentException("视频文件不存在");
        }
        if (frameNumber < 1) {
            throw new IllegalArgumentException("帧号必须大于0");
        }
        if (outputFile == null) {
            throw new IllegalArgumentException("输出文件不能为空");
        }

        List<String> commands = new ArrayList<>();
        commands.add("ffmpeg");
        commands.add("-i");
        commands.add(videoFile.getAbsolutePath());
        commands.add("-vf");
        // select=eq(n,frameNumber-1) 选择指定帧（FFmpeg帧号从0开始）
        commands.add("select=eq(n\\," + (frameNumber - 1) + ")");
        commands.add("-vframes");
        commands.add("1");
        commands.add("-y");
        commands.add(outputFile.getAbsolutePath());

        return commands;
    }

    /**
     * 构建为视频添加字幕的FFmpeg命令
     * 优先使用subtitles滤镜（需要libass支持），如果不支持则使用drawtext滤镜
     *
     * @param videoFile 视频文件
     * @param subtitleFile SRT字幕文件
     * @param outputFile 输出文件
     * @param useDrawtext 是否强制使用drawtext（当subtitles不支持时）
     * @return FFmpeg命令列表
     */
    public static List<String> addSubtitlesToVideo(File videoFile, File subtitleFile, File outputFile, boolean useDrawtext) {
        if (videoFile == null || !videoFile.exists()) {
            throw new IllegalArgumentException("视频文件不存在");
        }
        if (subtitleFile == null || !subtitleFile.exists()) {
            throw new IllegalArgumentException("字幕文件不存在");
        }
        if (outputFile == null) {
            throw new IllegalArgumentException("输出文件不能为空");
        }

        List<String> commands = new ArrayList<>();
        commands.add("ffmpeg");
        FFmpegEncodeParams.insertGlobalOptionsAfterFfmpeg(commands);
        commands.add("-i");
        commands.add(videoFile.getAbsolutePath());
        
        if (useDrawtext) {
            // 使用drawtext滤镜（不需要额外库支持）
            commands.add("-vf");
            // drawtext需要读取SRT文件内容并构建复杂的filter_complex
            // 这里先提供一个简单的实现，读取SRT并构建drawtext链
            String drawtextFilter = buildDrawtextFilterFromSrt(subtitleFile);
            commands.add(drawtextFilter);
        } else {
            // 尝试使用subtitles滤镜（需要libass支持，支持SRT和ASS格式）
            commands.add("-vf");
            String subtitlePath = subtitleFile.getAbsolutePath();
            // 转义路径中的特殊字符（FFmpeg滤镜语法要求）
            subtitlePath = subtitlePath.replace("\\", "\\\\")
                                       .replace(":", "\\:")
                                       .replace("[", "\\[")
                                       .replace("]", "\\]")
                                       .replace(",", "\\,")
                                       .replace("'", "\\'");
            
            // 检查文件扩展名，如果是ASS文件，不需要force_style（使用ASS文件中的样式）
            String fileName = subtitleFile.getName().toLowerCase();
            String subtitleFilter;
            if (fileName.endsWith(".ass") || fileName.endsWith(".ssa")) {
                // ASS/SSA文件已经包含样式定义，直接使用
                subtitleFilter = String.format("subtitles=%s", subtitlePath);
                log.debug("使用ASS/SSA字幕文件，保留原有样式");
            } else {
                // SRT文件需要指定样式
                subtitleFilter = String.format("subtitles=%s:force_style='FontName=Arial,FontSize=24,PrimaryColour=&Hffffff,OutlineColour=&H000000,Outline=2,Shadow=1'", 
                    subtitlePath);
            }
            commands.add(subtitleFilter);
            log.debug("构建subtitles滤镜: {}", subtitleFilter);
        }
        
        commands.add("-c:v");
        commands.add("libx264");
        FFmpegEncodeParams.appendLibx264Tuning(commands);
        commands.add("-c:a");
        commands.add("copy"); // 音频流直接复制，不重新编码
        commands.add("-y");
        commands.add(outputFile.getAbsolutePath());

        return commands;
    }

    /**
     * 构建为视频添加字幕的FFmpeg命令（默认尝试subtitles）
     */
    public static List<String> addSubtitlesToVideo(File videoFile, File subtitleFile, File outputFile) {
        return addSubtitlesToVideo(videoFile, subtitleFile, outputFile, false);
    }

    /**
     * 构建为视频添加字幕的FFmpeg命令（支持自定义字体目录）
     *
     * @param videoFile    视频文件
     * @param subtitleFile 字幕文件
     * @param outputFile   输出文件
     * @param fontsDir     字体目录（可选，用于加载自定义字体）
     * @return FFmpeg命令列表
     */
    public static List<String> addSubtitlesToVideo(File videoFile, File subtitleFile, File outputFile, File fontsDir) {
        if (videoFile == null || !videoFile.exists()) {
            throw new IllegalArgumentException("视频文件不存在");
        }
        if (subtitleFile == null || !subtitleFile.exists()) {
            throw new IllegalArgumentException("字幕文件不存在");
        }
        if (outputFile == null) {
            throw new IllegalArgumentException("输出文件不能为空");
        }

        List<String> commands = new ArrayList<>();
        commands.add("ffmpeg");
        FFmpegEncodeParams.insertGlobalOptionsAfterFfmpeg(commands);
        commands.add("-i");
        commands.add(videoFile.getAbsolutePath());

        commands.add("-vf");
        String subtitlePath = subtitleFile.getAbsolutePath();
        // 转义路径中的特殊字符（FFmpeg滤镜语法要求）
        subtitlePath = subtitlePath.replace("\\", "\\\\")
                .replace(":", "\\:")
                .replace("[", "\\[")
                .replace("]", "\\]")
                .replace(",", "\\,")
                .replace("'", "\\'");

        // 构建subtitles滤镜
        StringBuilder subtitleFilter = new StringBuilder();
        subtitleFilter.append("subtitles=").append(subtitlePath);

        // 如果指定了字体目录，添加fontsdir参数
        if (fontsDir != null && fontsDir.exists() && fontsDir.isDirectory()) {
            String fontsDirPath = fontsDir.getAbsolutePath()
                    .replace("\\", "\\\\")
                    .replace(":", "\\:")
                    .replace("[", "\\[")
                    .replace("]", "\\]")
                    .replace(",", "\\,")
                    .replace("'", "\\'");
            subtitleFilter.append(":fontsdir=").append(fontsDirPath);
            log.debug("使用自定义字体目录: {}", fontsDir.getAbsolutePath());
        }

        commands.add(subtitleFilter.toString());
        log.debug("构建subtitles滤镜: {}", subtitleFilter);

        commands.add("-c:v");
        commands.add("libx264");
        FFmpegEncodeParams.appendLibx264Tuning(commands);
        commands.add("-c:a");
        commands.add("copy");
        commands.add("-y");
        commands.add(outputFile.getAbsolutePath());

        return commands;
    }

    /**
     * 获取系统字体路径（用于drawtext滤镜）
     * 优先尝试常见的系统字体路径
     */
    private static String getSystemFontPath() {
        // macOS 系统字体路径
        String[] fontPaths = {
            "/System/Library/Fonts/Supplemental/Arial.ttf",
            "/System/Library/Fonts/Helvetica.ttc",
            "/Library/Fonts/Arial.ttf",
            "/System/Library/Fonts/Supplemental/Helvetica.ttc"
        };
        
        // 检查字体文件是否存在
        for (String fontPath : fontPaths) {
            File fontFile = new File(fontPath);
            if (fontFile.exists()) {
                log.debug("找到系统字体: {}", fontPath);
                return fontPath;
            }
        }
        
        // 如果找不到，返回空字符串，让FFmpeg使用默认字体
        log.warn("未找到系统字体，将尝试使用FFmpeg默认字体");
        return "";
    }

    /**
     * 从SRT文件构建drawtext滤镜链
     * 注意：drawtext不支持多行字幕，这里简化处理，只处理单行字幕
     */
    private static String buildDrawtextFilterFromSrt(File subtitleFile) {
        try {
            // 读取SRT文件内容
            List<String> lines = Files.readAllLines(subtitleFile.toPath(), StandardCharsets.UTF_8);
            
            List<String> drawtextFilters = new ArrayList<>();
            int index = 0;
            
            // 解析SRT格式
            while (index < lines.size()) {
                // 跳过序号行
                if (index < lines.size() && lines.get(index).trim().matches("\\d+")) {
                    index++;
                }
                
                // 读取时间轴行
                if (index < lines.size() && lines.get(index).contains("-->")) {
                    String timeLine = lines.get(index);
                    String[] times = timeLine.split("-->");
                    if (times.length == 2) {
                        double startTime = parseSrtTime(times[0].trim());
                        double endTime = parseSrtTime(times[1].trim());
                        index++;
                        
                        // 读取字幕文本
                        StringBuilder text = new StringBuilder();
                        while (index < lines.size() && !lines.get(index).trim().isEmpty()) {
                            if (text.length() > 0) {
                                text.append(" ");
                            }
                            // 先读取原始文本，转义在构建drawtext时统一处理
                            text.append(lines.get(index).trim());
                            index++;
                        }
                        
                        if (text.length() > 0) {
                            // 构建drawtext滤镜
                            // x=(w-text_w)/2 居中显示，y=h-th-20 底部显示
                            // 注意：text参数中的单引号已经转义，这里用单引号包裹整个text值
                            String escapedText = text.toString()
                                .replace("\\", "\\\\")  // 先转义反斜杠
                                .replace("'", "\\'")    // 转义单引号
                                .replace(":", "\\:")    // 转义冒号
                                .replace("[", "\\[")    // 转义方括号
                                .replace("]", "\\]")    // 转义方括号
                                .replace(",", "\\,");  // 转义逗号
                            
                            // 获取系统字体路径
                            String fontPath = getSystemFontPath();
                            
                            // 构建drawtext滤镜参数
                            StringBuilder drawtextBuilder = new StringBuilder("drawtext=");
                            drawtextBuilder.append("text='").append(escapedText).append("'");
                            
                            // 如果找到字体文件，添加fontfile参数
                            if (!fontPath.isEmpty()) {
                                // 转义字体路径中的特殊字符
                                String escapedFontPath = fontPath
                                    .replace("\\", "\\\\")
                                    .replace(":", "\\:")
                                    .replace("[", "\\[")
                                    .replace("]", "\\]")
                                    .replace(",", "\\,");
                                drawtextBuilder.append(":fontfile=").append(escapedFontPath);
                            }
                            
                            drawtextBuilder.append(":fontsize=24");
                            drawtextBuilder.append(":fontcolor=white");
                            drawtextBuilder.append(":x=(w-text_w)/2");
                            drawtextBuilder.append(":y=h-th-20");
                            drawtextBuilder.append(String.format(":enable='between(t,%.3f,%.3f)'", startTime, endTime));
                            
                            drawtextFilters.add(drawtextBuilder.toString());
                        }
                    }
                    index++;
                } else {
                    index++;
                }
            }
            
            // 如果没有任何字幕，返回空滤镜
            if (drawtextFilters.isEmpty()) {
                log.warn("SRT文件中没有有效的字幕条目");
                return "null";
            }
            
            // 组合所有drawtext滤镜
            return String.join(",", drawtextFilters);
            
        } catch (Exception e) {
            log.error("构建drawtext滤镜失败", e);
            throw new IllegalArgumentException("无法解析SRT文件: " + e.getMessage());
        }
    }

    /**
     * 解析SRT时间格式 (HH:MM:SS,mmm) 为秒数
     */
    private static double parseSrtTime(String timeStr) {
        try {
            // 格式: 00:00:00,000
            timeStr = timeStr.replace(",", ".");
            String[] parts = timeStr.split(":");
            if (parts.length == 3) {
                int hours = Integer.parseInt(parts[0]);
                int minutes = Integer.parseInt(parts[1]);
                double seconds = Double.parseDouble(parts[2]);
                return hours * 3600 + minutes * 60 + seconds;
            }
        } catch (Exception e) {
            log.warn("解析SRT时间失败: {}", timeStr, e);
        }
        return 0.0;
    }

    /**
     * 获取视频时长（毫秒）
     * 使用FFmpeg命令获取视频信息并解析时长
     *
     * @param videoFile 视频文件
     * @return 视频时长（毫秒），解析失败返回 null
     */
    public static Integer getVideoDuration(File videoFile) {
        if (videoFile == null || !videoFile.exists()) {
            log.warn("[ffmpeg] 视频文件不存在: {}", videoFile != null ? videoFile.getAbsolutePath() : "null");
            return null;
        }

        try {
            VideoProbeInfo probeInfo = probeVideoInfo(videoFile);
            if (probeInfo == null) {
                log.warn("[ffmpeg] 获取视频时长失败，探测结果为null，file: {}", videoFile.getAbsolutePath());
                return null;
            }
            Integer duration = probeInfo.durationMs();
            if (duration == null) {
                log.warn("[ffmpeg] 解析视频时长失败，file: {}, output: {}", 
                    videoFile.getAbsolutePath(), 
                    probeInfo.rawOutput() != null && probeInfo.rawOutput().length() > 500
                            ? probeInfo.rawOutput().substring(0, 500) + "..."
                            : probeInfo.rawOutput());
            } else {
                log.debug("[ffmpeg] 获取视频时长成功，file: {}, duration: {}ms", videoFile.getAbsolutePath(), duration);
            }
            return duration;
        } catch (Exception e) {
            log.error("[ffmpeg] 获取视频时长失败，file: {}", videoFile.getAbsolutePath(), e);
            return null;
        }
    }

    /**
     * 检查视频文件是否有音频流
     *
     * @param videoFile 视频文件
     * @return 是否有音频流
     */
    public static boolean hasAudioStream(File videoFile) {
        if (videoFile == null || !videoFile.exists()) {
            log.warn("[ffmpeg] 视频文件不存在: {}", videoFile != null ? videoFile.getAbsolutePath() : "null");
            return false;
        }

        try {
            VideoProbeInfo probeInfo = probeVideoInfo(videoFile);
            if (probeInfo == null) {
                log.warn("[ffmpeg] 无法获取视频信息，假设没有音频流，file: {}", videoFile.getAbsolutePath());
                return false;
            }
            
            log.debug("[ffmpeg] 检测视频音频流，file: {}, hasAudio: {}", videoFile.getAbsolutePath(), probeInfo.hasAudio());
            return probeInfo.hasAudio();
        } catch (Exception e) {
            log.warn("[ffmpeg] 检测视频音频流失败，假设没有音频流，file: {}", videoFile.getAbsolutePath(), e);
            return false;
        }
    }

    /**
     * 单次探测视频信息，避免重复调用 ffmpeg 造成额外开销。
     */
    public static VideoProbeInfo probeVideoInfo(File videoFile) {
        if (videoFile == null || !videoFile.exists()) {
            log.warn("[ffmpeg] 视频文件不存在: {}", videoFile != null ? videoFile.getAbsolutePath() : "null");
            return null;
        }

        try {
            List<String> commands = new ArrayList<>();
            commands.add("ffmpeg");
            commands.add("-i");
            commands.add(videoFile.getAbsolutePath());
            commands.add("-f");
            commands.add("null");
            commands.add("-");

            String output = FFmpegCommandRunner.runProcess(commands);
            if (output == null) {
                log.warn("[ffmpeg] 视频探测失败，命令执行返回null，file: {}", videoFile.getAbsolutePath());
                return null;
            }

            Integer duration = regDuration(output);
            // FFmpeg 输出格式示例：Stream #0:1(und): Audio: aac (LC) ...
            boolean hasAudio = output.contains("Stream") && output.contains("Audio:");
            return new VideoProbeInfo(duration, hasAudio, output);
        } catch (Exception e) {
            log.warn("[ffmpeg] 探测视频信息失败，file: {}", videoFile.getAbsolutePath(), e);
            return null;
        }
    }

    /**
     * 构建音频速度调整过滤器
     * atempo 限制在 0.5 到 2.0 之间，超出范围需要链式使用
     * 如果 speedRatio 过小或过大，限制 atempo 链的最大长度，避免命令过长
     *
     * @param speedRatio 速度比例
     * @return 音频过滤器字符串
     */
    public static String buildAudioTempoFilter(double speedRatio) {
        // atempo 的有效范围是 0.5 到 2.0
        if (speedRatio >= 0.5 && speedRatio <= 2.0) {
            // 在有效范围内，直接使用
            return String.format("[0:a]atempo=%f[a]", speedRatio);
        }
        
        // 限制 atempo 链的最大长度，避免命令过长导致失败
        // 对于极小的 speedRatio，使用最小允许值
        final int MAX_ATEMPO_CHAIN = 15; // 最多15个 atempo 过滤器
        final double MIN_EFFECTIVE_RATIO = Math.pow(0.5, MAX_ATEMPO_CHAIN); // 约 3e-5
        
        if (speedRatio < MIN_EFFECTIVE_RATIO) {
            log.warn("[ffmpeg] speedRatio {} 过小，已达到 atempo 链的最大长度限制，使用最小值 {}", speedRatio, MIN_EFFECTIVE_RATIO);
            speedRatio = MIN_EFFECTIVE_RATIO;
        }
        
        // 超出范围，需要链式使用多个 atempo
        List<Double> tempoValues = new ArrayList<>();
        double remainingRatio = speedRatio;
        int maxIterations = MAX_ATEMPO_CHAIN;
        int iterations = 0;
        
        while ((remainingRatio < 0.5 || remainingRatio > 2.0) && iterations < maxIterations) {
            if (remainingRatio < 0.5) {
                // 小于 0.5，使用 0.5
                tempoValues.add(0.5);
                remainingRatio = remainingRatio / 0.5;
            } else if (remainingRatio > 2.0) {
                // 大于 2.0，使用 2.0
                tempoValues.add(2.0);
                remainingRatio = remainingRatio / 2.0;
            }
            iterations++;
        }
        
        // 添加最后一个值（应该在有效范围内）
        if (remainingRatio >= 0.5 && remainingRatio <= 2.0) {
            tempoValues.add(remainingRatio);
        } else if (remainingRatio > 0) {
            // 如果还是超出范围，使用边界值
            tempoValues.add(remainingRatio < 0.5 ? 0.5 : 2.0);
        }
        
        // 如果链太长，记录警告
        if (tempoValues.size() > 10) {
            log.warn("[ffmpeg] atempo 链较长，speedRatio: {}, chainLength: {}, 可能影响性能", speedRatio, tempoValues.size());
        }
        
        // 构建过滤器字符串
        StringBuilder filter = new StringBuilder("[0:a]");
        for (int i = 0; i < tempoValues.size(); i++) {
            if (i > 0) {
                filter.append(",");
            }
            filter.append("atempo=").append(String.format("%f", tempoValues.get(i)));
        }
        filter.append("[a]");
        
        log.debug("[ffmpeg] 构建音频速度过滤器，speedRatio: {}, tempoValues: {}, chainLength: {}", 
                speedRatio, tempoValues, tempoValues.size());
        return filter.toString();
    }

    /**
     * 构建速度调整命令
     * atempo 过滤器速度限制：0.5 到 2.0
     * 如果超出范围，需要链式使用多个 atempo 过滤器
     *
     * @param inputVideo 输入视频文件
     * @param outputVideo 输出视频文件
     * @param speedRatio 速度比例
     * @return FFmpeg命令列表
     */
    public static List<String> buildSpeedAdjustmentCommands(File inputVideo, File outputVideo, double speedRatio) {
        return buildSpeedAdjustmentCommands(inputVideo, outputVideo, speedRatio, null);
    }

    /**
     * 构建速度调整命令（可传入已探测的音频流信息，减少重复探测）
     */
    public static List<String> buildSpeedAdjustmentCommands(File inputVideo, File outputVideo, double speedRatio, Boolean hasAudioHint) {
        if (inputVideo == null || !inputVideo.exists()) {
            throw new IllegalArgumentException("输入视频文件不存在");
        }
        if (outputVideo == null) {
            throw new IllegalArgumentException("输出文件不能为空");
        }

        // 优先使用上层已探测结果，避免重复执行 ffmpeg -i 探测
        boolean hasAudio = hasAudioHint != null ? hasAudioHint : hasAudioStream(inputVideo);
        
        List<String> commands = new ArrayList<>();
        commands.add("ffmpeg");
        FFmpegEncodeParams.insertGlobalOptionsAfterFfmpeg(commands);
        commands.add("-i");
        commands.add(inputVideo.getAbsolutePath());
        commands.add("-filter_complex");
        
        // 构建完整的 filter_complex
        String filterComplex;
        if (hasAudio) {
            // 有音频流，构建音频过滤器：处理 atempo 的速度限制（0.5-2.0）
            String audioFilter = buildAudioTempoFilter(speedRatio);
            filterComplex = String.format("[0:v]setpts=%f*PTS[v];%s", 1.0 / speedRatio, audioFilter);
        } else {
            // 没有音频流，只处理视频
            log.debug("[ffmpeg] 视频没有音频流，只处理视频部分，file: {}", inputVideo.getAbsolutePath());
            filterComplex = String.format("[0:v]setpts=%f*PTS[v]", 1.0 / speedRatio);
        }
        commands.add(filterComplex);
        
        commands.add("-map");
        commands.add("[v]");
        
        // 只有当有音频流时才映射音频
        if (hasAudio) {
            commands.add("-map");
            commands.add("[a]");
        }
        
        commands.add("-c:v");
        commands.add("libx264");
        FFmpegEncodeParams.appendLibx264Tuning(commands);
        
        // 只有当有音频时才编码音频
        if (hasAudio) {
            commands.add("-c:a");
            commands.add("aac");
        }
        
        commands.add("-y");
        commands.add(outputVideo.getAbsolutePath());
        
        log.debug("[ffmpeg] 构建FFmpeg命令，speedRatio: {}, hasAudio: {}, filterComplex: {}", speedRatio, hasAudio, filterComplex);
        return commands;
    }

    /**
     * 构建拼接视频的FFmpeg命令
     * 使用FFmpeg的concat demuxer
     *
     * @param concatFile concat列表文件
     * @param outputVideo 输出视频文件
     * @return FFmpeg命令列表
     */
    public static List<String> buildConcatenateVideosCommand(File concatFile, File outputVideo) {
        if (concatFile == null || !concatFile.exists()) {
            throw new IllegalArgumentException("concat列表文件不存在");
        }
        if (outputVideo == null) {
            throw new IllegalArgumentException("输出文件不能为空");
        }

        List<String> commands = new ArrayList<>();
        commands.add("ffmpeg");
        commands.add("-f");
        commands.add("concat");
        commands.add("-safe");
        commands.add("0");
        commands.add("-i");
        commands.add(concatFile.getAbsolutePath());
        commands.add("-c");
        commands.add("copy");
        commands.add("-y");
        commands.add(outputVideo.getAbsolutePath());

        return commands;
    }
}
