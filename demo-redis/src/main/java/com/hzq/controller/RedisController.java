package com.hzq.controller;

import com.hzq.dto.PersonDTO;
import com.hzq.service.RedisToolsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

/**
 * @Author hzq
 * @ClassName com.hzq.controller.RedisController
 * @Date 2022/11/8 19:51
 * @Description
 */
@RestController
public class RedisController {


    private static final Logger LOGGER = LoggerFactory.getLogger(RedisController.class);

    @Autowired
    RedisToolsService redisToolsService;

    @PostMapping("/test")
    public String test(@RequestBody PersonDTO personDTO){
        Boolean b = redisToolsService.setData(personDTO.getName(), personDTO.getAge(),
                24L, TimeUnit.HOURS);
        if(b){
            LOGGER.info("添加成功");
        }else {
            LOGGER.info("添加失败");
            return "fail";
        }

        return "success";
    }


}
