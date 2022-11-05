package com.hzq.controller;

import com.hzq.common.MQSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

/**
 * @author Administrator
 */
@RestController
public class SendMessageController {

    @Autowired
    private MQSender mqSender;


    @RequestMapping("/send/{msg}")
    public String sendMessage(@PathVariable("msg")String message){
        mqSender.sendMessage(message);
        return "success";
    }
}
