package com.halo.utils.ffmpeg;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

/**
 * 进程输出流处理器
 * 负责读取并处理进程的标准输出和错误输出
 *
 * @author: wangweichang@tal.com
 * @date: 2025/12/17 15:52
 */
@Slf4j
public class ProcessCallbackHandler {

    /**
     * 进程输出最大缓存字符数，避免超大输出占满堆内存。
     */
    private static final int MAX_CAPTURE_CHARS = 200_000;

    /**
     * 处理进程输入流，读取所有输出内容
     *
     * @param inputStream 进程输入流
     * @return 读取到的所有内容，读取失败返回空字符串
     * @throws IOException 读取流时发生异常
     */
    public String handler(InputStream inputStream) throws IOException {
        if (inputStream == null) {
            log.warn("[ffmpeg] 输入流为空，返回空字符串");
            return "";
        }

        StringBuilder sb = new StringBuilder();
        boolean truncated = false;
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (sb.length() < MAX_CAPTURE_CHARS) {
                    int remaining = MAX_CAPTURE_CHARS - sb.length();
                    if (line.length() + 1 <= remaining) {
                        sb.append(line).append("\n");
                    } else if (remaining > 0) {
                        int canAppend = Math.max(0, remaining - 1);
                        if (canAppend > 0) {
                            sb.append(line, 0, Math.min(line.length(), canAppend));
                        }
                        sb.append("\n");
                        truncated = true;
                    }
                } else {
                    // 继续读取并丢弃，避免子进程输出阻塞
                    truncated = true;
                }
            }
        } catch (IOException e) {
            log.error("[ffmpeg] 读取进程输出流失败", e);
            throw e;
        }

        if (truncated) {
            sb.append("[ffmpeg] 输出已截断，超过最大缓存长度 ").append(MAX_CAPTURE_CHARS).append(" 字符\n");
        }

        String result = sb.toString();
        log.debug("[ffmpeg] 读取进程输出完成, 长度: {} 字符", result.length());
        return result;
    }
}
