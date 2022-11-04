package com.hzq.controller;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Administrator
 */
@RestController
public class SendMessageController {

    @Autowired
    private AmqpTemplate rabbitTemplate;

    @RequestMapping("/send/{msg}")
    public String sendMessage(@PathVariable("msg")String message){
        rabbitTemplate.convertAndSend("TestDirectExchange", "TestDirectRoutingKey", "hello " + message);
        return "success";
    }
}
