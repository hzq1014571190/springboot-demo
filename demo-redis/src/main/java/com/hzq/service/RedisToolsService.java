package com.hzq.service;

import java.util.concurrent.TimeUnit;

/**
 * @author Jimmy Shan
 * @date 2021-12-03
 * @desc redis 工具服务
 */
public interface RedisToolsService {
    /**
     * @desc 通过key获取 value
     */
    Object getData(String key);

    /**
     * @desc 带过期时间的保存
     */
    Boolean setData(String key, Object value, Long times, TimeUnit tu);

    /**
     * 普通保存
     * @param key
     * @param value
     * @return
     */
    public Boolean setData(String key, Object value);

    /**
     * @desc 判断key是否存在或过期
     */
    Boolean isExpireByKey(String key);

    /**
     * @desc 删除key
     */
    Boolean removeKey(String key);
}
