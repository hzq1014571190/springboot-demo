package com.hzq.config;

import com.hzq.common.MQSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.nio.charset.StandardCharsets;

/**
 * @author Administrator
 */
@Configuration
public class RabbitmqConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(MQSender.class);

    @Autowired
    private CachingConnectionFactory cachingConnectionFactory;


    /**
     *  新建一个队列并设置持久化
     *
     *      durable:是否持久化,默认是false,持久化队列：会被存储在磁盘上，当消息代理重启时仍然存在，暂存队列：当前连接有效
     *      exclusive:默认也是false，只能被当前创建的连接使用，而且当连接关闭后队列即被删除。此参考优先级高于durable
     *      autoDelete:是否自动删除，当没有生产者或者消费者使用此队列，该队列会自动删除。
     *        return new Queue("TestDirectQueue",true,true,false);
     *
     *     一般设置一下队列的持久化就好
     *     所以我们只需要设置下队列名称就好   其他选项默认
     *
     *     //其中的一个构造方法
     *     public Queue(String name) {
     *         this(name, true, false, false);
     *     }
     *
     * @return
     */
    @Bean
    public Queue TestDirectQueue(){
        return new Queue("TestDirectQueue");
    }


    /**
     * 新建一个Direct交换机 起名：TestDirectExchange
     * @return
     */
    @Bean
    DirectExchange TestDirectExchange() {
        //  return new DirectExchange("TestDirectExchange",true,true);
        return new DirectExchange("TestDirectExchange");
    }


    /**
     * 绑定  将队列和交换机绑定, 并设置用于匹配键：TestDirectRouting
     * 当 routingKey 与消息的 routingKey 相匹配时 对应的消息就会被发到此队列
     * @return
     */
    @Bean
    Binding bindingDirect() {
        return BindingBuilder.bind(TestDirectQueue()).to(TestDirectExchange()).with("TestDirectRoutingKey");
    }



    /**
     * 多例的RabbitTemplate
     * @return
     */
    @Bean
    public RabbitTemplate rabbitTemplate(){

        RabbitTemplate rabbitTemplate = new RabbitTemplate(cachingConnectionFactory);

        // 消息只要被rabbit exchange接收到就会执行confirmCallback
        // 不管是否成功投递到exchange都会执行
        // 被broker执行只能保证消息到达服务器，并不能保证一定被投递到目标queue里
        rabbitTemplate.setConfirmCallback((data,ack,cause) -> {
            // 此处的data对应我们发送消息时设置的 CorrelationData correlationData = new CorrelationData(UUID.randomUUID().toString());
            // 这个类其实就是回调消息类
            String msgId = data.getId();
            if (ack) {
                LOGGER.info(msgId + "消息发送到exchange成功!!!");
            }else{
                LOGGER.info(msgId + "消息发送到exchange失败!!!");
            }
        });


        // confirm 模式只能保证消息达到exchange 不能保证消息准确投递到目标queue中
        // 有些业务场景下，需要保证消息一定投递到目标queue中，此时需要用到return退回模式
        // 如果未能达到目前queue中将调用returnCallback,可以记录下详细投递数据，定期巡检或者纠错
        rabbitTemplate.setReturnsCallback(new RabbitTemplate.ReturnsCallback() {
            @Override
            public void returnedMessage(ReturnedMessage returnedMessage) {
                LOGGER.info("消息投递到对应queue失败");
                LOGGER.info("routingKey {}", returnedMessage.getRoutingKey());
                LOGGER.info("消息 " + new String(returnedMessage.getMessage().getBody(), StandardCharsets.UTF_8));
                LOGGER.info("replyCode {}", returnedMessage.getReplyCode());
                LOGGER.info("exchange {}", returnedMessage.getExchange());
                LOGGER.info("replyText {}", returnedMessage.getReplyText());
            }
        });
        return rabbitTemplate;
    }
}
