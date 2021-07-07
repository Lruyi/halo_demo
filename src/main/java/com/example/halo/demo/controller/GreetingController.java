package com.example.halo.demo.controller;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Assert;
import com.alibaba.fastjson.JSON;
import com.example.halo.demo.api.PeopleServiceApi;
import com.example.halo.demo.common.Result;
import com.example.halo.demo.entity.Greeting;
import com.example.halo.demo.entity.People;
import com.example.halo.demo.enums.PeopleEnum;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @Description:
 * @Author: Halo_ry
 * @Date: 2020/3/13 22:43
 */
@RestController
@RequestMapping("/halo")
@Slf4j
public class GreetingController {
    //    private final Logger log = LoggerFactory.getLogger(GreetingController.class);
    public static final String TEMPLATE = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();

    @Autowired
    private PeopleServiceApi peopleServiceApi;


    @GetMapping("/hello")
    public String hello(@RequestParam(value = "name", defaultValue = "World") String name) {
        return String.format("hello %s!", name);
    }

//    @Cacheable
    @RequestMapping("/greeting")
    public Greeting greeting(HttpServletRequest request, @RequestParam(value = "name", defaultValue = "World") String name) {
        log.info("页面请求参数：{}", JSON.toJSONString(name));
        People people = new People();
        people.setId(2);
        Result<People> result = peopleServiceApi.queryPeople(people);
        return new Greeting(counter.incrementAndGet(), String.format(TEMPLATE, name));
    }

    /**
     * 获取对应的人信息
     *
     * @param request
     * @param people
     * @return
     */
    @RequestMapping("/queryPeople")
    public Result<People> queryPeople(HttpServletRequest request, HttpServletResponse response, @RequestBody @Validated People people) {
        log.info("接口请求参数：{}", JSON.toJSONString(people));
        return peopleServiceApi.queryPeople(people);
    }

    /**
     * 保存
     *
     * @param request
     * @return
     */
    @RequestMapping("/savePeople")
    public Result<People> savePeople(HttpServletRequest request, @RequestBody People people) {
        log.info("接口请求参数：{}", JSON.toJSONString(people));
        people.setCreateTime(new Date());
        return peopleServiceApi.savePeople(people);
    }

    public static void main(String[] args) {

        for (PeopleEnum value : PeopleEnum.values()) {
            System.out.println(value.getDesc());
        }

        ArrayList<String> list = Lists.newArrayList("as", "df", "vf");
        String s = JSON.toJSONString(list);
        System.out.println(s);
        Date date = new Date("Tue Jan 20 03:00:00 CST 1970");
        String format = DateUtil.format(date, "yyyy-MM-dd hh:mm:ss");
        System.out.println(format);

        String date1 = "2021-06-14 00:00:00";
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        try {
            Date parse = format1.parse(date1);
            System.out.println(parse.getTime());
            System.out.println(parse);
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }
}
