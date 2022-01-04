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
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
        System.out.println("DemoApplication IS START ... ");
    }

}
