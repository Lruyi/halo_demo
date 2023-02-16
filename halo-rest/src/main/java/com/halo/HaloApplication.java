package com.halo;

import com.alibaba.nacos.spring.context.annotation.config.NacosPropertySource;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Halo_ry
 */
@SpringBootApplication(scanBasePackages = {"com.halo.*"})
@RestController
@MapperScan(basePackages = {"com.halo.mapper"})
@NacosPropertySource(dataId = "example", autoRefreshed = true)
public class HaloApplication {

    public static void main(String[] args) {
        SpringApplication.run(HaloApplication.class, args);
        System.out.println("HaloApplication START UP SUCCESS ... ");
    }

}
