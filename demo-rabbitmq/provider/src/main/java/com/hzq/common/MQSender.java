package com.hzq.common;

import com.hzq.constant.RabbitConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * @author Administrator
 */
@Component
public class MQSender {

    @Autowired
    private RabbitTemplate rabbitTemplate;


    public void sendMessage(String message){
        CorrelationData correlationData = new CorrelationData(UUID.randomUUID().toString());
        // 发送消息时将 correlationData 带上
        rabbitTemplate.convertAndSend(RabbitConstant.TEST_DIRECT_EXCHANGE,
                                    RabbitConstant.TEST_DIRECT_ROUTING_KEY,
                                        "hello " + message,
                                                correlationData);
    }
}
