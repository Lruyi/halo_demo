package com.halo;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Halo_ry
 */
@SpringBootApplication(scanBasePackages = {"com.halo.*"})
@RestController
@MapperScan(basePackages = {"com.halo.mapper"})
@EnableFeignClients(basePackages = {"com.halo.demo.client"})
public class HaloApplication {

    public static void main(String[] args) {
        SpringApplication.run(HaloApplication.class, args);
        System.out.println("HaloApplication STARTUP SUCCESS ... ");
    }

}
