package com.halo.utils;

import com.halo.constant.RegexConstants;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.utils.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.*;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Redis工具类
 *
 * @author zhy
 */
@Slf4j
@Component
public class RedisUtil {
    @Getter
    @Autowired(required = false)
    private RedisTemplate<String, Object> redisTemplate;

    public static final int SCAN_COUNT_20 = 20;

    /**
     * 根据key模糊搜索（利用scan游标快速定位）
     *
     * @param pattern 要匹配的key
     * @param count   单次遍历的字典槽位数量
     * @return
     */
    public Object fuzzySearch(String pattern, Long count) {
        List<Object> obj = Lists.newArrayList();
        pattern += RegexConstants.REGEX_ASTERISK;
        ScanOptions options = ScanOptions.scanOptions()
                .count(Objects.nonNull(count) ? count : SCAN_COUNT_20)
                .match(pattern)
                .build();
        Cursor<byte[]> cursor = redisTemplate.executeWithStickyConnection(connection -> connection.scan(options));
        while (Objects.nonNull(cursor) && cursor.hasNext()) {
            String key = new String(cursor.next());
            obj.add(this.get(key));
        }
        return obj;
    }

    /**
     * 指定缓存失效时间
     *
     * @param key  键
     * @param time 时间(秒)
     * @return
     */
    public boolean expire(String key, long time) {
        return expire(key, time, TimeUnit.SECONDS);
    }

    public boolean expire(String key, long time, TimeUnit timeUnit) {
        try {
            if (time > 0) {
                redisTemplate.expire(key, time, timeUnit);
            }
            return true;
        } catch (Exception e) {
            log.error("", e);
            return false;
        }
    }

    /**
     * 根据key 获取过期时间
     *
     * @param key 键 不能为null
     * @return 时间(秒) 返回0代表为永久有效
     */
    public long getExpire(String key) {
        return redisTemplate.getExpire(key, TimeUnit.SECONDS);
    }

    /**
     * 判断key是否存在
     *
     * @param key 键
     * @return true 存在 false不存在
     */
    public boolean hasKey(String key) {
        try {
            return redisTemplate.hasKey(key);
        } catch (Exception e) {
            log.error("", e);
            return false;
        }
    }

    /**
     * 删除缓存
     *
     * @param key 可以传一个值 或多个
     */
    @SuppressWarnings("unchecked")
    public void del(String... key) {
        if (key != null && key.length > 0) {
            if (key.length == 1) {
                redisTemplate.delete(key[0]);
            } else {
                redisTemplate.delete(Arrays.stream(key).collect(Collectors.toList()));
            }
        }
    }

    //============================String=============================

    /**
     * 普通缓存获取
     *
     * @param key 键
     * @return 值
     */
    public Object get(String key) {
        return key == null ? null : redisTemplate.opsForValue().get(key);
    }

    /**
     * 普通缓存批量获取
     *
     * @param keys 键
     * @return 值
     */
    public <T> List<T> multiGet(List<String> keys) {
        return keys == null ? null : (List<T>) redisTemplate.opsForValue().multiGet(keys);
    }

    /**
     * 普通缓存批量获取
     *
     * @param maps   对象
     * @param expire expire 单位s
     * @return 值
     */
    public void multiSet(Map<String, Object> maps, int expire) {
        redisTemplate.executePipelined(new RedisCallback<Object>() {
            @Override
            public Object doInRedis(RedisConnection redisConnection) throws DataAccessException {
                int expireTomils = expire * 1000;
                for (Map.Entry<String, Object> entry : maps.entrySet()) {
                    redisConnection.pSetEx(rawKey(entry.getKey()), expireTomils, rawValue(entry.getValue()));
                }
                return null;
            }
        });
    }

    byte[] rawKey(Object key) {
        Assert.notNull(key, "non null key required");
        RedisSerializer keyRedisSerializer = getRedisSerializer(redisTemplate.getKeySerializer());
        return keyRedisSerializer == null && key instanceof byte[] ? (byte[]) ((byte[]) key) : keyRedisSerializer.serialize(key);
    }

    RedisSerializer getRedisSerializer(RedisSerializer redisSerializer) {
        return redisSerializer;
    }

    byte[] rawValue(Object value) {
        RedisSerializer valueRedisSerializer = getRedisSerializer(redisTemplate.getValueSerializer());
        return valueRedisSerializer == null && value instanceof byte[] ? (byte[]) ((byte[]) value) : valueRedisSerializer.serialize(value);
    }

    @SuppressWarnings("unchecked")
    public <T> T getObj(String key) {
        if (key == null) {
            return null;
        }
        try {
            Object result = redisTemplate.opsForValue().get(key);
            return (T) result;
        } catch (Exception e) {
            log.error("redis getObj error. key:{}", key, e);
            return null;
        }
    }

    /**
     * 普通缓存放入
     *
     * @param key   键
     * @param value 值
     * @return true成功 false失败
     */
    public boolean set(String key, Object value) {
        return set(key, value, 0L, null);
    }

    /**
     * 普通缓存放入并设置时间
     *
     * @param key   键
     * @param value 值
     * @param time  时间(秒) time要大于0 如果time小于等于0 将设置无限期
     * @return true成功 false 失败
     */
    public boolean set(String key, Object value, long time) {
        return set(key, value, time, TimeUnit.SECONDS);
    }

    public boolean set(String key, Object value, long time, TimeUnit timeUnit) {
        try {
            if (time > 0) {
                redisTemplate.opsForValue().set(key, value, time, timeUnit);
            } else {
                redisTemplate.opsForValue().set(key, value);
            }
            return true;
        } catch (Exception e) {
            log.error("", e);
            return false;
        }
    }

    /**
     * 如果key不存在，则放入缓存并返回true，如果key存在则不放入，返回false
     *
     * @param key   键
     * @param value 值
     * @param time  时间(秒) time要大于0 如果time小于等于0 将设置无限期
     * @return
     */
    public boolean setnx(String key, String value, long time) {
        return redisTemplate.opsForValue().setIfAbsent(key, value, time, TimeUnit.SECONDS);
    }

    /**
     * 递增
     *
     * @param key   键
     * @param delta 要增加几(大于0)
     * @return
     */
    public long incr(String key, long delta) {
        if (delta < 0) {
            throw new RuntimeException("递增因子必须大于0");
        }
        return redisTemplate.opsForValue().increment(key, delta);
    }

    /**
     * 递减
     *
     * @param key   键
     * @param delta 要减少几(小于0)
     * @return
     */
    public long decr(String key, long delta) {
        if (delta < 0) {
            throw new RuntimeException("递减因子必须大于0");
        }
        return redisTemplate.opsForValue().increment(key, -delta);
    }

    //================================Map=================================

    /**
     * HashGet
     *
     * @param key  键 不能为null
     * @param item 项 不能为null
     * @return 值
     */
    public Object hget(String key, String item) {
        return redisTemplate.opsForHash().get(key, item);
    }

    public <T> T hgetObj(String key, String item) {
        try {
            return (T) redisTemplate.opsForHash().get(key, item);
        } catch (Exception e) {
            log.error("redis error.", e);
            return null;
        }
    }

    public <T> Set<T> hKeys(String key) {
        return (Set<T>) redisTemplate.opsForHash().keys(key);
    }

    /**
     * 获取hashKey对应的所有键值
     *
     * @param key 键
     * @return 对应的多个键值
     */
    public Map<Object, Object> hmget(String key) {
        return redisTemplate.opsForHash().entries(key);
    }

    /**
     * 获取hashKey对应的所有键值
     *
     * @param key 键
     * @return 对应的多个键值
     */
    public <H, V> List<V> hmget(String key, List<H> keys) {
        Map<Object, Object> result = redisTemplate.opsForHash().entries(key);
        List<V> resultList = Lists.newArrayList();
        if (result != null && result.size() > 0) {
            for (Map.Entry<Object, Object> entry : result.entrySet()) {
                if (keys.contains(entry.getKey())) {
                    resultList.add((V) entry.getValue());
                }
            }
        }
        return resultList;
    }

    /**
     * HashSet
     *
     * @param key 键
     * @param map 对应多个键值
     * @return true 成功 false 失败
     */
    public boolean hmset(String key, Map<String, Object> map) {
        try {
            redisTemplate.opsForHash().putAll(key, map);
            return true;
        } catch (Exception e) {
            log.error("", e);
            return false;
        }
    }

    /**
     * HashSet 并设置时间
     *
     * @param key  键
     * @param map  对应多个键值
     * @param time 时间(秒)
     * @return true成功 false失败
     */
    public boolean hmset(String key, Map<Object, Object> map, long time, TimeUnit timeUnit) {
        try {
            redisTemplate.opsForHash().putAll(key, map);
            if (time > 0) {
                expire(key, time, timeUnit);
            }
            return true;
        } catch (Exception e) {
            log.error("", e);
            return false;
        }
    }

    /**
     * 向一张hash表中放入数据,如果不存在将创建
     *
     * @param key   键
     * @param item  项
     * @param value 值
     * @return true 成功 false失败
     */
    public boolean hset(String key, String item, Object value) {
        try {
            redisTemplate.opsForHash().put(key, item, value);
            return true;
        } catch (Exception e) {
            log.error("", e);
            return false;
        }
    }

    /**
     * 向一张hash表中放入数据,如果不存在将创建
     *
     * @param key   键
     * @param item  项
     * @param value 值
     * @param time  时间(秒)  注意:如果已存在的hash表有时间,这里将会替换原有的时间
     * @return true 成功 false失败
     */
    public boolean hset(String key, String item, Object value, long time) {
        try {
            redisTemplate.opsForHash().put(key, item, value);
            if (time > 0) {
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            log.error("", e);
            return false;
        }
    }

    public <T> boolean hsetObj(String key, String item, T value, long time, TimeUnit timeUnit) {
        try {
            redisTemplate.opsForHash().put(key, item, value);
            if (time > 0) {
                expire(key, time, timeUnit);
            }
            return true;
        } catch (Exception e) {
            log.error("", e);
            return false;
        }
    }

    /**
     * 删除hash表中的值
     *
     * @param key  键 不能为null
     * @param item 项 可以使多个 不能为null
     */
    public void hdel(String key, Object... item) {
        redisTemplate.opsForHash().delete(key, item);
    }

    /**
     * 判断hash表中是否有该项的值
     *
     * @param key  键 不能为null
     * @param item 项 不能为null
     * @return true 存在 false不存在
     */
    public boolean hHasKey(String key, String item) {
        return redisTemplate.opsForHash().hasKey(key, item);
    }

    /**
     * hash递增 如果不存在,就会创建一个 并把新增后的值返回
     *
     * @param key  键
     * @param item 项
     * @param by   要增加几(大于0)
     * @return
     */
    public double hincr(String key, String item, double by) {
        return redisTemplate.opsForHash().increment(key, item, by);
    }

    /**
     * hash递减
     *
     * @param key  键
     * @param item 项
     * @param by   要减少记(小于0)
     * @return
     */
    public double hdecr(String key, String item, double by) {
        return redisTemplate.opsForHash().increment(key, item, -by);
    }

    //============================set=============================

    /**
     * 根据key获取Set中的所有值
     *
     * @param key 键
     * @return
     */
    public Set<Object> sGet(String key) {
        try {
            return redisTemplate.opsForSet().members(key);
        } catch (Exception e) {
            log.error("", e);
            return null;
        }
    }

    /**
     * 根据value从一个set中查询,是否存在
     *
     * @param key   键
     * @param value 值
     * @return true 存在 false不存在
     */
    public boolean sHasKey(String key, Object value) {
        try {
            return redisTemplate.opsForSet().isMember(key, value);
        } catch (Exception e) {
            log.error("", e);
            return false;
        }
    }

    /**
     * 将数据放入set缓存
     *
     * @param key    键
     * @param values 值 可以是多个
     * @return 成功个数
     */
    public long sSet(String key, Object... values) {
        try {
            return redisTemplate.opsForSet().add(key, values);
        } catch (Exception e) {
            log.error("", e);
            return 0;
        }
    }

    public <T> boolean sSet(String key, Collection<T> values, long expire, TimeUnit timeUnit) {
        try {
            redisTemplate.opsForSet().add(key, values.toArray());
            if (expire > 0) {
                expire(key, expire, timeUnit);
            }
            return true;
        } catch (Exception e) {
            log.error("redis error.", e);
            return false;
        }
    }

    /**
     * 将set数据放入缓存
     *
     * @param key    键
     * @param time   时间(秒)
     * @param values 值 可以是多个
     * @return 成功个数
     */
    public long sSetAndTime(String key, long time, Object... values) {
        try {
            Long count = redisTemplate.opsForSet().add(key, values);
            if (time > 0) {
                expire(key, time);
            }
            return count;
        } catch (Exception e) {
            log.error("", e);
            return 0;
        }
    }

    @SuppressWarnings("unchecked")
    public <T> T sSetPop(String key) {
        try {
            return (T) redisTemplate.opsForSet().pop(key);
        } catch (Exception e) {
            log.error("redis error.", e);
            return null;
        }
    }

    /**
     * 获取set缓存的长度
     *
     * @param key 键
     * @return
     */
    public long sGetSetSize(String key) {
        try {
            return redisTemplate.opsForSet().size(key);
        } catch (Exception e) {
            log.error("", e);
            return 0;
        }
    }

    /**
     * 移除值为value的
     *
     * @param key    键
     * @param values 值 可以是多个
     * @return 移除的个数
     */
    public long setRemove(String key, Object... values) {
        try {
            Long count = redisTemplate.opsForSet().remove(key, values);
            return count;
        } catch (Exception e) {
            log.error("", e);
            return 0;
        }
    }
    //===============================list=================================

    /**
     * 获取list缓存的内容
     *
     * @param key   键
     * @param start 开始
     * @param end   结束  0 到 -1代表所有值
     * @return
     */
    public List<Object> lGet(String key, long start, long end) {
        try {
            return redisTemplate.opsForList().range(key, start, end);
        } catch (Exception e) {
            log.error("", e);
            return null;
        }
    }

    /**
     * 获取list缓存的长度
     *
     * @param key 键
     * @return
     */
    public long lGetListSize(String key) {
        try {
            return redisTemplate.opsForList().size(key);
        } catch (Exception e) {
            log.error("", e);
            return 0;
        }
    }

    /**
     * 通过索引 获取list中的值
     *
     * @param key   键
     * @param index 索引  index>=0时， 0 表头，1 第二个元素，依次类推；index<0时，-1，表尾，-2倒数第二个元素，依次类推
     * @return
     */
    public Object lGetIndex(String key, long index) {
        try {
            return redisTemplate.opsForList().index(key, index);
        } catch (Exception e) {
            log.error("", e);
            return null;
        }
    }

    /**
     * 将list放入缓存
     *
     * @param key   键
     * @param value 值
     * @return
     */
    public <T> boolean lSet(String key, T value) {
        return lSet(key, value, 0);
    }

    /**
     * 将list放入缓存
     *
     * @param key   键
     * @param value 值
     * @param time  时间(秒)
     * @return
     */
    public <T> boolean lSet(String key, T value, long time) {
        return lSet(key, value, time, TimeUnit.SECONDS);
    }

    public <T> boolean lSet(String key, T value, long time, TimeUnit timeUnit) {
        try {
            redisTemplate.opsForList().rightPush(key, value);
            if (time > 0) {
                expire(key, time, timeUnit);
            }
            return true;
        } catch (Exception e) {
            log.error("", e);
            return false;
        }
    }

    /**
     * 将list放入缓存
     *
     * @param key   键
     * @param value 值
     * @return
     */
    public <T> boolean lSet(String key, List<T> value) {
        try {
            redisTemplate.opsForList().rightPushAll(key, value);
            return true;
        } catch (Exception e) {
            log.error("", e);
            return false;
        }
    }

    /**
     * 将list放入缓存
     *
     * @param key   键
     * @param value 值
     * @param time  时间(秒)
     * @return
     */
    public <T> boolean lSet(String key, List<T> value, long time) {
        try {
            redisTemplate.opsForList().rightPushAll(key, value);
            if (time > 0) {
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            log.error("", e);
            return false;
        }
    }

    /**
     * 根据索引修改list中的某条数据
     *
     * @param key   键
     * @param index 索引
     * @param value 值
     * @return
     */
    public boolean lUpdateIndex(String key, long index, Object value) {
        try {
            redisTemplate.opsForList().set(key, index, value);
            return true;
        } catch (Exception e) {
            log.error("", e);
            return false;
        }
    }

    /**
     * 移除N个值为value
     *
     * @param key   键
     * @param count 移除多少个
     * @param value 值
     * @return 移除的个数
     */
    public long lRemove(String key, long count, Object value) {
        try {
            Long remove = redisTemplate.opsForList().remove(key, count, value);
            return remove;
        } catch (Exception e) {
            log.error("", e);
            return 0;
        }
    }

    /**
     * 模糊查询获取key值
     *
     * @param pattern
     * @return
     */
    public Set keys(String pattern) {
        return redisTemplate.keys(pattern);
    }

    /**
     * 使用Redis的消息队列
     *
     * @param channel
     * @param message 消息内容
     */
    public void convertAndSend(String channel, Object message) {
        redisTemplate.convertAndSend(channel, message);
    }


    //=========BoundListOperations 用法 start============

    /**
     * 将数据添加到Redis的list中（从右边添加）
     *
     * @param listKey
     * @param expireEnum 有效期的枚举类
     * @param values     待添加的数据
     */
    public void addToListRight(String listKey, Status.ExpireEnum expireEnum, Object... values) {
        //绑定操作
        BoundListOperations<String, Object> boundValueOperations = redisTemplate.boundListOps(listKey);
        //插入数据
        boundValueOperations.rightPushAll(values);
        //设置过期时间
        boundValueOperations.expire(expireEnum.getTime(), expireEnum.getTimeUnit());
    }

    /**
     * 根据起始结束序号遍历Redis中的list
     *
     * @param listKey
     * @param start   起始序号
     * @param end     结束序号
     * @return
     */
    public List<Object> rangeList(String listKey, long start, long end) {
        //绑定操作
        BoundListOperations<String, Object> boundValueOperations = redisTemplate.boundListOps(listKey);
        //查询数据
        return boundValueOperations.range(start, end);
    }

    /**
     * 弹出右边的值 --- 并且移除这个值
     *
     * @param listKey
     */
    public Object rifhtPop(String listKey) {
        //绑定操作
        BoundListOperations<String, Object> boundValueOperations = redisTemplate.boundListOps(listKey);
        return boundValueOperations.rightPop();
    }

    public void zSetAdd(String key, Object value, Double score, long time, TimeUnit timeUnit) {
        try {
            redisTemplate.opsForZSet().add(key, value, score);
            if (time > 0) {
                expire(key, time, timeUnit);
            }
        } catch (Exception e) {
            log.error("redis error.", e);
        }
    }

    public <T> Set<T> zSetRangeByScore(String key, double min, double max) {
        try {
            return (Set<T>) redisTemplate.opsForZSet().rangeByScore(key, min, max);
        } catch (Exception e) {
            log.error("redis error.", e);
            return null;
        }
    }


    //=========BoundListOperations 用法 End============

    static class Status {
        /**
         * 过期时间相关枚举
         */
        public enum ExpireEnum {
            //未读消息的有效期为30天
            UNREAD_MSG(30L, TimeUnit.DAYS);

            /**
             * 过期时间
             */
            private Long time;
            /**
             * 时间单位
             */
            private TimeUnit timeUnit;

            ExpireEnum(Long time, TimeUnit timeUnit) {
                this.time = time;
                this.timeUnit = timeUnit;
            }

            public Long getTime() {
                return time;
            }

            public TimeUnit getTimeUnit() {
                return timeUnit;
            }
        }
    }


}
