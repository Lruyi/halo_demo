package com.halo.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * @Description:
 * @author: halo_ry
 * @Date: 2022/5/27 14:54
 */
@Data
@ConfigurationProperties(prefix = "halo.config")
@Component
public class HaloConfig {

    /**
     * 优先从配置文件读取值，如果从配置文件获取不到或者没有配置，
     * 就取默认值，冒号后面的值，如下默认值 06:00:00
     */
    @Value("${halo.config.beginTime:06:00:00}")
    private String beginTime;

    @Value("${halo.config.endTime:22:00:00}")
    private String endTime;

    @Value("#{'${lesson.config.businessBelong:-1}'.split(',')}")
    private Set<String> businessBelong;
}
