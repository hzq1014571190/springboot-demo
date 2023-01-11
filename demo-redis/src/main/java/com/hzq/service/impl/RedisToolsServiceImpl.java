package com.hzq.service.impl;

import com.hzq.service.RedisToolsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.concurrent.TimeUnit;

/**
 * @author Jimmy Shan
 * @date 2021-12-03
 * @desc redis 工具服务实现
 */
@Service("redisToolsService")
public class RedisToolsServiceImpl implements RedisToolsService {

    private static final Logger LOGGER = LoggerFactory.getLogger(RedisToolsServiceImpl.class);

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * @desc 通过key获取 value
     */
    @Override
    public Object getData(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    /**
     * @desc 带过期时间的保存
     */
    @Override
    public Boolean setData(String key, Object value, Long times, TimeUnit tu) {
        try {
            redisTemplate.opsForValue().set(key, value, times, tu);
            return true;
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return false;
        }
    }

    /**
     * @desc 普通保存
     */
    @Override
    public Boolean setData(String key, Object value) {
        try {
            redisTemplate.opsForValue().set(key, value);
            return true;
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return false;
        }
    }

    /**
     * @desc 判断key是否存在或过期
     */
    @Override
    public Boolean isExpireByKey(String key) {
        return !StringUtils.isEmpty(key)
                && redisTemplate.hasKey(key)
                && redisTemplate.opsForValue().get(key) != null;
    }

    /**
     * @desc 删除key
     */
    @Override
    public Boolean removeKey(String key) {
        try {
            redisTemplate.delete(key);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
