package com.halo.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @Description:
 * @Author: Halo_ry
 * @Date: 2021/2/20 17:34
 */

@ConfigurationProperties(prefix = "spring.datasource")
@Component
public class DbTestProperties {
    private String driverClassName;
    private String type;
    private String url;
    private String username;
    private String password;
//    private int maxActive;
//    private String minIdle;
//    private String initialSize;
//    private String maxWait;
//    private String timeBetweenEvictionRunsMillis;
//    private String minEvictableIdleTimeMillis;
//    private String validationQuery;

    public String getDriverClassName() {
        return driverClassName;
    }

    public void setDriverClassName(String driverClassName) {
        this.driverClassName = driverClassName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

