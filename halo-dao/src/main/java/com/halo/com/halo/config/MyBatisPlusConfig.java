package com.halo.com.halo.config;

import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.core.MybatisXMLLanguageDriver;
import com.baomidou.mybatisplus.core.config.GlobalConfig;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import com.halo.plugins.SqlInterceptor;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.type.JdbcType;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description:
 * @author: halo_ry
 * @Date: 2022/3/16 11:29
 */
@Configuration
public class MyBatisPlusConfig {

    @Bean("sqlSessionFactory")
    public SqlSessionFactory sqlSessionFactory(DataSource dataSource) throws Exception {
        MybatisSqlSessionFactoryBean sqlSessionFactory = new MybatisSqlSessionFactoryBean();
        sqlSessionFactory.setDataSource(dataSource);
        sqlSessionFactory.setGlobalConfig(globalConfig());
        sqlSessionFactory.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath*:/mapper/*.xml"));

        MybatisConfiguration configuration = new MybatisConfiguration();
        configuration.setDefaultScriptingLanguage(MybatisXMLLanguageDriver.class);
        configuration.setJdbcTypeForNull(JdbcType.NULL);
        configuration.setMapUnderscoreToCamelCase(true);
        configuration.setCacheEnabled(false);

        sqlSessionFactory.setConfiguration(configuration);

        List<Interceptor> interceptors = new ArrayList<>();
        interceptors.add(sqlInterceptor());
        sqlSessionFactory.setPlugins(interceptors.toArray(new Interceptor[0]));
        return sqlSessionFactory.getObject();
    }

    @Bean
    public SqlInterceptor sqlInterceptor() {
        // todo
        return new SqlInterceptor();
    }

    /**
     * 创建全局配置
     *
     * @return
     */
    @Bean
    public GlobalConfig globalConfig() {
        // 全局配置文件
        return new GlobalConfig();
    }
}
