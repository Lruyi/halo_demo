package com.halo.utils;

import com.halo.enums.ImageOrientation;
import dev.matrixlab.webp4j.WebPCodec;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;

/**
 * WebP 缩略图生成工具类
 * 提供等比例缩小 + WebP 有损压缩功能，生成小体积的缩略图
 *
 * @author Generated
 * @date 2026/01/20
 */
@Component
@Slf4j
public class WebPThumbnailUtil {

    /**
     * 默认压缩质量（0.8 = 80%）
     */
    private static final float DEFAULT_QUALITY = 0.8f;

    /**
     * 生成 WebP 格式的缩略图（使用默认质量）
     *
     * @param imageUrl 原图 URL
     * @param maxSize  最大尺寸（正方形，宽高相同）
     * @return WebP 格式的字节流
     * @throws IOException IO异常
     */
    public static byte[] generateWebPThumbnail(String imageUrl, int maxSize) throws IOException {
        return generateWebPThumbnail(imageUrl, maxSize, maxSize, DEFAULT_QUALITY);
    }

    /**
     * 生成 WebP 格式的缩略图
     *
     * @param imageUrl  原图 URL
     * @param maxWidth  最大宽度
     * @param maxHeight 最大高度
     * @param quality   压缩质量（0.0-1.0，推荐 0.75-0.85）
     * @return WebP 格式的字节流
     * @throws IOException IO异常
     */
    public static byte[] generateWebPThumbnail(String imageUrl, int maxWidth, int maxHeight, float quality) throws IOException {
        log.info("开始生成 WebP 缩略图，URL: {}, 最大尺寸: {}x{}, 质量: {}", imageUrl, maxWidth, maxHeight, quality);
        
        // 1. 从 URL 读取原图
        BufferedImage originalImage = readImageFromUrl(imageUrl);
        log.info("原图尺寸: {}x{}", originalImage.getWidth(), originalImage.getHeight());
        
        // 2. 等比例缩小
        BufferedImage resizedImage = resizeProportionally(originalImage, maxWidth, maxHeight);
        log.info("缩小后尺寸: {}x{}", resizedImage.getWidth(), resizedImage.getHeight());
        
        // 3. WebP 编码
        byte[] webpBytes = encodeToWebP(resizedImage, quality);
        log.info("WebP 缩略图生成完成，文件大小: {} bytes", webpBytes.length);
        
        return webpBytes;
    }

    /**
     * 从 BufferedImage 生成 WebP 格式的缩略图
     *
     * @param image     原图 BufferedImage
     * @param maxWidth  最大宽度
     * @param maxHeight 最大高度
     * @param quality   压缩质量（0.0-1.0）
     * @return WebP 格式的字节流
     * @throws IOException IO异常
     */
    public static byte[] generateWebPThumbnail(BufferedImage image, int maxWidth, int maxHeight, float quality) throws IOException {
        log.info("开始生成 WebP 缩略图，原图尺寸: {}x{}, 最大尺寸: {}x{}, 质量: {}", 
                image.getWidth(), image.getHeight(), maxWidth, maxHeight, quality);
        
        // 1. 等比例缩小
        BufferedImage resizedImage = resizeProportionally(image, maxWidth, maxHeight);
        log.info("缩小后尺寸: {}x{}", resizedImage.getWidth(), resizedImage.getHeight());
        
        // 2. WebP 编码
        byte[] webpBytes = encodeToWebP(resizedImage, quality);
        log.info("WebP 缩略图生成完成，文件大小: {} bytes", webpBytes.length);
        
        return webpBytes;
    }

    /**
     * 等比例缩小图片（不裁剪）
     * 使用 Thumbnailator 库进行高质量缩放，保持原图宽高比
     *
     * @param image     原图
     * @param maxWidth  最大宽度
     * @param maxHeight 最大高度
     * @return 缩小后的图片，如果原图小于目标尺寸则返回原图
     */
    public static BufferedImage resizeProportionally(BufferedImage image, int maxWidth, int maxHeight) {
        if (image == null) {
            throw new IllegalArgumentException("图片不能为空");
        }
        
        int originalWidth = image.getWidth();
        int originalHeight = image.getHeight();
        
        // 如果原图小于等于目标尺寸，直接返回原图
        if (originalWidth <= maxWidth && originalHeight <= maxHeight) {
            log.debug("原图尺寸小于目标尺寸，直接返回原图");
            return image;
        }
        
        try {
            // 使用 Thumbnailator 进行高质量缩放
            // size() 方法会自动保持宽高比，选择较小的缩放比例
            BufferedImage resized = Thumbnails.of(image)
                    .size(maxWidth, maxHeight)
                    .outputQuality(1.0f)  // 保持最高质量
                    .asBufferedImage();
            
            log.debug("Thumbnailator 缩放完成，原尺寸: {}x{}, 新尺寸: {}x{}", 
                    originalWidth, originalHeight, resized.getWidth(), resized.getHeight());
            
            return resized;
        } catch (IOException e) {
            log.error("Thumbnailator 缩放失败", e);
            throw new RuntimeException("图片缩放失败: " + e.getMessage(), e);
        }
    }

    /**
     * 将 BufferedImage 编码为 WebP 格式
     * 使用 WebP4j 的 WebPCodec，支持 ARM64 架构
     *
     * @param image   待编码的图片
     * @param quality 压缩质量（0.0-1.0，0.0 最小体积，1.0 最高质量）
     * @return WebP 格式的字节流
     * @throws IOException IO异常
     */
    public static byte[] encodeToWebP(BufferedImage image, float quality) throws IOException {
        if (image == null) {
            throw new IllegalArgumentException("图片不能为空");
        }
        
        // 确保质量参数在有效范围内
        float validQuality = Math.max(0.0f, Math.min(1.0f, quality));
        if (validQuality != quality) {
            log.warn("压缩质量参数 {} 超出范围，已调整为 {}", quality, validQuality);
        }
        
        try {
            // 使用 WebPCodec 进行编码
            // WebPCodec.encodeImage() 的质量参数范围是 0-100，需要转换
            // 75 是常用折中值，这里根据输入的质量参数计算
            float webpQuality = validQuality * 100f;
            
            // WebPCodec.encodeImage() 会自动处理 RGB 和 RGBA
            byte[] webpBytes = WebPCodec.encodeImage(image, webpQuality);
            
            log.debug("WebP 编码成功，质量: {}, 文件大小: {} bytes", webpQuality, webpBytes.length);
            return webpBytes;
        } catch (Exception e) {
            log.error("WebP 编码失败", e);
            throw new IOException("WebP 编码失败: " + e.getMessage(), e);
        }
    }

    /**
     * 从 URL 读取图片
     * 使用 MediaFileUtil.getInputStreamFromUrl 方法，包含 URL 安全性校验和编码处理
     *
     * @param imageUrl 图片 URL
     * @return BufferedImage
     * @throws IOException IO异常
     */
    private static BufferedImage readImageFromUrl(String imageUrl) throws IOException {
        if (imageUrl == null || imageUrl.trim().isEmpty()) {
            throw new IllegalArgumentException("图片 URL 不能为空");
        }
        
        try {
            String encodeUrl = MediaFileUtil.encodeUrlSpecialChars(imageUrl);
            URI uri = URI.create(encodeUrl);
            URL url = uri.toURL();
            try (InputStream inputStream = url.openStream()) {
                BufferedImage image = ImageIO.read(inputStream);
                if (image == null) {
                    throw new IOException("无法读取图片，可能不是有效的图片格式: " + imageUrl);
                }
                return image;
            }
        } catch (Exception e) {
            throw new IOException("无法从 URL 读取图片: " + imageUrl, e);
        }
    }

    /**
     * 判断图片方向（从 URL）
     * 
     * @param imageUrl 图片 URL
     * @return 图片方向枚举（LANDSCAPE/PORTRAIT/SQUARE）
     * @throws IOException IO异常
     * @throws IllegalArgumentException 如果 imageUrl 为空
     */
    public static ImageOrientation getImageOrientation(String imageUrl) throws IOException {
        if (imageUrl == null || imageUrl.trim().isEmpty()) {
            throw new IllegalArgumentException("图片 URL 不能为空");
        }
        
        BufferedImage image = readImageFromUrl(imageUrl);
        return getImageOrientation(image);
    }

    /**
     * 判断图片方向（从 BufferedImage）
     * 
     * @param image 图片 BufferedImage
     * @return 图片方向枚举（LANDSCAPE/PORTRAIT/SQUARE）
     * @throws IllegalArgumentException 如果 image 为空
     */
    public static ImageOrientation getImageOrientation(BufferedImage image) {
        if (image == null) {
            throw new IllegalArgumentException("图片不能为空");
        }
        
        int width = image.getWidth();
        int height = image.getHeight();
        
        if (width > height) {
            return ImageOrientation.LANDSCAPE;
        } else if (width < height) {
            return ImageOrientation.PORTRAIT;
        } else {
            return ImageOrientation.SQUARE;
        }
    }

    /**
     * 将 WebP 格式的字节流转换为 InputStream（用于上传到 COS）
     *
     * @param webpBytes WebP 格式的字节流
     * @return InputStream 输入流，可用于上传到 COS
     * @throws IllegalArgumentException 如果 webpBytes 为空
     */
    public static InputStream webpBytesToInputStream(byte[] webpBytes) {
        if (webpBytes == null || webpBytes.length == 0) {
            throw new IllegalArgumentException("WebP 字节流不能为空");
        }
        return new ByteArrayInputStream(webpBytes);
    }

    /**
     * 将 BufferedImage 转换为 WebP 格式的 InputStream（用于上传到 COS）
     * 先对图片进行 WebP 编码，然后转换为 InputStream
     *
     * @param image   待转换的图片
     * @param quality 压缩质量（0.0-1.0，推荐 0.75-0.85）
     * @return InputStream 输入流，可用于上传到 COS
     * @throws IOException IO异常
     * @throws IllegalArgumentException 如果 image 为空
     */
    public static InputStream bufferedImageToWebPInputStream(BufferedImage image, float quality) throws IOException {
        if (image == null) {
            throw new IllegalArgumentException("图片不能为空");
        }
        byte[] webpBytes = encodeToWebP(image, quality);
        return webpBytesToInputStream(webpBytes);
    }

    /**
     * 将 BufferedImage 转换为 WebP 格式的 InputStream（使用默认质量）
     * 先对图片进行 WebP 编码，然后转换为 InputStream
     *
     * @param image 待转换的图片
     * @return InputStream 输入流，可用于上传到 COS
     * @throws IOException IO异常
     * @throws IllegalArgumentException 如果 image 为空
     */
    public static InputStream bufferedImageToWebPInputStream(BufferedImage image) throws IOException {
        return bufferedImageToWebPInputStream(image, DEFAULT_QUALITY);
    }

    /**
     * 生成 WebP 格式的缩略图并转换为 InputStream（用于上传到 COS）
     * 从图片 URL 生成缩略图，然后转换为 InputStream
     *
     * @param imageUrl  原图 URL
     * @param maxWidth  最大宽度
     * @param maxHeight 最大高度
     * @param quality   压缩质量（0.0-1.0，推荐 0.75-0.85）
     * @return InputStream 输入流，可用于上传到 COS
     * @throws IOException IO异常
     */
    public static InputStream generateWebPThumbnailAsInputStream(String imageUrl, int maxWidth, int maxHeight, float quality) throws IOException {
        byte[] webpBytes = generateWebPThumbnail(imageUrl, maxWidth, maxHeight, quality);
        return webpBytesToInputStream(webpBytes);
    }

    /**
     * 生成 WebP 格式的缩略图并转换为 InputStream（使用默认质量）
     * 从图片 URL 生成缩略图，然后转换为 InputStream
     *
     * @param imageUrl 原图 URL
     * @param maxSize  最大尺寸（正方形，宽高相同）
     * @return InputStream 输入流，可用于上传到 COS
     * @throws IOException IO异常
     */
    public static InputStream generateWebPThumbnailAsInputStream(String imageUrl, int maxSize) throws IOException {
        return generateWebPThumbnailAsInputStream(imageUrl, maxSize, maxSize, DEFAULT_QUALITY);
    }

    /**
     * 生成 WebP 格式的缩略图并转换为 InputStream（从 BufferedImage）
     * 从 BufferedImage 生成缩略图，然后转换为 InputStream
     *
     * @param image     原图 BufferedImage
     * @param maxWidth  最大宽度
     * @param maxHeight 最大高度
     * @param quality   压缩质量（0.0-1.0）
     * @return InputStream 输入流，可用于上传到 COS
     * @throws IOException IO异常
     */
    public static InputStream generateWebPThumbnailAsInputStream(BufferedImage image, int maxWidth, int maxHeight, float quality) throws IOException {
        byte[] webpBytes = generateWebPThumbnail(image, maxWidth, maxHeight, quality);
        return webpBytesToInputStream(webpBytes);
    }

    public static void main(String[] args) throws IOException {
        String imageUrl = "https://static-xesapi.speiyou.cn/backstage-ai/imageGeneration/60af7220-28be-42a6-8d33-bc6aacea2579.png";
//        String imageUrl = "https://aigc-adapter-test-static.speiyou.com/media_library/20260121/144202666/82f6fa53cd844eca843299e97365cb2c.png";
        byte[] bytes = WebPThumbnailUtil.generateWebPThumbnail(imageUrl, 512, 384, 0.8f);
        InputStream inputStream = WebPThumbnailUtil.webpBytesToInputStream(bytes);
        // 写到桌面 /Users/liuruyi//Desktop/  格式webp
        String fileName = System.currentTimeMillis() + "test.webp";
        FileUtils.copyInputStreamToFile(inputStream, new File("/Users/liuruyi/Desktop/" + fileName));
    }
}
