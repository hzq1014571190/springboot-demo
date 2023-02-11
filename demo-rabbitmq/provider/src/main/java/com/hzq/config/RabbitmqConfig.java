package com.hzq.config;

import com.hzq.common.MQSender;
import com.hzq.constant.RabbitConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Administrator
 */
@Configuration
public class RabbitmqConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(MQSender.class);

    @Autowired
    private CachingConnectionFactory cachingConnectionFactory;

//=================== 正常 begin ========================
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
    public Queue testDirectQueue(){
        Map<String, Object> args = new HashMap<>(4);

        // 绑定死信交换机 声明交换机和routing key
        // x-dead-letter-exchange：这里声明当前业务队列绑定的死信交换机
        args.put("x-dead-letter-exchange", RabbitConstant.TEST_DEAD_LETTER_DIRECT_EXCHANGE);
        // x-dead-letter-routing-key：这里声明当前业务队列的死信路由 key
        args.put("x-dead-letter-routing-key", RabbitConstant.TEST_DEAD_LETTER_DIRECT_ROUTING_KEY);

        // TODO 设置一下过期时间 以便于测试死信
        args.put("x-message-ttl", RabbitConstant.QUEUE_MESSAGE_TTL);

        return new Queue(RabbitConstant.TEST_DIRECT_QUEUE, true, false, false, args);
    }


    /**
     * 新建一个Direct交换机 起名：TestDirectExchange
     * @return
     */
    @Bean
    public DirectExchange testDirectExchange() {
        return new DirectExchange(RabbitConstant.TEST_DIRECT_EXCHANGE);
    }


    /**
     * 绑定  将队列和交换机绑定, 并设置用于匹配键：TestDirectRouting
     * 当 routingKey 与消息的 routingKey 相匹配时 对应的消息就会被发到此队列
     * @return
     */
    @Bean
    public Binding bindingDirect() {
        return BindingBuilder.bind(testDirectQueue()).to(testDirectExchange())
                .with(RabbitConstant.TEST_DIRECT_ROUTING_KEY);
    }

//=================== 正常 end ==========================






//=================== 死信 begin ========================

    @Bean
    public Queue deadLetterQueue() {
        return new Queue(RabbitConstant.TEST_DEAD_LETTER_DIRECT_QUEUE);
    }

    @Bean
    public DirectExchange deadLetterExchange(){
        return new DirectExchange(RabbitConstant.TEST_DEAD_LETTER_DIRECT_EXCHANGE);
    }

    @Bean
    public Binding bindingDeadLetter(){
        return BindingBuilder.bind(deadLetterQueue()).to(deadLetterExchange())
                .with(RabbitConstant.TEST_DEAD_LETTER_DIRECT_ROUTING_KEY);
    }


//=================== 死信 end ==========================


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
