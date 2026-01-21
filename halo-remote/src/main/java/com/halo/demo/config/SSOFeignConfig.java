package com.halo.demo.config;

import com.halo.demo.annotation.FeignAddParameter;
import feign.RequestInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * SSOLoginFeignConfig
 *
 * @author : 刘宇泽
 * @date : 2023/9/14 10:14
 */
@Slf4j
@Configuration
public class SSOFeignConfig {

//    @Resource
//    private SSOConfig ssoConfig;
//
//    @Resource
//    private SSOTicketService ssoTicketService;

    @Bean
    RequestInterceptor requestInterceptor() {
        return requestTemplate -> {
            FeignAddParameter feignAddParameter = requestTemplate.methodMetadata().method().getAnnotation(FeignAddParameter.class);
            if (feignAddParameter == null) {
                return;
            }

//            String source = ContextHolder.getSource();
//            SSOConfig.AppConfig appConfig = ssoConfig.getAppConfig(source);
//            SSOTicketResp ssoTicketResp = ssoTicketService.getTicket(appConfig.getAppId(), appConfig.getAppKey());
//            String ticket = ssoTicketResp.getTicket();

            // 支持自定义参数名，如果没有指定则使用默认的 "ticket"
            String paramName = feignAddParameter.paramName();
            if (StringUtils.isBlank(paramName)) {
                paramName = "ticket";
            }

//            requestTemplate.query(paramName, ticket);
//            log.debug("使用source: {} 的SSO配置获取ticket", source);
        };
    }
}
