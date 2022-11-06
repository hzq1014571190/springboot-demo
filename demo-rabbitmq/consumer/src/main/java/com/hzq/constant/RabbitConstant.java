package com.hzq.constant;

/**
 * @author Administrator
 */
public class RabbitConstant {
    /**
     * 测试用direct交换机
     */
    public static final String TEST_DIRECT_EXCHANGE = "TestDirectExchange";
    /**
     * 测试用队列 与direct交换机绑定
     */
    public static final String TEST_DIRECT_QUEUE = "TestDirectQueue";
    /**
     * routing key
     */
    public static final String TEST_DIRECT_ROUTING_KEY = "TestDirectRoutingKey";


    /**
     * 测试用direct死信交换机
     */
    public static final String TEST_DEAD_LETTER_DIRECT_EXCHANGE = "TestDeadDirectExchange";

    public static final String TEST_DEAD_LETTER_DIRECT_QUEUE = "TestDeadDirectQueue";

    public static final String TEST_DEAD_LETTER_DIRECT_ROUTING_KEY = "TestDirectDeadRoutingKey";



}
