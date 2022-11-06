package com.hzq;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.TimeUnit;


@RunWith(SpringRunner.class)
@SpringBootTest
public class SendMessageTest {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Test
    public void test(){

        CorrelationData correlationData = new CorrelationData(rabbitTemplate.getUUID());

        rabbitTemplate.convertAndSend("TestDirectExchange", "TestDirectRoutingKey1", "hello hzq", correlationData);

        try {
            TimeUnit.SECONDS.sleep(2L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
