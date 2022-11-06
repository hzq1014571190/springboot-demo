package com.hzq.receiver;

import com.hzq.constant.RabbitConstant;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import java.io.IOException;


/**
 * @author Administrator
 * 消费消息
 */
@Component
@RabbitListener(queues = RabbitConstant.TEST_DIRECT_QUEUE)
public class DirectReceiver {

    @RabbitHandler
    public void process(String message, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) Long deliveryTag) throws IOException {
        try {
            //手动Ack
            /**
             * 手动Ack参数说明
             * basicAck(long deliveryTag, boolean multiple)
             * deliveryTag：批量处理的标号，举例：这个队列现在有5条消息要消费，那么这批数据会标号从1-5递增，5的时候就会手动Ack  multiple：是否批量处理
             *
             */
            System.out.println("deliveryTag：" + deliveryTag);
            // deliveryTag：表示消息投递序号，每次消费消息或者消息重新投递后，deliveryTag 都会增加。
            // 手动消息确认模式下，我们可以对指定 deliveryTag 的消息进行 ack、nack、reject 等操作。
            // multiple：是否批量确认，值为 true 则会一次性 ack 所有小于当前消息 deliveryTag 的消息。

            channel.basicAck(deliveryTag,false);

            System.out.println("DirectReceiver消费者收到消息: " + message);
        }catch (Exception e){
            // 出现异常 拒签
            // deliveryTag：表示消息投递序号。
            // multiple：是否批量确认。
            // requeue：值为 true 消息将重新入队列。
            channel.basicNack(deliveryTag, false, false);
        }

    }

}
