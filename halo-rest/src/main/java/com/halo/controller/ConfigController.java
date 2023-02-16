package com.halo.controller;

import com.alibaba.nacos.api.config.annotation.NacosValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description:
 * @author: halo_ry
 * @Date: 2022/10/10 17:16
 */
@RestController
@RequestMapping("config")
public class ConfigController {

    @NacosValue(value = "${useLocalCache:false}", autoRefreshed = true)
    private boolean useLocalCache;

    @GetMapping("/get")
    @ResponseBody
    public boolean get() {
        return useLocalCache;
    }
}
