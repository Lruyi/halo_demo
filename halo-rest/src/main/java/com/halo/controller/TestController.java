package com.halo.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description:
 * @Author: liuruyi
 * @Date: 2026/1/15 14:52
 */
@Profile({"dev", "test", "pre"}) // 只允许指定的这三个环境访问
@Slf4j
@RestController
@RequestMapping("/test")
public class TestController {
}
