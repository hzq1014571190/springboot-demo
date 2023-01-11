package com.hzq;

import com.hzq.service.RedisToolsService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @Author hzq
 * @ClassName com.hzq.TestRedis
 * @Date 2023/1/11 16:52
 * @Description
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class TestRedis {

    private static final Logger LOGGER = LoggerFactory.getLogger(TestRedis.class);

    @Autowired
    private RedisToolsService redisToolsService;

    @Test
    public void setData(){
        redisToolsService.setData("hzq", 18);
    }

    @Test
    public void getData(){
        Integer age = (Integer)(redisToolsService.getData("hzq"));
        LOGGER.info("取出来的value是: {}", age);
    }
}
