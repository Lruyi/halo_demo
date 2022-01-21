package com.halo;

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
public class HaloApplication {

    public static void main(String[] args) {
        SpringApplication.run(HaloApplication.class, args);
        System.out.println("HaloApplication START SUCCESS ... ");
    }

}
