package com.halo.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

/**
 * @Description:
 * @author: halo_ry
 * @Date: 2022/10/10 18:00
 */
@Configuration
@ConfigurationProperties(prefix = "mplat.webmvc")
public class WebMvcProperties {

    private XesOrigin xesOrigin;

    public XesOrigin getXesOrigin() {
        return xesOrigin;
    }

    public void setXesOrigin(XesOrigin xesOrigin) {
        this.xesOrigin = xesOrigin;
    }

    public static class XesOrigin {
        /**
         * 请求来源Header名称
         */
        public static final String HEADER_XES_ORIGIN = "xes-origin";
        /**
         * 是否启用验证有效性
         */
        private boolean validated = false;
        /**
         * 配置项
         */
        private Map<String, String> items;

        /**
         * 是否为有效的请求源
         *
         * @param xesOrigin
         * @return
         */
        public boolean isValidXesOrigin(String xesOrigin) {
            return items.containsKey(xesOrigin);
        }

        public Map<String, String> getItems() {
            return items;
        }

        public void setItems(Map<String, String> items) {
            this.items = items;
        }

        public boolean isValidated() {
            return validated;
        }

        public void setValidated(boolean validated) {
            this.validated = validated;
        }
    }
}
