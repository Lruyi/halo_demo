package com.example.halo.demo.service;

import cn.hutool.json.JSONUtil;
import com.alibaba.druid.support.json.JSONUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.halo.demo.annotation.IfApiLogger;
import com.example.halo.demo.api.PeopleServiceApi;
import com.example.halo.demo.common.Result;
import com.example.halo.demo.constant.RegexConstants;
import com.example.halo.demo.entity.People;
import com.example.halo.demo.mapper.PeopleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Description:
 * @Author: Halo_ry
 * @Date: 2020/4/6 16:14
 */
@Service
public class PeopleService extends ServiceImpl<PeopleMapper, People> implements PeopleServiceApi {

    /**
     * k-v保存字符串的
     */
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private PeopleMapper peopleMapper;

    @Override
    @IfApiLogger(apiType = 6)
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public Result<People> queryPeople(People people) {
        Object obj = redisTemplate.opsForValue().get(people.getName() + RegexConstants.UNDERLINE + people.getId());
        if (obj != null){
            People bean = JSONUtil.toBean((String) obj, People.class);
            return Result.getSuccess(bean, "查询完毕");
        }
        People result = peopleMapper.queryPeople(people);
        if (result != null) {
            // 默认序列化方式是JdkSerializationRedisSerializer，需要改变的话，我们就自定义一些Redis的配置，RedisConfig
            redisTemplate.opsForValue().set(result.getName() + RegexConstants.UNDERLINE + result.getId(), JSON.toJSONString(result));
//            stringRedisTemplate.opsForValue().set(result.getName() + RegexConstants.UNDERLINE + result.getId() , JSON.toJSONString(result));
        }
        return Result.getSuccess(result, "查询完毕");
    }


    @Override
    public Result<People> savePeople(People people) {
        Result<People> result;
        int num = peopleMapper.savePeople(people);
        if (num != 0) {
            redisTemplate.opsForValue().set(people.getName() + RegexConstants.UNDERLINE + people.getId(), JSON.toJSONString(people));
            result = Result.getSuccess(people, "SUCCESS", num);
        } else {
            result = Result.getFail("FAIL");
        }
        return result;
    }
}
