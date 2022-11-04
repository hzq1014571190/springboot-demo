package com.hzq.receiver;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @author Administrator
 * 消费消息
 */
@Component
@RabbitListener(queues = "TestDirectQueue")
public class DirectReceiver {

    @RabbitHandler
    public void process(String message) {
        System.out.println("DirectReceiver消费者收到消息: " + message);
    }

}
