package com.halo.storage;

import com.halo.config.FfmpegProperties;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.Duration;

/**
 * @author:
 * @date: 2026/04/17 18:43
 * @description: 文件下载器 — 从 URL 下载文件到本地临时目录
 */
@Slf4j
@Component
public class FileDownloader {

    @Resource
    private FfmpegProperties ffmpegProperties;

    private final HttpClient httpClient = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(30))
            .followRedirects(HttpClient.Redirect.NORMAL)
            .build();

    /**
     * 下载文件到任务工作目录
     *
     * @param url      远程文件 URL
     * @param taskId   任务ID
     * @param fileName 本地保存的文件名
     * @return 本地文件路径
     */
    public Path download(String url, Long taskId, String fileName) throws IOException, InterruptedException {
        Path inputDir = Path.of(ffmpegProperties.getTmpDir(), String.valueOf(taskId), "input");
        Files.createDirectories(inputDir);
        Path localFile = inputDir.resolve(fileName);

        log.info("[下载] taskId={}, url={}, 保存至: {}", taskId, url, localFile);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .timeout(Duration.ofMinutes(10))
                .GET()
                .build();

        HttpResponse<InputStream> response = httpClient.send(request, HttpResponse.BodyHandlers.ofInputStream());
        if (response.statusCode() != 200) {
            throw new IOException("下载失败，HTTP " + response.statusCode() + ", url=" + url);
        }

        try (InputStream is = response.body()) {
            Files.copy(is, localFile, StandardCopyOption.REPLACE_EXISTING);
        }

        log.info("[下载] taskId={}, 完成, 文件大小: {} bytes", taskId, Files.size(localFile));
        return localFile;
    }

    /**
     * 获取任务工作目录
     */
    public Path getTaskWorkDir(Long taskId) {
        return Path.of(ffmpegProperties.getTmpDir(), String.valueOf(taskId));
    }

    /**
     * 获取任务输出目录
     */
    public Path getTaskOutputDir(Long taskId) throws IOException {
        Path outputDir = Path.of(ffmpegProperties.getTmpDir(), String.valueOf(taskId), "output");
        Files.createDirectories(outputDir);
        return outputDir;
    }
}
