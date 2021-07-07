package com.example.halo.demo.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

/**
 * @Description:
 * @Author: Halo_ry
 * @Date: 2020/4/20 22:33
 */
@Configuration
public class DruidDataSourceConfig {

    private DbTestProperties dbTestProperties;

    public DruidDataSourceConfig(DbTestProperties dbTestProperties) {
        this.dbTestProperties = dbTestProperties;
    }

    //    @ConfigurationProperties(prefix = "spring.datasource")
    @Bean
    public DataSource druid() {
        DruidDataSource druidDataSource = new DruidDataSource();
        druidDataSource.setUsername(dbTestProperties.getUsername());
        druidDataSource.setPassword(dbTestProperties.getPassword());
        druidDataSource.setUrl(dbTestProperties.getUrl());
        druidDataSource.setDriverClassName(dbTestProperties.getDriverClassName());
//        druidDataSource.setMaxActive(dbTestProperties.getMaxActive());
        return druidDataSource;
    }


    /**
     * 配置监控服务器
     *
     * @return 返回监控注册的servlet对象
     * @author SimpleWu
     */
    @Bean
    public ServletRegistrationBean statViewServlet() {
        ServletRegistrationBean servletRegistrationBean = new ServletRegistrationBean(new StatViewServlet(), "/druid/*");
        // 添加IP白名单
        servletRegistrationBean.addInitParameter("allow", "127.0.0.1");
        // 添加IP黑名单，当白名单和黑名单重复时，黑名单优先级更高
        servletRegistrationBean.addInitParameter("deny", "127.0.0.1");
        // 添加控制台管理用户
        servletRegistrationBean.addInitParameter("loginUsername", "root");
        servletRegistrationBean.addInitParameter("loginPassword", "123456");
        // 是否能够重置数据
        servletRegistrationBean.addInitParameter("resetEnable", "false");
        return servletRegistrationBean;
    }

    /**
     * 配置服务过滤器
     *
     * @return 返回过滤器配置对象
     */
    @Bean
    @Primary
    public FilterRegistrationBean statFilter1() {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean(new WebStatFilter());
        // 添加过滤规则
        filterRegistrationBean.addUrlPatterns("/*");
        // 忽略过滤格式
        filterRegistrationBean.addInitParameter("exclusions", "*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid/*,");
        return filterRegistrationBean;
    }
}
