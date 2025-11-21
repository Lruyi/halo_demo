package com.halo.utils;

import com.halo.exception.BusinessException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * 媒体文件工具类
 */
public class MediaFileUtil {

    public static final String TMP_FILE_BASE_DIR = "/tmp";

    public static final String TMP_BARCODE_SVG = TMP_FILE_BASE_DIR + "/barcode/svg";
    public static final String TMP_BARCODE_PDF = TMP_FILE_BASE_DIR + "/barcode/pdf";

    public static final String COS_BARCODE_BASE_PATH = "/barcode";
    public static final String COS_BARCODE_SVG_PATH = COS_BARCODE_BASE_PATH + "/svg";
    public static final String COS_BARCODE_PDF_PATH = COS_BARCODE_BASE_PATH + "/pdf";
    public static final String COS_BARCODE_PDF_COVER_PATH = COS_BARCODE_BASE_PATH + "/pdf_cover";

    public static final String COS_BATCH_TASK_FILE_PATH = "/batch_task_file";
    public static final String COS_CLIENT_UPLOAD_PATH = "/client_upload";
    public static final String COS_MEDIA_LIB_PATH = "/media_library";
    public static final String COS_MEDIA_COMPOSITION_PATH = "/media_composition";
    public static final String COS_AIGC_RESULT_PATH = "/aigc_result";
    public static final String COS_EXPORT_TASK_PATH = "/export_task";

    public static final String COS_LAYERS_BASE_PATH = "/layers";
    public static final String COS_LAYERS_TASK_PATH = COS_LAYERS_BASE_PATH + "/layers_task";
    public static final String COS_LAYERS_TASK_IMG_PARSE_PATH = COS_LAYERS_TASK_PATH + "/img_parse";
    public static final String COS_LAYERS_TASK_RESULT_PATH = COS_LAYERS_BASE_PATH + "/result";

    public static final String COS_TXT2IMG_PATH = "/text2img";

    public static final int IMAGE_TYPE = 1;
    public static final int VIDEO_TYPE = 2;
    public static final int AUDIO_TYPE = 3;

    private static final List<String> ALLOWED_IMAGE_EXTENSIONS = Arrays.asList("jpg", "jpeg", "png", "gif", "bmp");
    private static final List<String> ALLOWED_VIDEO_EXTENSIONS = Arrays.asList("mp4", "avi", "mov", "wmv");
    private static final List<String> ALLOWED_AUDIO_EXTENSIONS = Arrays.asList("mp3", "wav", "aac", "m4a", "ogg");
    private static final Logger log = LoggerFactory.getLogger(MediaFileUtil.class);

    public static String generateUploadPath(String subFolder, String fileName) {
        String dateFolder = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String timestampFolder = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HHmmssSSS"));
        return subFolder + "/" + dateFolder + "/" + timestampFolder + "/" + fileName;
    }

    /**
     * 获取文件扩展名
     */
    public static String getFileExtension(String filename) {
        int lastDotIndex = filename.lastIndexOf('.');
        return (lastDotIndex > 0) ? filename.substring(lastDotIndex + 1) : "";
    }

    /**
     * 检查文件类型是否合法
     */
    public static boolean isValidFileType(String extension) {
        return ALLOWED_IMAGE_EXTENSIONS.contains(extension) ||
                ALLOWED_VIDEO_EXTENSIONS.contains(extension) ||
                ALLOWED_AUDIO_EXTENSIONS.contains(extension);
    }

    /**
     * 获取文件类型
     * @return 1:图片 2:视频 3:音频 0:未知
     */
    public static Integer getFileType(String extension) {
        if (ALLOWED_IMAGE_EXTENSIONS.contains(extension)) {
            return IMAGE_TYPE;  // 图片
        } else if (ALLOWED_VIDEO_EXTENSIONS.contains(extension)) {
            return VIDEO_TYPE;  // 视频
        } else if (ALLOWED_AUDIO_EXTENSIONS.contains(extension)) {
            return AUDIO_TYPE;  // 音频
        }
        return 0;  // 未知类型
    }

    /**
     * 获取支持的文件类型描述
     */
    public static String getAllowedFileTypesDescription() {
        return String.join(",", ALLOWED_IMAGE_EXTENSIONS) + "," +
                String.join(",", ALLOWED_VIDEO_EXTENSIONS) + "和" +
                String.join(",", ALLOWED_AUDIO_EXTENSIONS);
    }

    /**
     * 创建基于日期的临时目录
     * @return 日期临时目录
     */
    private static File createDateTempDir() {
        String datePath = new SimpleDateFormat("yyyyMMdd").format(new Date());
        File tmpDir = new File(System.getProperty("java.io.tmpdir"), datePath);
        if (!tmpDir.exists()) {
            tmpDir.mkdirs();
        }
        return tmpDir;
    }

    public static File convertMultipartFileToFile(MultipartFile file) throws IOException {
        // 创建日期目录
        File tmpDir = createDateTempDir();

        // 在日期目录下创建临时文件
        File tempFile = File.createTempFile("temp", file.getOriginalFilename(), tmpDir);
        file.transferTo(tempFile);
        return tempFile;
    }

    /**
     * 删除临时文件
     * @param file 临时文件
     * @return 是否删除成功
     */
    public static boolean deleteTempFile(File file) {
        if (file != null && file.exists()) {
            boolean deleted = file.delete();
            // 如果目录为空，删除日期目录
            if (deleted) {
                File parentDir = file.getParentFile();
                if (parentDir != null && parentDir.isDirectory() && parentDir.list().length == 0) {
                    parentDir.delete();
                }
            }
            return deleted;
        }
        return false;
    }

    /**
     * 删除临时文件，忽略结果
     * @param file 临时文件
     */
    public static void deleteTempFileQuietly(File file) {
        try {
            deleteTempFile(file);
        } catch (Exception ignored) {
            // 忽略删除失败的异常
        }
    }

    /**
     * Base64图片数据转换为文件
     *
     * @param base64Data Base64编码的图片数据
     * @param extension  文件扩展名（例如：png, jpg）
     * @return 临时文件
     */
    public static File base64ToFile(String base64Data, String extension) {
        try {
            // 解码Base64图片数据
            byte[] imageBytes = Base64.getDecoder().decode(base64Data);

            // 创建日期临时目录
            File tmpDir = createDateTempDir();

            // 在日期目录下创建临时文件
            File outputFile = File.createTempFile("base64_", "." + extension, tmpDir);

            // 写入图片数据
            try (FileOutputStream fos = new FileOutputStream(outputFile)) {
                fos.write(imageBytes);
            }

            return outputFile;
        } catch (IOException e) {
            log.error("Base64转文件失败", e);
            throw new RuntimeException("Base64转文件失败：" + e.getMessage(), e);
        }
    }


    /**
     * 对URL中的特殊字符进行编码，保持协议和域名部分不变
     * @param url 原始URL
     * @return 编码后的URL
     */
    public static String encodeUrlSpecialChars(String url) {
        try {
            // 如果URL已经被编码过，直接返回
            if (url.contains("%20") || url.contains("%") && url.matches(".*%[0-9A-Fa-f]{2}.*")) {
                return url;
            }

            // 找到协议和域名部分（例如：https://domain.com）
            int protocolEndIndex = url.indexOf("://");
            if (protocolEndIndex == -1) {
                // 如果没有协议，整个URL都需要编码
                return URLEncoder.encode(url, String.valueOf(StandardCharsets.UTF_8)).replace("+", "%20");
            }

            int pathStartIndex = url.indexOf('/', protocolEndIndex + 3);
            if (pathStartIndex == -1) {
                // 没有路径部分，直接返回
                return url;
            }

            // 分别处理基础URL和路径部分
            String baseUrl = url.substring(0, pathStartIndex);
            String pathPart = url.substring(pathStartIndex);

            // 对路径部分进行编码，但保留路径分隔符
            String[] pathSegments = pathPart.split("/");
            StringBuilder encodedPath = new StringBuilder();

            for (int i = 0; i < pathSegments.length; i++) {
                if (i > 0) {
                    encodedPath.append("/");
                }
                if (!pathSegments[i].isEmpty()) {
                    // 对每个路径段进行编码
                    String encoded = URLEncoder.encode(pathSegments[i], String.valueOf(StandardCharsets.UTF_8));
                    // 将+替换为%20（空格的标准URL编码）
                    encoded = encoded.replace("+", "%20");
                    encodedPath.append(encoded);
                }
            }

            return baseUrl + encodedPath.toString();
        } catch (Exception e) {
            log.warn("URL编码失败，使用原始URL: {}", url, e);
            // 如果编码失败，至少处理常见的特殊字符
            return url.replace(" ", "%20");
        }
    }

    /**
     * 从HTTP URL获取输入流（用于流式处理）
     * @param fileUrl HTTP文件URL
     * @return 输入流（调用方需要负责关闭）
     * @throws IOException 如果获取流失败
     */
    public static InputStream getInputStreamFromUrl(String fileUrl) throws IOException {
        try {
            // 对URL进行编码，处理文件名中带有特殊字符的情况
            String encodedUrl = encodeUrlSpecialChars(fileUrl);
            URL url = URI.create(encodedUrl).toURL();
            InputStream inputStream = url.openStream();
            log.debug("成功从URL获取输入流: {}", fileUrl);
            return inputStream;
        } catch (Exception e) {
            log.error("从URL获取输入流失败: {}", fileUrl, e);
            throw new IOException("从URL获取输入流失败: " + e.getMessage(), e);
        }
    }

    /**
     * 从HTTP URL下载文件并创建临时文件
     * @param fileUrl HTTP文件URL
     * @param fileExtension 文件扩展名，如果为空则从URL中提取
     * @return 创建的临时文件
     * @throws IOException 如果下载失败
     */
    public static File downloadFileFromUrl(String fileUrl, String fileExtension) throws IOException {
        try {
            // 对URL进行编码，处理文件名中带有特殊字符的情况
            String encodedUrl = encodeUrlSpecialChars(fileUrl);
            URL url = URI.create(encodedUrl).toURL();
            try (InputStream inputStream = url.openStream()) {
                // 确定文件扩展名
                String extension = fileExtension;
                if (StringUtils.isBlank(extension)) {
                    extension = getFileExtension(fileUrl);
                }
                if (extension.isEmpty()) {
                    extension = "tmp";
                }

                // 创建日期临时目录
                File tmpDir = createDateTempDir();

                // 在日期目录下创建临时文件
                File tempFile = File.createTempFile("download_", "." + extension, tmpDir);

                // 将下载的内容复制到临时文件
                Files.copy(inputStream, tempFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

                log.debug("成功从URL下载文件到临时文件: {} -> {}", fileUrl, tempFile.getAbsolutePath());
                return tempFile;
            }
        } catch (Exception e) {
            log.error("从URL下载文件失败: {}", fileUrl, e);
            throw new IOException("从URL下载文件失败: " + e.getMessage(), e);
        }
    }

    /**
     * 批量从HTTP URL下载文件并创建临时文件
     * @param fileUrls HTTP文件URL列表
     * @param fileExtension 文件扩展名，如果为空则从URL中提取（对所有URL生效）
     * @return URL到文件的映射，只包含成功下载的文件
     */
    public static Map<String, File> downloadFilesFromUrls(List<String> fileUrls, String fileExtension) {
        Map<String, File> resultMap = new HashMap<>();
        if (fileUrls == null || fileUrls.isEmpty()) {
            return resultMap;
        }

        for (String fileUrl : fileUrls) {
            if (StringUtils.isBlank(fileUrl)) {
                log.warn("跳过空的URL");
                continue;
            }
            try {
                File downloadedFile = downloadFileFromUrl(fileUrl, fileExtension);
                resultMap.put(fileUrl, downloadedFile);
                log.debug("批量下载成功: {} -> {}", fileUrl, downloadedFile.getAbsolutePath());
            } catch (Exception e) {
                log.error("批量下载文件失败，跳过该URL: {}", fileUrl, e);
                // 继续处理下一个URL，不中断整个批量下载过程
            }
        }

        log.info("批量下载完成，成功: {}/{}", resultMap.size(), fileUrls.size());
        return resultMap;
    }

    /**
     * 批量从HTTP URL下载文件并创建临时文件（每个URL可指定不同的扩展名）
     * @param fileUrlMap URL到文件扩展名的映射，如果扩展名为空则从URL中提取
     * @return URL到文件的映射，只包含成功下载的文件
     */
    public static Map<String, File> downloadFilesFromUrls(Map<String, String> fileUrlMap) {
        Map<String, File> resultMap = new HashMap<>();
        if (fileUrlMap == null || fileUrlMap.isEmpty()) {
            return resultMap;
        }

        for (Map.Entry<String, String> entry : fileUrlMap.entrySet()) {
            String fileUrl = entry.getKey();
            String fileExtension = entry.getValue();
            
            if (StringUtils.isBlank(fileUrl)) {
                log.warn("跳过空的URL");
                continue;
            }
            try {
                File downloadedFile = downloadFileFromUrl(fileUrl, fileExtension);
                resultMap.put(fileUrl, downloadedFile);
                log.debug("批量下载成功: {} -> {}", fileUrl, downloadedFile.getAbsolutePath());
            } catch (Exception e) {
                log.error("批量下载文件失败，跳过该URL: {}", fileUrl, e);
                // 继续处理下一个URL，不中断整个批量下载过程
            }
        }

        log.info("批量下载完成，成功: {}/{}", resultMap.size(), fileUrlMap.size());
        return resultMap;
    }

    /**
     * 格式化文件大小显示
     * @param size 文件大小（字节）
     * @return 格式化后的大小字符串
     */
    public static String formatFileSize(long size) {
        if (size < 1024) {
            return size + "B";
        } else if (size < 1024 * 1024) {
            return String.format("%.1fKB", size / 1024.0);
        } else if (size < 1024 * 1024 * 1024) {
            return String.format("%.1fMB", size / (1024.0 * 1024.0));
        } else {
            return String.format("%.1fGB", size / (1024.0 * 1024.0 * 1024.0));
        }
    }

    /**
     * 将MB转换为字节大小
     * @param sizeInMB 文件大小（MB）
     * @return 字节大小
     */
    public static long convertMBToBytes(double sizeInMB) {
        return (long) (sizeInMB * 1024 * 1024);
    }

    /**
     * 从URL中提取文件名（支持ComfyUI格式和普通URL格式）
     * 处理ComfyUI特殊格式的URL，如：http://10.97.0.6:8001/view?filename=ComfyUI_temp_uyxas_00008_.png&subfolder=&type=temp&rand=1756262959.6516235
     * 
     * @param url 图片URL
     * @return 提取的文件名，如果提取失败则返回基于时间戳的默认名称
     */
    public static String extractFileNameFromComfyUrl(String url) {
        if (StringUtils.isBlank(url)) {
            return generateDefaultFileName("file");
        }

        try {
            // 优先处理ComfyUI格式的URL（包含filename参数）
            String fileName = extractFileNameFromQueryParam(url);
            if (StringUtils.isNotBlank(fileName)) {
                log.debug("从URL查询参数提取文件名: {}", fileName);
                return fileName;
            }

            // 从URL路径提取文件名
            fileName = extractFileNameFromPath(url);
            if (StringUtils.isNotBlank(fileName)) {
                log.debug("从URL路径提取文件名: {}", fileName);
                return fileName;
            }

            // 生成默认文件名
            String defaultFileName = generateDefaultFileName("file");
            log.warn("无法从URL提取文件名，使用默认文件名: {}, URL: {}", defaultFileName, url);
            return defaultFileName;

        } catch (Exception e) {
            String defaultFileName = generateDefaultFileName("file");
            log.warn("从URL提取文件名时发生异常，使用默认文件名: {}, URL: {}", defaultFileName, url, e);
            return defaultFileName;
        }
    }

    /**
     * 从URL查询参数中提取filename
     * @param url URL
     * @return 文件名，如果没有找到返回null
     */
    private static String extractFileNameFromQueryParam(String url) {
        if (url.contains("filename=")) {
            int start = url.indexOf("filename=") + "filename=".length();
            int end = url.indexOf("&", start);
            if (end == -1) {
                end = url.length();
            }
            String fileName = url.substring(start, end);
            return StringUtils.isNotBlank(fileName) ? fileName : null;
        }
        return null;
    }

    /**
     * 从URL路径中提取文件名
     * @param url URL
     * @return 文件名，如果没有找到返回null
     */
    public static String extractFileNameFromPath(String url) {
        String path = url.contains("?") ? url.substring(0, url.indexOf("?")) : url;
        int lastSlashIndex = path.lastIndexOf("/");
        if (lastSlashIndex >= 0 && lastSlashIndex < path.length() - 1) {
            String pathFileName = path.substring(lastSlashIndex + 1);
            return StringUtils.isNotBlank(pathFileName) ? pathFileName : null;
        }
        return null;
    }

    /**
     * 生成默认文件名
     * @param prefix 文件名前缀
     * @return 默认文件名
     */
    private static String generateDefaultFileName(String prefix) {
        return prefix + "_" + System.currentTimeMillis() + ".png";
    }

    /**
     * 创建导出目录（公共方法）
     *
     * @param dirPath 目录路径
     * @return 创建的目录File对象
     */
    public static File createExportDir(String dirPath) {
        File dir = new File(dirPath);
        if (!dir.exists()) {
            boolean success = dir.mkdirs();
            if (!success) {
                throw new BusinessException("创建导出目录失败: " + dirPath);
            }
        }
        return dir;
    }

    /**
     * 递归压缩目录
     *
     * @param rootDir 根目录
     * @param sourceDir 源目录
     * @param zos ZIP输出流
     */
    public static void zipDirectory(File rootDir, File sourceDir, ZipOutputStream zos) throws IOException {
        File[] files = sourceDir.listFiles();
        if (files == null) {
            return;
        }

        for (File file : files) {
            if (file.isDirectory()) {
                zipDirectory(rootDir, file, zos);
            } else {
                String relativePath = getRelativePath(rootDir, file);
                ZipEntry zipEntry = new ZipEntry(relativePath);
                zos.putNextEntry(zipEntry);

                try (FileInputStream fis = new FileInputStream(file)) {
                    byte[] buffer = new byte[1024];
                    int len;
                    while ((len = fis.read(buffer)) > 0) {
                        zos.write(buffer, 0, len);
                    }
                }

                zos.closeEntry();
            }
        }
    }

    /**
     * 获取相对路径
     *
     * @param rootDir 根目录
     * @param file 文件
     * @return 相对路径
     */
    public static String getRelativePath(File rootDir, File file) {
        String rootPath = rootDir.getAbsolutePath();
        String filePath = file.getAbsolutePath();
        return filePath.substring(rootPath.length() + 1);
    }

    /**
     * 清理临时文件
     *
     * @param tempDir 临时目录
     * @param zipFile 压缩文件
     */
    public static void cleanupTempFiles(File tempDir, File zipFile) {
        try {
            // 删除临时目录及其内容
            deleteDirectory(tempDir);

            // 删除压缩文件
            if (zipFile.exists()) {
                zipFile.delete();
            }

            log.info("清理临时文件完成");
        } catch (Exception e) {
            log.warn("清理临时文件失败", e);
        }
    }

    /**
     * 递归删除目录
     *
     * @param dir 目录
     */
    public static void deleteDirectory(File dir) {
        try {
            FileUtils.deleteDirectory(dir);
        } catch (Exception e) {
            log.error("删除目录失败: {}", dir.getAbsolutePath(), e);
        }
    }
}