package com.halo.controller;

import com.alibaba.nacos.api.annotation.NacosInjected;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Description:
 * @author: halo_ry
 * @Date: 2022/10/10 17:25
 */
@RestController
@RequestMapping("discovery")
public class DiscoveryController {

    @NacosInjected
    private NamingService namingService;

    @GetMapping("/get")
    @ResponseBody
    public List<Instance> get(@RequestParam String serviceName) throws NacosException {
        return namingService.getAllInstances(serviceName);
    }
}
