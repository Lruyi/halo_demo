package com.example.halo.demo;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Halo_ry
 */
@SpringBootApplication(scanBasePackages = {"com.example.halo.*"})
@RestController
@MapperScan(basePackages = {"com.example.halo.demo.mapper"})
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
        System.out.println("DemoApplication START SUCCESS ... ");
    }

}
