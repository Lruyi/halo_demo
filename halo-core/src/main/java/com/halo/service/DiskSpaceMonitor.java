package com.halo.service;

import com.halo.config.FfmpegDynamicProperties;
import com.halo.config.FfmpegProperties;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.File;

/**
 * @author:
 * @date: 2026/04/17 18:43
 * @description: 磁盘空间监控
 */
@Slf4j
@Component
public class DiskSpaceMonitor {

    @Resource
    private FfmpegProperties ffmpegProperties;
    @Resource
    private FfmpegDynamicProperties ffmpegDynamicProperties;

    /**
     * 检查磁盘空间是否充足
     */
    public boolean hasEnoughSpace() {
        File tmpDir = new File(ffmpegProperties.getTmpDir());
        if (!tmpDir.exists()) {
            tmpDir.mkdirs();
        }
        long freeSpaceGB = tmpDir.getFreeSpace() / (1024L * 1024 * 1024);
        boolean enough = freeSpaceGB >= ffmpegDynamicProperties.getMinFreeDiskGb();
        if (!enough) {
            log.warn("[磁盘] 空间不足: {}GB < 阈值 {}GB", freeSpaceGB, ffmpegDynamicProperties.getMinFreeDiskGb());
        }
        return enough;
    }

    /**
     * 获取剩余空间（GB）
     */
    public double getFreeDiskGB() {
        File tmpDir = new File(ffmpegProperties.getTmpDir());
        if (!tmpDir.exists()) {
            tmpDir.mkdirs();
        }
        return tmpDir.getFreeSpace() / (1024.0 * 1024 * 1024);
    }
}
