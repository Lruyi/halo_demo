package com.halo.utils;

import com.halo.annotation.ExecutionTime;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.exception.CosClientException;
import com.qcloud.cos.exception.CosServiceException;
import com.qcloud.cos.http.HttpProtocol;
import com.qcloud.cos.model.ObjectMetadata;
import com.qcloud.cos.model.PutObjectResult;
import com.qcloud.cos.region.Region;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * @Description:
 * @Author: halo_ry
 * @Date: 2025/9/11 16:59
 */
@Slf4j
@Component
//@RefreshScope
public class TencentCOSUtil {

    @Value("${tencent.cos.secretId:}")
    private String accessKeyId;

    @Value("${tencent.cos.secretKey:}")
    private String accessKeySecret;

    /**
     * 文件存储桶名称
     */
    @Value("${tencent.cos.bucketName:}")
    private String fileBucketName;

    /**
     * 存储桶所属地域
     */
    @Value("${tencent.cos.endpoint:cos.ap-beijing.myqcloud.com}")
    private String endpoint;

    /**
     * 媒体处理服务所属地域
     */
    @Value("${tencent.cos.mps-regionId:ap-beijing}")
    private String mpsRegionId;

    /**
     * 绑到桶上的自定义域名
     */
    @Value("${tencent.cos.downLoadUrlPrefix:}")
    private String downLoadUrlPrefix;

    /**
     * 单例COSClient，线程安全，避免重复创建连接
     */
    private COSClient cosClient;

    /**
     * 初始化单例COSClient
     */
    @PostConstruct
    public void initCosClient() {
        log.info("Initializing COSClient singleton...");
        // 1、实例化一个认证对象，入参需要传入腾讯云账户secretId，secretKey,此处还需注意密钥对的保密
        // 密钥可前往https://console.cloud.tencent.com/cam/capi网站进行获取
        COSCredentials cred = new BasicCOSCredentials(accessKeyId, accessKeySecret);

        // 2 设置 bucket 的地域, COS 地域的简称请参照 https://cloud.tencent.com/document/product/436/6224
        // clientConfig 中包含了设置 region, https(默认 http), 超时, 代理等 set 方法, 使用可参见源码或者常见问题 Java SDK 部分。
        Region region = new Region(mpsRegionId);
        ClientConfig clientConfig = new ClientConfig(region);
        // 这里建议设置使用 https 协议
        // 从 5.6.54 版本开始，默认使用了 https
        clientConfig.setHttpProtocol(HttpProtocol.https);

        // 3 生成 cos 客户端。
        this.cosClient = new COSClient(cred, clientConfig);
        log.info("COSClient singleton initialized successfully");
    }

    /**
     * 应用关闭时销毁COSClient
     */
    @PreDestroy
    public void destroyCosClient() {
        if (cosClient != null) {
            log.info("Shutting down COSClient singleton...");
            cosClient.shutdown();
            log.info("COSClient singleton shutdown successfully");
        }
    }


    /** 单文件上传
     *
     * @param file    文件
     * @param fileKey 文件路径
     * @return
     */
    @ExecutionTime("上传文件到COS")
    public String uploadFile(File file, String fileKey) {
        try {
            cosClient.putObject(fileBucketName, fileKey, file);
            log.info("uploadFile | putObjectResult successful");
        } catch (CosServiceException e) {
            log.error("uploadFile | fail to upload file to cos");
            throw new RuntimeException("fail to upload file to cos", e);
        }
        // 构建COS完整URL
        String url = buildCosUrl(fileKey);
        log.info("uploadFile | return url is {}", url);
        return url;
    }

    /**
     * 多文件上传
     *
     * @param files    文件列表
     * @param bastPath 文件根路径
     * @return
     */
    public List<String> uploadFiles(List<File> files, String bastPath) {
        List<String> urls = new ArrayList<>();
        for (File file : files) {
            String imageUrl = uploadFile(file, getFileKey(bastPath));
            urls.add(imageUrl);
        }
        // 3. Delete temporary file
        for (File file : files) {
            if (file != null && file.exists()) {
                file.delete();
            }
        }
        log.info("uploadFiles | return urls is {}", urls);
        return urls;
    }

    /**
     * 获取图片上传的key
     * @return
     */
    private String getFileKey(String bastPath) {
        if (StringUtils.isBlank(bastPath)) {
            bastPath = "/";
        }
        String dateStr = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        return Paths.get(bastPath, dateStr, UUID.randomUUID() + ".png").toString();
    }


    /**
     * 通过InputStream上传文件
     *
     * @param inputStream 文件输入流
     * @param filePath    文件路径
     * @param fileName    文件名
     * @return
     */
    public String uploadFileByInputStream(InputStream inputStream, String filePath, String fileName) {
        try {
            PutObjectResult putObjectResult = cosClient.putObject(fileBucketName, filePath, inputStream, new ObjectMetadata());
            log.info("uploadFileByInputStream | putObjectResult successful, putObjectResult is {} filePath is {}", putObjectResult.getETag(), filePath);
        } catch (CosClientException e) {
            log.warn("uploadFileByInputStream | fail to upload file to cos", e);
            throw new RuntimeException(String.format("Failed to handle file: %s with bucket: %s", fileName, fileBucketName), e);
        } finally {
            closeInputStream(inputStream);
        }

        // 构建COS完整URL
        String url = buildCosUrl(filePath);
        log.info("uploadFileByInputStream | return url is {}", url);
        return url;
    }

    private void closeInputStream(InputStream inputStream) {
        try {
            if (Objects.nonNull(inputStream)) {
                inputStream.close();
            }
        } catch (IOException e) {
            log.warn("closeInputStream | fail to close inputStream");
        }
    }

    /**
     * 构建COS完整URL
     * @param filePath 文件路径（可能带或不带前缀"/"）
     * @return 完整的COS访问URL
     */
    private String buildCosUrl(String filePath) {
        String prefix = downLoadUrlPrefix;

        // 确保prefix不以"/"结尾，filePath以"/"开头
        if (prefix.endsWith("/")) {
            prefix = prefix.substring(0, prefix.length() - 1);
        }

        if (!filePath.startsWith("/")) {
            filePath = "/" + filePath;
        }

        return prefix + filePath;
    }


    /**
     * 从URL下载图片并上传到COS（流式上传）
     * 优点：性能更好，不占用磁盘空间，速度更快
     * @param imageUrl 图片URL地址
     * @param fileKey COS文件路径（例如：/aigc_result/20231201/image.jpg）
     * @return 上传到COS后的URL
     */
    public String downloadAndUploadToCos(String imageUrl, String fileKey) {
        InputStream inputStream = null;
        try {
            // 1. 从URL获取输入流
            log.info("downloadAndUploadToCos | 开始从URL获取文件流: {}", imageUrl);
            inputStream = MediaFileUtil.getInputStreamFromUrl(imageUrl);
            log.info("downloadAndUploadToCos | 文件流获取成功");

            // 2. 通过流直接上传到COS（高效，无需临时文件）
            log.info("downloadAndUploadToCos | 开始流式上传到COS，fileKey: {}", fileKey);
            String fileName = MediaFileUtil.getFileExtension(imageUrl);
            String cosUrl = uploadFileByInputStream(inputStream, fileKey, fileName);
            log.info("downloadAndUploadToCos | 流式上传成功，COS URL: {}", cosUrl);

            return cosUrl;
        } catch (Exception e) {
            log.error("downloadAndUploadToCos | 流式上传失败，imageUrl: {}, fileKey: {}", imageUrl, fileKey, e);
            throw new RuntimeException("从URL流式上传到COS失败: " + e.getMessage(), e);
        } finally {
            // 3. 关闭输入流
            closeInputStream(inputStream);
        }
    }

    /**
     * 从URL下载图片并上传到COS（文件方式，适用于需要重试的场景）
     * 优点：上传失败可重试，便于调试
     * @param imageUrl 图片URL地址
     * @param fileKey COS文件路径（例如：/aigc_result/20231201/image.jpg）
     * @return 上传到COS后的URL
     */
    public String downloadAndUploadToCosWithFile(String imageUrl, String fileKey) {
        File tempFile = null;
        try {
            // 1. 从URL下载文件到本地临时文件
            log.info("downloadAndUploadToCosWithFile | 开始从URL下载文件: {}", imageUrl);
            String fileExtension = MediaFileUtil.getFileExtension(imageUrl);
            tempFile = MediaFileUtil.downloadFileFromUrl(imageUrl, fileExtension);
            log.info("downloadAndUploadToCosWithFile | 文件下载成功，临时文件路径: {}", tempFile.getAbsolutePath());

            // 2. 上传文件到COS
            log.info("downloadAndUploadToCosWithFile | 开始上传文件到COS，fileKey: {}", fileKey);
            String cosUrl = uploadFile(tempFile, fileKey);
            log.info("downloadAndUploadToCosWithFile | 文件上传成功，COS URL: {}", cosUrl);

            return cosUrl;
        } catch (Exception e) {
            log.error("downloadAndUploadToCosWithFile | 下载并上传文件失败，imageUrl: {}, fileKey: {}", imageUrl, fileKey, e);
            throw new RuntimeException("下载并上传文件到COS失败: " + e.getMessage(), e);
        } finally {
            // 3. 清理临时文件
            if (tempFile != null) {
                MediaFileUtil.deleteTempFileQuietly(tempFile);
                log.info("downloadAndUploadToCosWithFile | 临时文件已清理");
            }
        }
    }
}
