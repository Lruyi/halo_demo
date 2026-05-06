package com.halo.config;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.util.Asserts;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

@Data
@Configuration
@ConfigurationProperties("sources")
public class SourcesConfig {

    private List<SourceInfo> config;

    /**
     * 获取配置的source名称列表
     * @return
     */
    public List<String> getConfigSourceNames() {
        if (CollectionUtils.isEmpty(config)) {
            return new ArrayList<>();
        }
        return config.stream().map(SourceInfo::getName).collect(Collectors.toList());
    }

    /**
     * 获取source对应的验签key
     * @param source
     * @return
     */
    public String getSourceSecretKey(String source) {
        Asserts.notBlank(source, "source is null");
        if (config == null || config.isEmpty()) {
            throw new IllegalStateException("source not config");
        }
        Map<String, String> sourceSecretKeyMap = new HashMap<>();
        for (SourceInfo sourceInfo : config) {
            sourceSecretKeyMap.put(sourceInfo.getName(), sourceInfo.getSecretKey());
        }
        String secretKey = sourceSecretKeyMap.get(source);
        if (StringUtils.isBlank(secretKey)) {
            throw new IllegalStateException("source secret key not config");
        }
        return secretKey;
    }

    public String getClientSecret(String source) {
        Asserts.notBlank(source, "source is null");
        if (config == null || config.isEmpty()) {
            throw new IllegalStateException("source not config");
        }
        Map<String, SourceInfo> sourceInfoMap = new HashMap<>();
        for (SourceInfo sourceInfo : config) {
            sourceInfoMap.put(sourceInfo.getName(), sourceInfo);
        }
        SourceInfo sourceInfo = sourceInfoMap.get(source);
        if (Objects.isNull(sourceInfo)) {
            throw new IllegalStateException("source not config, source: " + source);
        }
        return sourceInfo.getClientSecret();
    }

}
