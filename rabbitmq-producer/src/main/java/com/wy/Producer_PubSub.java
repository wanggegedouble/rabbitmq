package com.wy;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Producer_PubSub {
    public static void main(String[] args) throws IOException, TimeoutException {
        //1 创建连接工厂
        ConnectionFactory factory = new ConnectionFactory();
        //2 设置参数
        factory.setHost("172.16.11.179");
        factory.setPort(32002);
        factory.setVirtualHost("wy");
        factory.setUsername("guest");
        factory.setPassword("guest");
        //3 创建连接 connection
        Connection connection = factory.newConnection();
        //4 创建channel
        Channel channel = connection.createChannel();
        //5 创建虚拟机
        /*
        String exchange, 交换机名称
        BuiltinExchangeType type, 交换机类型   DIRECT 定向, FANOUT 广播, TOPIC 通配符, HEADERS 参数;
        boolean durable, 是否持久化
        boolean autoDelete, 自动删除
        boolean internal, mq 内部使用 一般false
        Map<String, Object> arguments 参数 一般为null
        * */
        final String EXCHANGE = "pubSub_Fanout";
        channel.exchangeDeclare(EXCHANGE, BuiltinExchangeType.FANOUT,true,false,false,null);
        //6 创建队列
        final String queueName01 = "fanout01";
        final String queueName02 = "fanout02";
        channel.queueDeclare(queueName01,true,false,false,null);
        channel.queueDeclare(queueName02,true,false,false,null);
        //7 绑定队列和交换机
        /*
         * String queue, 队列名称
         * String exchange, 交换机名称
         * String routingKey, 路由键
         *   如果交换机类型为:fanout routingKey 设置为""
         * Map<String, Object> arguments
         * */
        channel.queueBind(queueName01,EXCHANGE,"");
        channel.queueBind(queueName02,EXCHANGE,"");
        //8 发送消息
        String body = "hell word! hello rabbitmq";
        channel.basicPublish(EXCHANGE,"",null,body.getBytes());
        channel.close();
        connection.close();
    }
}
