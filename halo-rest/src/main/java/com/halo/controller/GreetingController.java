package com.halo.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.ConcurrentHashSet;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.lang.UUID;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.TypeReference;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.halo.api.PeopleServiceApi;
import com.halo.common.Result;
import com.halo.config.HaloConfig;
import com.halo.dto.PeopleDTO;
import com.halo.entity.Greeting;
import com.halo.entity.People;
import com.halo.enums.ArrangeRuleTypeEnum;
import com.halo.enums.PeopleEnum;
import com.halo.enums.PriceTypeEnum;
import com.halo.exception.BizException;
import com.halo.req.ParseTheWeekReq;
import com.halo.resp.PyClassInfoResp;
import com.halo.utils.MathUtil;
import com.halo.vo.ClassVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.constraints.NotBlank;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.time.temporal.TemporalAdjusters.*;

/**
 * @Description:
 * @Author: Halo_ry
 * @Date: 2020/3/13 22:43
 */
@RestController
@RequestMapping("/halo")
@Slf4j
//@Validated  类上
public class GreetingController {
    public static final String TEMPLATE = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();

    @Autowired
    private PeopleServiceApi peopleServiceApi;

    @Autowired
    private HaloConfig haloConfig;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    public static final String KEY = "mt:lesson:businessBelong";

    public static final ConcurrentHashSet HASH_SET = new ConcurrentHashSet();


    @GetMapping("/hello")
    public String hello(@RequestParam(value = "name", defaultValue = "World") String name) {
        log.info("这是一个get请求。。。。");
        redisTemplate.opsForSet().add(KEY, name);
        redisTemplate.expire(KEY, 10, TimeUnit.MILLISECONDS);
        Set<Object> members = redisTemplate.opsForSet().members(KEY);
        String collect = members.stream().map(Objects::toString).collect(Collectors.joining(","));
        return String.format("hello %s!", collect);
    }

//    @Cacheable
    @GetMapping("/greeting")
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
     * 添加 @Validated 走校验逻辑
     * @param people
     * @return
     */
    @PostMapping("/queryPeople")
    public Result<People> queryPeople(@RequestBody @Validated People people) {
        log.info("获取对应的人信息(走缓存)接口请求参数：{}", JSON.toJSONString(people));
        String beginTime = haloConfig.getBeginTime();
        String endTime = haloConfig.getEndTime();
        Set<String> businessBelong = haloConfig.getBusinessBelong();
        System.out.println(beginTime);
        System.out.println(endTime);


        if (people.isFlag()) {
            return peopleServiceApi.queryPeopleCache(people);
        }
        return peopleServiceApi.queryPeople(people);
    }

    /**
     * 测试jmeter读取CSV文件，调用post接口处理数据
     * @param classVO
     * @return
     */
    @PostMapping("importClassIds")
    public Result<List<String>> importClassIds(@RequestBody ClassVO classVO) {
        if (CollectionUtils.isNotEmpty(classVO.getClassId())) {
            System.out.println(JSON.toJSONString(classVO.getClassId()));
        }
        return Result.getSuccess();
    }

    /**
     * 测试jmeter读取CSV文件，调用get接口处理数据
     * @param classId
     * @return
     */
    @GetMapping(value= "importClassId")
    public Result<String> importClassIds(@RequestParam String classId) {
        if (StringUtils.isNotBlank(classId)) {
            System.out.println("classId:"+ classId);
        }
        return Result.getSuccess(classId);
    }

    /**
     * 测试postman读取CSV文件，调用get接口处理数据
     * @param
     * @return
     */
    @GetMapping(value= "importPeople")
    public Result<String> importClassIds(@RequestParam String username, @RequestParam Integer age) {
        System.out.println("username:" + username + " -- age:" + age);
        return Result.getSuccess("username:" + username + " -- age:" + age);
    }

    /**
     * 测试postman读取CSV文件，调用post接口处理数据
     *
     * @param peopleDTO
     * @return
     */
    @PostMapping("importPeoples")
    public Result<List<String>> importClassIds(@RequestBody PeopleDTO peopleDTO) {
        System.out.println(JSON.toJSONString(peopleDTO));
        return Result.getSuccess(JSON.toJSONString(peopleDTO));
    }

    /**
     * 根据ID物理删除一条记录
     * @param id
     * @return
     */
    @GetMapping("/deletedPeopleById")
    public Result<Object> deletedPeopleById(@RequestParam("id") @NotBlank(message = "id不能为空") Integer id) {
        boolean flag = peopleServiceApi.removeById(id);
        if (!flag) {
            return Result.getFail("删除失败");
        }
        return Result.getSuccess(flag, "删除成功");
    }

    @PostMapping("/queryTest")
    public Result<Object> queryTest () {
        log.info("接口请求查询所有的人员消息（不走缓存）");
        return Result.getSuccess("SUCCESS");
    }

    /**
     * 查询所有的人员消息（不走缓存）
     * @return
     */
    @GetMapping("/queryPeopleList")
    public Result<List<People>> queryPeopleList() {
        log.info("接口请求查询所有的人员消息（不走缓存）");
//        List<People> list = peopleServiceApi.queryPeopleList();
        long minId = 0;
//        AtomicBoolean flag = new AtomicBoolean(false);
        boolean flag = false;
        if (minId == 0) {
            // 获取最小ID值
            QueryWrapper<People> queryWrapper = new QueryWrapper<>();
            QueryWrapper<People> select = queryWrapper.select(" MIN(id) AS id");
            People one = peopleServiceApi.getOne(select);
            if (Objects.nonNull(one)) {
                minId = one.getId() - 1;
            }
        }
        do {
            List<People> list = peopleServiceApi.list(new LambdaQueryWrapper<People>()
                    .select(People::getId, People::getAge, People::getName)
                    .gt(People::getId, minId)
                    .between(People::getCreateTime, "2021-07-07 19:00:00", "2021-07-07 20:20:00")
                    .orderByAsc(People::getId)
                    .last(" LIMIT 2")
            );
            if (CollectionUtils.isEmpty(list)) {
                return null;
            }
            flag = true;
            Integer maxId = list.get(list.size() - 1).getId();
            minId = maxId;
            log.info("每次的最大ID作为下次分页查询的最小ID，minId:{}", maxId);
        } while (flag);




        List<People> list = peopleServiceApi.list();
        return Result.getSuccess(list);
    }

    /**
     * 保存
     *
     * @param people
     * @return
     */
    @PostMapping("/savePeople")
    public Result<People> savePeople(@RequestBody People people) {
        log.info("接口请求参数：{}", JSON.toJSONString(people));
        people.setCreateTime(new Date());
//        peopleServiceApi.save(people);
//        System.out.println(people.getId());
        boolean flag = peopleServiceApi.save(people);// 可以返回主键
        return Result.getSuccess(people);
    }

    /**
     * 保存
     *
     * @param peopleList
     * @return
     */
    @PostMapping("/saveBatchPeople")
    public Result<List<People>> savePeople(@RequestBody List<People> peopleList) {
        log.info("接口请求参数：{}", JSON.toJSONString(peopleList));
        peopleList.forEach(people -> people.setCreateTime(new Date()));
        boolean flag = peopleServiceApi.saveBatch(peopleList);// 可以批量返回主键
        return Result.getSuccess(peopleList);
    }

    @PostMapping("updatePeopleById")
    public Result<People> updatePeople(@RequestBody People people) {
        boolean flag = false;
        peopleServiceApi.update(new LambdaUpdateWrapper<People>()
                .set(!flag, People::getAge, people.getAge())
                .set(People::getType, people.getType())

                .eq(People::getId, people.getId()));
        return Result.getSuccess(people);
    }

    /**
     * 后置排课按照解锁规则解析日期
     *  第一讲按缴费日期 00:00:00开始上课，第二讲从缴费日期后按规则计算上课日期
     * @param parseTheWeekReq
     * @return
     */
    @PostMapping("/parseTheWeek")
    public Result<List<Date>> parseTheWeek(@RequestBody ParseTheWeekReq parseTheWeekReq) {

        // 排课类型 1-前置排课  2-后置排课
        Integer arrangeType = parseTheWeekReq.getArrangeType();
        // 要解析的课次号
        List<Integer> curriculumNos = parseTheWeekReq.getCurriculumNos();
        // 缴费时间
        String payDateStr = parseTheWeekReq.getPayDate();
        DateTime payDate = DateUtil.parse(payDateStr, "yyyy-MM-dd");
        /**
         * 解锁规则 1-每n日解锁1课次  2-每周x解锁1课次（可多选）
         * {"arrangeRuleType" : 1, "arrangeRuleContent": ["2"]}
         * {"arrangeRuleType" : 2, "arrangeRuleContent": ["1","2","5"]}
         * {"arrangeRuleType" : 3, "arrangeRuleContent": []}
         */
        Integer arrangeRuleType = parseTheWeekReq.getArrangeRuleType();
        ArrangeRuleTypeEnum arrangeRuleTypeEnum = ArrangeRuleTypeEnum.valueOf(arrangeRuleType);
        List<String> arrangeRuleContent = parseTheWeekReq.getArrangeRuleContent();
        // 前置排课不解析
        if (Objects.equals(arrangeType, 1)) {
            return null;
        }
        List<Date> dateList;
        //
        switch (arrangeRuleTypeEnum) {
            case DAYS:
                // 每n日解锁1课次
//                dateList = unlockForDay(arrangeRuleContent, curriculumNos.size(), payDate);
                dateList = unlockForDay1(arrangeRuleContent, curriculumNos.size(), payDate);
                break;
            case DAY_OF_WEEK:
                // 每周x解锁1课次
//                dateList = unlockForWeek(arrangeRuleContent, curriculumNos.size(), payDate);
                dateList = unlockForWeek1(arrangeRuleContent, curriculumNos.size(), payDate);
                break;
            case UNLOCK_ALL:
                // 一次性解锁
//                dateList = unlockForAll(curriculumNos.size(), payDate);
                dateList = unlockForAll1(curriculumNos.size(), payDate);
                break;
            default:
                throw new BizException("无此类解锁规则");
        }

        return Result.getSuccess(dateList);
    }

    /**
     * 每n日解锁1课次    "arrangeRuleContent": ["2"]
     *
     * @param arrangeRuleContent
     * @param curriculumCount
     * @return
     */
    private List<Date> unlockForDay(List<String> arrangeRuleContent, int curriculumCount, Date payDate) {
        List<Date> dateList = new ArrayList<>();
        ZoneId zoneId = ZoneId.systemDefault();

        // 第一讲时间按缴费时间00:00:00上课
        LocalDate localDate = payDate.toInstant().atZone(zoneId).toLocalDate();
        LocalTime localTime = LocalTime.of(0, 0, 0);
        LocalDateTime localDateTimeFrist = LocalDateTime.of(localDate, localTime);
        dateList.add(Date.from(localDateTimeFrist.atZone(zoneId).toInstant()));

        // 第二讲从缴费日期后按规则计算上课日期
        for (int i = 1; i < curriculumCount; localDateTimeFrist = localDateTimeFrist.plus(Integer.parseInt(arrangeRuleContent.get(0)), ChronoUnit.DAYS)) {
            LocalDateTime localDateTime = localDateTimeFrist.plus(Integer.parseInt(arrangeRuleContent.get(0)), ChronoUnit.DAYS);
            dateList.add(Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant()));
            i++;
        }
        return dateList;
    }


    /**
     * 每n日解锁1课次    "arrangeRuleContent": ["2"]
     *
     * @param arrangeRuleContent
     * @param curriculumCount
     * @return
     */
    private List<Date> unlockForDay1(List<String> arrangeRuleContent, int curriculumCount, Date payDate) {
        List<Date> dateList = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();

        // 第一讲时间按缴费时间00:00:00上课
        calendar.setTime(payDate);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        dateList.add(calendar.getTime());

        // 第二讲从缴费日期后按规则计算上课日期
        for (int i = 1; i < curriculumCount; i++) {
            calendar.add(Calendar.DAY_OF_MONTH, Integer.parseInt(arrangeRuleContent.get(0)));
            dateList.add(calendar.getTime());
        }
        return dateList;
    }

    /**
     * 每周x解锁1课次   "arrangeRuleContent": ["1","2","5"]
     * @return
     * @param arrangeRuleContent
     * @param curriculumCount
     */
    private List<Date> unlockForWeek(List<String> arrangeRuleContent, int curriculumCount, Date payDate) {
        List<Date> dateList = new ArrayList<>();
        ZoneId zoneId = ZoneId.systemDefault();
        // 第一讲时间按缴费时间00:00:00上课
        LocalDate localDate = payDate.toInstant().atZone(zoneId).toLocalDate();
        LocalTime localTime = LocalTime.of(0, 0, 0);
        LocalDateTime localDateTime = LocalDateTime.of(localDate, localTime);
        dateList.add(Date.from(localDateTime.atZone(zoneId).toInstant()));

        // 第二讲从缴费日期后按规则计算上课日期
        // 拿到每周的哪几天解锁
        List<? extends Comparable<? extends Comparable<?>>> daysOfWeek = arrangeRuleContent.stream().map(date -> {
            switch (date) {
                case "1":
                    return DayOfWeek.MONDAY;
                case "2":
                    return DayOfWeek.TUESDAY;
                case "3":
                    return DayOfWeek.WEDNESDAY;
                case "4":
                    return DayOfWeek.THURSDAY;
                case "5":
                    return DayOfWeek.FRIDAY;
                case "6":
                    return DayOfWeek.SATURDAY;
                case "7":
                    return DayOfWeek.SUNDAY;
                default:
                    return 0;
            }
        }).collect(Collectors.toList());
        // 缴费日期加一天
        LocalDateTime payDatePlusOne = localDateTime.plus(1, ChronoUnit.DAYS);
        for (int i = 1; i < curriculumCount; payDatePlusOne = payDatePlusOne.plus(1, ChronoUnit.DAYS)) {
            if (daysOfWeek.contains(payDatePlusOne.getDayOfWeek())) {
                dateList.add(Date.from(payDatePlusOne.atZone(zoneId).toInstant()));
                i++;
            }
        }
        return dateList;
    }

    /**
     * 每周x解锁1课次   "arrangeRuleContent": ["1","2","5"]
     * @return
     * @param arrangeRuleContent
     * @param curriculumCount
     */
    private List<Date> unlockForWeek1(List<String> arrangeRuleContent, int curriculumCount, Date payDate) {
        List<Date> dateList = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        // 第一讲时间按缴费时间00:00:00上课
        calendar.setTime(payDate);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        dateList.add(calendar.getTime());

        // 第二讲从缴费日期后按规则计算上课日期
        // 拿到每周的哪几天解锁
        List<Integer> daysOfWeek = arrangeRuleContent.stream().map(date -> {
            switch (date) {
                case "1":
                    return Calendar.MONDAY;
                case "2":
                    return Calendar.TUESDAY;
                case "3":
                    return Calendar.WEDNESDAY;
                case "4":
                    return Calendar.THURSDAY;
                case "5":
                    return Calendar.FRIDAY;
                case "6":
                    return Calendar.SATURDAY;
                case "7":
                    return Calendar.SUNDAY;
                default:
                    return 0;
            }
        }).collect(Collectors.toList());

        // 缴费日期加一天
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        for (int i = 1; i < curriculumCount; calendar.add(Calendar.DAY_OF_MONTH, 1)) {
            if (daysOfWeek.contains(calendar.get(Calendar.DAY_OF_WEEK))) {
                dateList.add(calendar.getTime());
                i++;
            }
        }
        return dateList;
    }

    /**
     * 一次性解锁全部(按缴费日期 00:00:00 上课)
     * @param curriculumCount
     * @return
     */
    private List<Date> unlockForAll(int curriculumCount, Date payDate) {
        List<Date> dateList = new ArrayList<>();

        LocalTime localTime = LocalTime.of(0,0,0);
        ZoneId zoneId = ZoneId.systemDefault();
        LocalDate localDate = payDate.toInstant().atZone(zoneId).toLocalDate();
        LocalDateTime localDateTime = LocalDateTime.of(localDate, localTime);

        for (int i = 0; i < curriculumCount; i++) {
            dateList.add(Date.from(localDateTime.atZone(zoneId).toInstant()));
        }
        return dateList;
    }

    /**
     * 一次性解锁全部(按缴费日期 00:00:00 上课)
     * @param curriculumCount
     * @return
     */
    private List<Date> unlockForAll1(int curriculumCount, Date payDate) {
        List<Date> dateList = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        for (int i = 0; i < curriculumCount; i++) {
            calendar.setTime(payDate);
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MONDAY, 0);
            calendar.set(Calendar.SECOND, 0);
            dateList.add(calendar.getTime());
        }
        return dateList;
    }

    /**
     * 计算每个课次的金额
     * @param classPrice 去掉报名费后的班级总金额
     * @param totalCucNo 班级课次总数
     * @return
     */
    public Map<Integer, Integer> calculationCucPrice(PriceTypeEnum priceTypeEnum, double classPrice, Integer totalCucNo) {
        Map<Integer, Integer> cucMap = new HashMap<>(totalCucNo);
        // 按总金额均摊
        if (Objects.equals(priceTypeEnum, PriceTypeEnum.COURSE)) {
            int averagePrice = (int) (classPrice / totalCucNo);
            int remainingPrice = (int) (classPrice % totalCucNo);

            int[] cucNoArr = new int[totalCucNo];
            Arrays.fill(cucNoArr, averagePrice);
            Arrays.sort(cucNoArr);

            int[] myList = new int[totalCucNo];
            Arrays.fill(myList, averagePrice);

            for (int i = cucNoArr.length; i >= 0; i--) {
                int value = cucNoArr[i];
                if (remainingPrice >= 1) {
                    cucMap.put(i, value + 1);
                    remainingPrice--;
                } else {
                    cucMap.put(i, value);
                }
            }
            return cucMap;
        }
        return Maps.newHashMap();
    }

    /**
     * 按时间段拆分
     * @param startDateStr 开始时间
     * @param endDateStr 结束时间
     * @return 日期集合
     */
    public static List<LocalDate> splitDateRange(String startDateStr, String endDateStr) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate startDate = LocalDate.parse(startDateStr, formatter);
        LocalDate endDate = LocalDate.parse(endDateStr, formatter);
        List<LocalDate> dates = new ArrayList<>();
        for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
            dates.add(date);
        }
        return dates;
    }




    public static void main(String[] args) throws ParseException {
        // 按时间段
        List<LocalDate> dates = splitDateRange("2023-02-26", "2023-03-05");

        String startDateStr = "20200101";
        String endDateStr = "20200131";
        // 判断两个日期段是否大于7天
        long betweenDay = DateUtil.betweenDay(DateUtil.parse(startDateStr, "yyyyMMdd"), DateUtil.parse(endDateStr, "yyyyMMdd"), false);
        if (betweenDay > 7) {
            System.out.println("日期段大于7天");
        }
        // 转成yyyy-MM-dd HH:mm:ss
        Date beginOfDay = DateUtil.beginOfDay(DateUtil.parse(startDateStr, "yyyyMMdd"));// 2020-01-01 00:00:00
        Date endOfDay = DateUtil.endOfDay(DateUtil.parse(endDateStr, "yyyyMMdd"));// 2020-01-31 23:59:59


        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC")); // 设置时区为 UTC
        String dateString = dateFormat.format(new Date()); // 获取当前时间并转换为指定格式的字符串
        System.out.println(dateString); // 输出转换后的字符串

        String input = "[] 重新处理消息失败\n" +
                "com.xes.mplat.trade.lesson.exception.BaseException: 未查询到调课目标班课表,classId:6c6fa1a75370439ab35634966593daab，registId:68ef0464562943a4828f50e799abf83e\n" +
                "    at com.xes.mplat.trade.lesson.processor.studentclass.ChangeCourseStudentClassAggregationProcessor.process(ChangeCourseStudentClassAggrega";
        String patternString = "[\\da-fA-F]{32}";

        Pattern pattern = Pattern.compile(patternString);
        Matcher matcher = pattern.matcher(input);

        StringBuffer output = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(output, "");
        }
        matcher.appendTail(output);

        System.out.println(output.toString().length() >=100 ? output.toString().substring(0, 100) : output.toString());

        if (matcher.matches()) {
            System.out.println("Match found: " + matcher.group(0));
        } else {
            System.out.println("Match not found");
        }


        // MD5算法签名  sign = md5(taskid + "&" + timestamp + token)
        String sign = DigestUtils.md5Hex("278&1592285175dd98e713eee47c65db528e6e0e3c08bc1c7eb867");
        long timesss = System.currentTimeMillis() / 1000;
        String sign1 = DigestUtils.md5Hex("1335871&1686204796" + "4069405402f4e3b2762dd2a28ff3de1942cbfae3");
        // 按时间段
        List<LocalDate> dates = splitDateRange("2023-02-26", "2023-03-05");

        AtomicInteger skip = new AtomicInteger(0);
        System.out.println(skip.addAndGet(1));
        System.out.println(skip.getAndAdd(1));

        double priceTotal = 0D;
        double currentPrice = 0D;
        double overFlowPrice = 0D;
        for (int i = 0; i < 5; i++) {
            double price = 9D;
            double stageAmount = (double) i * 3;
            priceTotal += price;
            currentPrice += stageAmount;
            overFlowPrice += priceTotal - currentPrice;
            priceTotal -= currentPrice;
        }
        for (int i = 0; i < 5; i++) {
            double price = 9D;
            double stageAmount = (double) i * 3;
            priceTotal = MathUtil.add(priceTotal, price);
            currentPrice = MathUtil.add(currentPrice, stageAmount);
            overFlowPrice = MathUtil.add(overFlowPrice, MathUtil.sub(priceTotal, currentPrice));
            priceTotal = MathUtil.sub(priceTotal, currentPrice);
        }

        System.out.println("priceTotal:"+ priceTotal);
        System.out.println("currentPrice:"+ currentPrice);
        System.out.println("overFlowPrice:"+ overFlowPrice);


        System.out.println(63 & 63);

        double price = 5.0D;

        System.out.println(price * 1/3);

        System.out.println(1/3);
        double saadsdfdsaf = (double) 1/3;
        System.out.println(saadsdfdsaf);


        ArrayList<Integer> integerArrayList = Lists.newArrayList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        int passedCount = 4;
        List<Integer> collect5 = integerArrayList.stream().skip(passedCount).limit(0).collect(Collectors.toList());


        Map<String, String> mmmmmp = new HashMap<>();
        mmmmmp.put("ni", "hao");
        String wo = mmmmmp.get("wo");


        Date date20 = new Date(1628574552000L);
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println(sf.format(date20));


        // 字符串转Integer类型list
        String ids = new String("11,333,44");
        String[] items = ids.split(",");
        List<Integer> idList = Stream.of(items).map(Integer::parseInt).collect(Collectors.toList());

        System.out.println(System.currentTimeMillis());

        String s = UUID.randomUUID().toString();
        System.out.println(UUID.fastUUID().toString().replaceAll("-",""));
        System.out.println(UUID.randomUUID().toString().replaceAll("-",""));
        SimpleDateFormat sfdd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String ddddddd = "2023-04-28 09:30:00";
        Date endTime1 = sfdd.parse(ddddddd);
        System.out.println(endTime1.getTime());
        LocalDateTime endLocalDateTime = DateUtil.toLocalDateTime(endTime1);
        long delaySeconds = LocalDateTimeUtil.between(LocalDateTime.now(), endLocalDateTime, ChronoUnit.SECONDS);

        System.out.println(endTime1.compareTo(new Date())); // >1
        System.out.println(endTime1.after(new Date())); // true
        System.out.println(endTime1.compareTo(endTime1)); // =0
        System.out.println(!endTime1.before(endTime1)); // true

        LocalDateTime of1 = LocalDateTime.of(2022, 05, 29, 20, 29, 40);
        LocalDate localDate = of1.toLocalDate();
        LocalTime localTime = of1.toLocalTime();
        // 获取今天日期
        LocalDate now = LocalDate.now();
        // 获取今天是星期几 MONDAY：1  WEDNESDAY：3  SUNDAY：7
        DayOfWeek dayOfWeek = now.getDayOfWeek();
        LocalDate localDate1 = now.with(nextOrSame(DayOfWeek.SUNDAY));
        LocalDate localDate2 = now.with(next(DayOfWeek.SUNDAY));
        LocalDate localDate3 = now.with(nextOrSame(DayOfWeek.SATURDAY));
        LocalDate localDate4 = now.with(next(DayOfWeek.SATURDAY));


        int year1 = now.get(ChronoField.YEAR);
        int month1 = now.get(ChronoField.MONTH_OF_YEAR);
        int day1 = now.get(ChronoField.DAY_OF_MONTH);
        // 星期几对应的数值，周一：1，周三：3
        int weekNum = now.get(ChronoField.DAY_OF_WEEK);

        LocalDate date12 = LocalDate.of(2022, 05, 29);
        int year = date12.getYear();
        Month month = date12.getMonth();
        int day = date12.getDayOfMonth();
        DayOfWeek dow = date12.getDayOfWeek();
        int len = date12.lengthOfMonth();
        boolean leap = date12.isLeapYear();

        LocalTime time = LocalTime.of(13, 45, 20);
        int hour = time.getHour();
        int minute = time.getMinute();
        int second = time.getSecond();

        LocalDate date111 = LocalDate.parse("2014-03-18");
        LocalTime time1 = LocalTime.parse("13:45:20");

        // "2014-03-18"
        LocalDate date1 = LocalDate.of(2014, 3, 18);
        //修改年 "2011-03-18"
        LocalDate date2 = date1.withYear(2011);
        //修改日 "2011-03-25"
        LocalDate date3 = date2.withDayOfMonth(25);
        //修改月 "2011-09-25"
        LocalDate date4 = date3.with(ChronoField.MONTH_OF_YEAR, 9);

        // "2014-03-18"
        LocalDate date11 = LocalDate.of(2014, 3, 18);
        //加1周 "2014-03-25"
        LocalDate date22 = date11.plusWeeks(1);
        // 减3年 "2011-03-25"
        LocalDate date33 = date22.minusYears(3);
        // 加6个月 "2011-09-25"
        LocalDate date44 = date33.plus(6, ChronoUnit.MONTHS);
        // 加1天  "2011-09-26"
        LocalDate date55 = date44.plus(1, ChronoUnit.DAYS);


        // "2022-05-29"
        LocalDate date7 = LocalDate.of(2022, 5, 29);
        // 如果今天是星期日则返回今天日期，否则返回下一周的星期日 "2022-05-29"
        LocalDate localDate5 = date7.with(nextOrSame(DayOfWeek.SUNDAY));
        // 返回下一个星期日 "2022-06-05"
        LocalDate localDate6 = date7.with(next(DayOfWeek.SUNDAY));
        // 如果今天是星期六则返回今天日期，否则返回下一周的星期六 "2022-06-04"
        LocalDate localDate7 = date7.with(nextOrSame(DayOfWeek.SATURDAY));
        // 返回下一个星期六 "2022-06-04"
        LocalDate localDate8 = date7.with(next(DayOfWeek.SATURDAY));

        /**
         * TemporalAdjuster类中的工厂方法
         *
         * dayOfWeekInMonth     创建一个新的日期，它的值为同一个月中每一周的第几天
         * firstDayOfMonth      创建一个新的日期，它的值为当月的第一天
         * firstDayOfNextMonth  创建一个新的日期，它的值为下月的第一天
         * firstDayOfNextYear   创建一个新的日期，它的值为明年的第一天
         * firstDayOfYear       创建一个新的日期，它的值为当年的第一天
         * firstInMonth         创建一个新的日期，它的值为同一个月中，第一个符合星期几要求的值
         * lastDayOfMonth       创建一个新的日期，它的值为下月的最后一天
         * lastDayOfNextMonth   创建一个新的日期，它的值为下月的最后一天
         * lastDayOfNextYear    创建一个新的日期，它的值为明年的最后一天
         * lastDayOfYear        创建一个新的日期，它的值为今年的最后一天
         * lastInMonth          创建一个新的日期，它的值为同一个月中，最后一个符合星期几要求的值
         * next/previous        创建一个新的日期，并将其值设定为日期调整后或者调整前，第一个符合指定星期几要求的日期
         * nextOrSame/previousOrSame    创建一个新的日期，并将其值设定为日期调整后或者调整前，第一个符合指定星 期几要求的日期，
         *                              如果该日期已经符合要求，直接返回该对象
         */
        // "2022-05-29"
        LocalDate date8 = LocalDate.of(2022, 5, 29);
        // 它的值为同一个月中每一周的第几天
        // 代表同一个月中第1周的星期一 "2022-05-02"
        LocalDate localDate9 = date8.with(dayOfWeekInMonth(1, DayOfWeek.MONDAY));
        // 代表同一个月中第2周的星期一  "2022-05-09"
        LocalDate localDate10 = date8.with(dayOfWeekInMonth(2, DayOfWeek.MONDAY));

        // "2022-05-29"
        LocalDate date9 = LocalDate.of(2022, 5, 29);
        // "20220529"
        String s1 = date9.format(DateTimeFormatter.BASIC_ISO_DATE);
        // "2022-05-29"
        String s2 = date9.format(DateTimeFormatter.ISO_LOCAL_DATE);

        // "2022-05-29"
        LocalDate date10 = LocalDate.parse("20220529", DateTimeFormatter.BASIC_ISO_DATE);
        // "2022-05-29"
        LocalDate date13 = LocalDate.parse("2022-05-29", DateTimeFormatter.ISO_LOCAL_DATE);


        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        // "2022-05-29"
        LocalDate date14 = LocalDate.of(2022, 5, 29);
        // "29/05/2022"
        String formattedDate = date14.format(formatter);
        // "2022-05-29"
        LocalDate date15 = LocalDate.parse(formattedDate, formatter);

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String ddddd = "2022-05-31 18:00:00";
        Date parse2 = format.parse(ddddd);
        Date date16 = DateUtils.addMinutes(parse2, 15);
        LocalDateTime localDateTime = DateUtil.toLocalDateTime(date16);
        // 判断两个时间差值
        long between = LocalDateTimeUtil.between(LocalDateTime.now(), localDateTime, ChronoUnit.SECONDS);


        Calendar calendar1 = Calendar.getInstance();
        // 设置指定日期
        calendar1.setTime(new Date());
        // 加一天
        calendar1.add(Calendar.DAY_OF_MONTH, 1);
        // 获取加一天后的时间
        Date time2 = calendar1.getTime();
        int i = calendar1.get(Calendar.DAY_OF_WEEK);

        // Date 转 LocalDateTime
        LocalDateTime localDateTime1 = new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        // LocalDateTime 转 Date
        Date date17 = Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant());
        // 获取一天的开始时间
        LocalDateTime localDateTime2 = LocalDateTimeUtil.beginOfDay(LocalDateTime.now());

        Date startTime2, endTime2;
        String time11 = "2022-06-01";
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
        Date date18 = sdf1.parse(time11);
        LocalDateTime localDateTimeMin = DateUtil.toLocalDateTime(date18).with(LocalTime.MIN);
        LocalDateTime localDateTimeMax = DateUtil.toLocalDateTime(date18).with(LocalTime.MAX);
        startTime2 = Date.from(localDateTimeMin.atZone(ZoneId.systemDefault()).toInstant());
        endTime2 = Date.from(localDateTimeMax.atZone(ZoneId.systemDefault()).toInstant());
        SimpleDateFormat sdf13 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        // "2022-06-01 00:00:00"
        System.out.println(sdf13.format(startTime2));
        // "2022-06-01 23:59:59"
        System.out.println(sdf13.format(endTime2));

        // 前一天 00:00:00
        LocalDateTime localDateTime3 = LocalDate.now().minus(1, ChronoUnit.DAYS).atTime(LocalTime.MIN);
        // 前一天 23:59:59
        LocalDateTime localDateTime4 = LocalDate.now().minus(1, ChronoUnit.DAYS).atTime(LocalTime.MAX);


        Set<String> aa = new HashSet<>();// A、B、C、D
//        aa.add("A");
//        aa.add("B");
//        aa.add("C");
//        aa.add("D");
        Set<Object> bb = new HashSet<>();// A、D、M、Q、U
        bb.add("A");
        bb.add("D");
        bb.add("M");
        bb.add("Q");
        bb.add("U");

        System.out.println("aa和bb是否相同：" + CollectionUtils.isEqualCollection(aa, bb));  // aa和bb是否相同：false
        System.out.println("aa和bb去掉重："+CollectionUtils.disjunction(aa, bb));    // aa和bb去掉重：[Q, B, C, U, M]
        System.out.println("aa和bb并集："+CollectionUtils.union(aa, bb));    // aa和bb并集：[A, Q, B, C, D, U, M]
        System.out.println("aa和bb交集：" + CollectionUtils.intersection(aa,bb));    // aa和bb交集：[A, D]
        System.out.println("aa和bb差集：" + CollectionUtils.subtract(aa,bb));    // aa和bb差集：[B, C]
        System.out.println("bb和aa差集：" + CollectionUtils.subtract(bb,aa));    // bb和aa差集：[M, Q, U]
        System.out.println(JSON.toJSONString(CollectionUtils.subtract(aa,bb)));

        HashSet<String> hashSet = new HashSet<>();
        boolean add = hashSet.add("1");
        boolean add1 = hashSet.add("2");
        boolean add2 = hashSet.add("1");

        ArrayList<Integer> integers = Lists.newArrayList(3, 1, 6, 4, 9);
        integers.sort(Comparator.comparing(Integer::intValue));
        String collect3 = integers.stream().map(String::valueOf).collect(Collectors.joining(":"));
        System.out.println(collect3);

        // 对集合按照指定长度分段，每一个段为单独的集合，返回这个集合的列表
        ArrayList<String> listStr = Lists.newArrayList("a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n");
        List<List<String>> split1 = ListUtil.split(listStr, 20);
        for (List<String> list : split1) {
            System.out.println(JSON.toJSONString(list));
        }

        String sjosnfs = "{\"arrangeRuleType\" : 2, \"arrangeRuleContent\": [\"1\",\"2\"]}";
        PyClassInfoResp.ArrangeRule body = JSON.parseObject(sjosnfs, new TypeReference<PyClassInfoResp.ArrangeRule>() {});

        String jssong = "[{\"useLabel\":\"0\",\"tagTransName\":\"自學\",\"courseType\":0,\"gradeId\":\"1\",\"tagId\":\"14\",\"arrangeRule\":\"{\\\\\\\"arrangeRuleType\\\\\\\" : 2, \\\\\\\"arrangeRuleContent\\\\\\\": [\\\\\\\"1\\\\\\\",\\\\\\\"2\\\\\\\"]}\",\"arrangeType\":2,\"cityId\":\"010\",\"tagName\":\"自学\",\"categoryType\":16,\"courseMaxPersons\":200,\"classId\":\"5a90169d446a4c70b7564f37a1a9ded3\",\"id\":\"5a90169d446a4c70b7564f37a1a9ded3\",\"classType\":0,\"productType\":1}]";
        String jssong1 = "{\"useLabel\":\"0\",\"tagTransName\":\"自學\",\"courseType\":0,\"gradeId\":\"1\",\"tagId\":\"14\",\"arrangeRule\":\"{\\\\\\\"arrangeRuleType\\\\\\\" : 2, \\\\\\\"arrangeRuleContent\\\\\\\": [\\\\\\\"1\\\\\\\",\\\\\\\"2\\\\\\\"]}\",\"arrangeType\":2,\"cityId\":\"010\",\"tagName\":\"自学\",\"categoryType\":16,\"courseMaxPersons\":200,\"classId\":\"5a90169d446a4c70b7564f37a1a9ded3\",\"id\":\"5a90169d446a4c70b7564f37a1a9ded3\",\"classType\":0,\"productType\":1}";

        String jsonjson = "{\"useLabel\":\"0\",\"tagTransName\":\"自學\",\"courseType\":0,\"gradeId\":\"1\",\"tagId\":\"14\",\"arrangeRule\":\"{\\\\\\\"arrangeRuleType\\\\\\\" : 2, \\\\\\\"arrangeRuleContent\\\\\\\": [\\\\\\\"1\\\\\\\",\\\\\\\"2\\\\\\\"]}\",\"arrangeType\":2,\"cityId\":\"010\",\"tagName\":\"自学\",\"categoryType\":16,\"courseMaxPersons\":200,\"classId\":\"5a90169d446a4c70b7564f37a1a9ded3\",\"id\":\"5a90169d446a4c70b7564f37a1a9ded3\",\"classType\":0,\"productType\":1}";
//        List<PyClassInfoRsp> pyCurriculumInfoRspList = JSON.parseArray(jsonjson, PyClassInfoRsp.class);


        JSONObject jsonObject1 = JSON.parseObject(jssong1);
        String arrangeRule = jsonObject1.getString("arrangeRule");
        System.out.println(arrangeRule);
//        JSONObject jsonObject2 = JSON.parseObject(arrangeRule);
//        int arrangeRuleType = jsonObject2.getIntValue("arrangeRuleType");
//        JSONArray arrangeRuleContent = jsonObject2.getJSONArray("arrangeRuleContent");
        for (String feild : People.QUERY_FEILD_LIST) {
            System.out.println("feild:" + feild);
        }

        // fastjson2
        String str = "{\"id\":123, \"age\":18}";
        JSONObject jsonObject = JSON.parseObject(str);
        int id = jsonObject.getIntValue("age");
        String str111 = "[\"id\", 123]";
        JSONArray jsonArray = JSON.parseArray(str111);
        String name = jsonArray.getString(0);
        int id1 = jsonArray.getIntValue(1);

        System.out.println(DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss"));

        int random = new Random().nextInt(5);
        System.out.println("随机数：" + random);

        String sssss = "{\"普通\":0,\"公益课\":517,\"诊断课\":515,\"中考班\":520,\"精品课\":256,\"新用户专享\":519,\"小班课\":514,\"盒子课\":513,\"前台随诊\":516,\"第二课堂\":1,\"赠送类课程\":4,\"微课\":128,\"自建课\":518,\"自习室\":521}";
        Map<String, Object> map = JSON.parseObject(sssss, Map.class);


        long startTimeLong = 1648396800000L;

        LocalDateTime of = LocalDateTimeUtil.of(startTimeLong);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar calendar = Calendar.getInstance();
        String startTime1 = "20220412";
        calendar.set(Calendar.DAY_OF_MONTH, 0);
        startTime1 = sdf.format(calendar.getTime());

        LocalDateTime nowDateTime = LocalDateTime.now();
        LocalDateTime plus = nowDateTime.plus(10, ChronoUnit.MINUTES);
        System.out.println(!nowDateTime.isAfter(plus));


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

        long ddd = 1649847194331L;

        long l1 = now1.getTime() + ddd;

        Date date = new Date(1649847194331L);

        if (!now1.before(date)) {
            System.out.println(111111111);
        }

        Date endConvertDate = DateUtils.truncate(date, Calendar.HOUR_OF_DAY);
        Date endConvertDate1 = DateUtils.truncate(date, Calendar.MINUTE);
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


        String date11111 = "2022-03-24 16:56:25";
        String date1111 = "2022-03-24 16:57:27";
        long aadfdf = 4500L;
        Date parse1 = DateUtil.parse(date11111, "yyyy-MM-dd HH:mm:ss");
        Date parse1111 = DateUtil.parse(date1111, "yyyy-MM-dd HH:mm:ss");
        long l2 = parse1.getTime();
        long l4 = l2 + aadfdf;
        long l3 = parse1111.getTime();
        System.out.println(l4 >= l3);

        System.out.println(parse1.getTime());

        System.out.println(System.currentTimeMillis());


        List<People> peopleList = new ArrayList<>();
        People people2 = new People();
        people2.setId(1);
        people2.setAge(12);
        people2.setType(2);
        people2.setName("nihao");
        people2.setCreateTime(DateUtils.addDays(new Date(), 1));
        peopleList.add(people2);
        People people = new People();
        people.setId(1);
        people.setAge(18);
        people.setType(1);
        people.setName("halo");
        people.setCreateTime(DateUtils.addDays(new Date(), -1));
        peopleList.add(people);
        People people1 = new People();
        people1.setId(1);
        people1.setAge(15);
        people1.setType(3);
        people1.setName("mdd");
        people1.setCreateTime(new Date());
        peopleList.add(people1);

        IntSummaryStatistics collect7 = peopleList.stream().collect(Collectors.summarizingInt(People::getAge));
        int max = collect7.getMax();
        int min = collect7.getMin();

        List<Object> collect6 = peopleList.stream().filter(people3 -> people3.getAge() >= 15).map(people3 -> {
            People people4 = new People();
            BeanUtil.copyProperties(people3, people4);
            return people4;
        }).collect(Collectors.toList());

        System.out.println(collect6);


        HashMap<String, List<People>> mapppppp = new HashMap<>();
        mapppppp.put("1", peopleList);
        List<People> peopleList1 = mapppppp.get("2");
        List<Integer> collect4 = peopleList1.stream().map(People::getAge).collect(Collectors.toList());

        peopleList.sort(Comparator.comparing(People::getCreateTime));
        Collections.sort(peopleList, new Comparator<People>() {
            @Override
            public int compare(People o1, People o2) {
                return o1.getCreateTime().compareTo(o2.getCreateTime());
            }
        });

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

        peopleList = peopleList.stream().filter(people11 -> !(Objects.equals(people11.getId(), 1) && Objects.equals(people11.getAge(), 18))).collect(Collectors.toList());

        // 默认升序
        peopleList.sort(Comparator.comparing(People::getCreateTime));
        Collections.sort(peopleList, new Comparator<People>() {
            @Override
            public int compare(People o1, People o2) {
                return o1.getCreateTime().compareTo(o2.getCreateTime());
            }
        });
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

        Date date6 = new Date("Tue Jan 20 03:00:00 CST 1970");
        String format1 = DateUtil.format(date6, "yyyy-MM-dd HH:mm:ss");
        System.out.println(format1);

        String date5 = "2021-06-14 00:00:00";

        String date19 = "2022-10-14 07:30:04";


        SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar startTime11 = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date startDate11 = simpleDateFormat.parse(date19);
        startTime11.setTime(startDate11);
        startTime11.add(Calendar.HOUR, 0);
        startTime11.add(Calendar.MINUTE, 0);
        startTime11.add(Calendar.SECOND, 0);
        Date date21 = startTime11.getTime();
        System.out.println("date21:" + date21);


        try {
            Date parse = format2.parse(date5);
            System.out.println(parse.getTime());
            System.out.println(parse);
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }
}
