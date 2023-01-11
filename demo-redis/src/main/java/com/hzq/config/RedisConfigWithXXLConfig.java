package com.hzq.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xxl.conf.core.annotation.XxlConf;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import redis.clients.jedis.JedisPoolConfig;

import java.time.Duration;

/**
 * @Author hzq
 * @ClassName com.hzq.config.RedisConfig
 * @Date 2022/11/8 20:21
 * @Description
 */
// @Configuration
public class RedisConfigWithXXLConfig {


    @XxlConf("demo-redis.spring.redis.database")
    private String redisDatabase;
    @XxlConf("demo-redis.spring.redis.password")
    private String redisPassword;
    @XxlConf("demo-redis.spring.redis.host")
    private String redisHost;
    @XxlConf("demo-redis.spring.redis.port")
    private String redisPort;
    @XxlConf("demo-redis.spring.redis.timeout")
    private String redisTimeout;
    @XxlConf("demo-redis.spring.redis.jedis.pool.max.active")
    private String redisMaxPoolSize;
    @XxlConf("demo-redis.spring.redis.jedis.pool.max.wait")
    private String redisMaxPoolWait;
    @XxlConf("demo-redis.spring.redis.jedis.pool.max.idle")
    private String redisMaxIdle;
    @XxlConf("demo-redis.spring.redis.jedis.pool.min.idle")
    private String redisMinIdle;



    /**
     * @desc redis模板，存储关键字是字符串，值jackson2JsonRedisSerializer是序列化后的值
     */
    @Bean
    public RedisTemplate<String, Object> redisTemplate() {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(connectionPoolsFactory());
        // 开启事务
        redisTemplate.setEnableTransactionSupport(true);

        // 使用Jackson2JsonRedisSerializer来序列化和反序列化redis的value值（默认使用JDK的序列化方式）
        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer =
                new Jackson2JsonRedisSerializer<>(Object.class);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jackson2JsonRedisSerializer.setObjectMapper(objectMapper);

        // 使用StringRedisSerializer来序列化和反序列化redis的key值
        RedisSerializer<?> redisSerializer = new StringRedisSerializer();
        // key
        redisTemplate.setKeySerializer(redisSerializer);
        redisTemplate.setHashKeySerializer(redisSerializer);
        // value
        redisTemplate.setValueSerializer(jackson2JsonRedisSerializer);
        redisTemplate.setHashValueSerializer(jackson2JsonRedisSerializer);

        redisTemplate.afterPropertiesSet();

        return redisTemplate;
    }

    /**
     * @desc 使用jedis pool创建连接(连接池配置)
     */
    public RedisConnectionFactory connectionPoolsFactory() {
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        // 最大空闲连接数, 默认8个
        poolConfig.setMaxIdle(Integer.parseInt(redisMaxIdle));
        // 最小空闲连接数, 默认0
        poolConfig.setMinIdle(Integer.parseInt(redisMinIdle));
        // 最大连接数, 默认8个
        poolConfig.setMaxTotal(Integer.parseInt(redisMaxPoolSize));
        // 获取连接时的最大等待毫秒数, 如果不超时设置: -1
        poolConfig.setMaxWaitMillis(Long.parseLong(redisMaxPoolWait));
        // 逐出扫描的时间间隔(毫秒) 如果为负数,则不运行逐出线程, 默认-1
        poolConfig.setTimeBetweenEvictionRunsMillis(-1);
        // 在获取连接的时候检查有效性, 默认false
        poolConfig.setTestOnBorrow(true);
        // 在空闲时检查有效性, 默认false
        poolConfig.setTestWhileIdle(true);

        JedisClientConfiguration jedisClientConfiguration =
                JedisClientConfiguration.builder().usePooling().poolConfig(poolConfig).and()
                        .readTimeout(Duration.ofMillis(Long.parseLong(redisTimeout)))
                        .connectTimeout(Duration.ofMillis(Long.parseLong(redisTimeout)))
                        .build();
        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
        redisStandaloneConfiguration.setDatabase(Integer.parseInt(redisDatabase));
        redisStandaloneConfiguration.setHostName(redisHost);
        redisStandaloneConfiguration.setPassword(redisPassword == null ? "" : redisPassword);
        redisStandaloneConfiguration.setPort(Integer.parseInt(redisPort));
        JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory(redisStandaloneConfiguration, jedisClientConfiguration);
        jedisConnectionFactory.afterPropertiesSet();
        return jedisConnectionFactory;
    }
}
