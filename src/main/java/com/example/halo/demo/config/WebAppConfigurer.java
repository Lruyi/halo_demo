package com.example.halo.demo.config;

import com.example.halo.demo.interceptor.CheckResourceInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

/**
 * @Description:
 * @Author: Halo_ry
 * @Date: 2021/2/3 20:50
 */
@Configuration
public class WebAppConfigurer implements WebMvcConfigurer {

//    @Autowired
//    private Environment env;

//    @Value("${addpathpatterns.uri.name:}")
//    @Value("#{'${interceptor.addpathpatterns:}'.empty ? null : '${interceptor.addpathpatterns:}'.split(',')}")
    @Value("#{'${interceptor.addpathpatterns:}'.split(',')}")
    private List<String> addPathPatterns;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
//        String property = env.getProperty("addpathpatterns.uri.name");
        // 拦截  " /api/person/ "  下面所有请求
        // 拦截所有请求 "/** "
//        registry.addInterceptor(new CheckResourceInterceptor()).addPathPatterns("/api/person/**");
//        registry.addInterceptor(new CheckResourceInterceptor()).addPathPatterns("/**").excludePathPatterns();
        registry.addInterceptor(new CheckResourceInterceptor()).addPathPatterns(addPathPatterns.stream().map(String::trim).collect(toList()));
    }
}
