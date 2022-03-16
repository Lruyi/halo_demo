package com.halo.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.google.common.collect.Lists;
import com.halo.api.PeopleServiceApi;
import com.halo.common.Result;
import com.halo.entity.Greeting;
import com.halo.entity.People;
import com.halo.enums.PeopleEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

/**
 * @Description:
 * @Author: Halo_ry
 * @Date: 2020/3/13 22:43
 */
@RestController
@RequestMapping("/halo")
@Slf4j
public class GreetingController {
    public static final String TEMPLATE = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();

    @Autowired
    private PeopleServiceApi peopleServiceApi;


    @GetMapping("/hello")
    public String hello(@RequestParam(value = "name", defaultValue = "World") String name) {
        log.info("这是一个get请求。。。。");
        return String.format("hello %s!", name);
    }

//    @Cacheable
    @RequestMapping("/greeting")
    public Greeting greeting(@RequestParam(value = "name", defaultValue = "World") String name) {
        log.info("页面请求参数：{}", JSON.toJSONString(name));
        People people = new People();
        people.setId(2);
        List<People> list = peopleServiceApi.list(new LambdaQueryWrapper<People>().eq(People::getId, people.getId()));
        Result<People> result = peopleServiceApi.queryPeopleCache(people);
        return new Greeting(counter.incrementAndGet(), String.format(TEMPLATE, name));
    }

    /**
     * 获取对应的人信息(走缓存)
     *
     * @param people
     * @return
     */
    @RequestMapping("/queryPeople")
    public Result<People> queryPeople(@RequestBody @Validated People people) {
        log.info("获取对应的人信息(走缓存)接口请求参数：{}", JSON.toJSONString(people));
        if (people.isFlag()) {
            return peopleServiceApi.queryPeopleCache(people);
        }
        return peopleServiceApi.queryPeople(people);
    }

    /**
     * 查询所有的人员消息（不走缓存）
     * @return
     */
    @GetMapping("/queryPeopleList")
    public Result<List<People>> queryPeopleList() {
        log.info("接口请求查询所有的人员消息（不走缓存）");
//        List<People> list = peopleServiceApi.queryPeopleList();
        List<People> list = peopleServiceApi.list();
        return Result.getSuccess(list);
    }

    /**
     * 保存
     *
     * @param people
     * @return
     */
    @RequestMapping("/savePeople")
    public Result<People> savePeople(@RequestBody People people) {
        log.info("接口请求参数：{}", JSON.toJSONString(people));
        people.setCreateTime(new Date());
        return peopleServiceApi.savePeople(people);
    }

    public static void main(String[] args) throws ParseException {

        String startTime = "20220101";
        String endTime = "20220402";
        DateTime startDate = DateUtil.parse(startTime, "yyyyMMdd");
        DateTime endDate = DateUtil.parse(endTime, "yyyyMMdd");
        DateTime dateTime = DateUtil.offsetMonth(startDate, 3);
        long l = DateUtil.betweenDay(startDate, endDate, false);
        if (endDate.after(dateTime)) {
            System.out.println("超出了三个月了");
        }


        Date now1 = new Date();

        long ddd = 1643509759833L;

        long l1 = now1.getTime() + ddd;

        Date date = new Date(1643509759833L);
        Date endConvertDate = DateUtils.truncate(date, Calendar.HOUR_OF_DAY);
        date = DateUtils.addDays(date, 0 - 30);

//        Date endConvertDate = DateUtils.truncate(now, Calendar.HOUR_OF_DAY);


        String formate = "%s#%s";
        System.out.println(String.format(formate, 2, 4));// 2#4


        Integer registType = 1;
        System.out.println(!Objects.equals(1, registType));


        System.out.println(System.currentTimeMillis());

        int a = 100;
        long b = 100L;
        System.out.println(a==b);
        System.out.println(Objects.equals(a, b));

        // 获取消息重试sign
        System.out.println(System.currentTimeMillis() - 3600000);
        

        List<String> curriculumnIds = Lists.newArrayList("5154804da45040ed87fdd27b5ef270a2", "0422671cb8904e70b1dc8198189c664e");
        System.out.println(JSON.toJSONString(curriculumnIds));


        String date11 = "2022-02-22 11:03:00";
        Date parse1 = DateUtil.parse(date11, "yyyy-MM-dd HH:mm:ss");

        System.out.println(parse1.getTime());

        System.out.println(System.currentTimeMillis());


        List<People> peopleList = new ArrayList<>();
        People people2 = new People();
        people2.setId(1);
        people2.setAge(12);
        people2.setType(2);
        people2.setName("nihao");
        people2.setCreateTime(DateUtils.addHours(new Date(), 1));
        peopleList.add(people2);
        People people = new People();
        people.setId(1);
        people.setAge(18);
        people.setType(1);
        people.setName("halo");
        people.setCreateTime(DateUtils.addHours(new Date(), -1));
        peopleList.add(people);
        People people1 = new People();
        people1.setId(1);
        people1.setAge(15);
        people1.setType(3);
        people1.setName("mdd");
        people1.setCreateTime(new Date());
        peopleList.add(people1);

        peopleList.forEach(people3 -> {
            if (people3.getAge() >= 15 ) {
                return;// 相当于continue
            }
            System.out.println(people3.getName() + "的年龄：" + people3.getAge());
        });
        // 升序
        List<People> collect1 = peopleList.stream().sorted(new Comparator<People>() {
            @Override
            public int compare(People o1, People o2) {
                return o1.getCreateTime().compareTo(o2.getCreateTime());
            }
        }).collect(Collectors.toList());
        // 降序
        List<People> collect2 = peopleList.stream().sorted(new Comparator<People>() {
            @Override
            public int compare(People o1, People o2) {
                return o2.getCreateTime().compareTo(o1.getCreateTime());
            }
        }).collect(Collectors.toList());
        for (People people3 : peopleList) {
            if (people3.getAge() <= 15) {
                break;
            }
        }
        peopleList.forEach(people3 -> {
            if (people3.getAge() >= 15 ) {
                return;// 相当于continue
            }
            System.out.println(people3.getName() + "的年龄：" + people3.getAge());
        });

        peopleList = peopleList.stream().filter(people11 -> !Objects.equals(people11.getId(), 1)).collect(Collectors.toList());

        // 默认升序
        peopleList.sort(Comparator.comparing(People::getCreateTime));
        System.out.println(JSON.toJSONString(peopleList));
        // people属性转map
        List<Map> maps = BeanUtil.copyToList(peopleList, Map.class);

//        peopleList.forEach(people11 -> {
//            Map<String, Object> map = new HashMap<String, Object>();
//            Map<String, Object> stringObjectMap = BeanUtil.beanToMap(people11);
//            BeanUtil.copyProperties(people11, map);
////            BeanUtils.copyProperties(people11, map);
//            System.out.println(1111);
//        });





        String replayNums = "1,a";
        String[] split = replayNums.trim().split(StringPool.COMMA);
        List<Integer> collect = Arrays.stream(split).filter(NumberUtils::isCreatable).map(Integer::parseInt).collect(Collectors.toList());
        if (Objects.equals(collect.size(), split.length)) {
            System.out.println("满足");
        } else {
            System.out.println("存在非法字符");
        }

        for (PeopleEnum value : PeopleEnum.values()) {
            System.out.println(value.getDesc());
        }

        Date date1 = new Date("Tue Jan 20 03:00:00 CST 1970");
        String format = DateUtil.format(date, "yyyy-MM-dd hh:mm:ss");
        System.out.println(format);

        String date2 = "2021-06-14 00:00:00";
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        try {
            Date parse = format1.parse(date2);
            System.out.println(parse.getTime());
            System.out.println(parse);
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }
}
