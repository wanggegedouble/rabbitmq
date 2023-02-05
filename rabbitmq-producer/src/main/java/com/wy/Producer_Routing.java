package com.wy;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Producer_Routing {
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
        final String EXCHANGE = "Routing_direct";
        channel.exchangeDeclare(EXCHANGE, BuiltinExchangeType.DIRECT,true,false,false,null);
        final String ERROR = "error";
        final String INFO = "info";
        final String WARING = "waring";
        channel.queueBind(queueName01,EXCHANGE,ERROR);
        channel.queueBind(queueName02,EXCHANGE,INFO);
        channel.queueBind(queueName02,EXCHANGE,WARING);
        channel.queueBind(queueName02,EXCHANGE,ERROR);
        //8 发送消息
        String body = "hell word! hello rabbitmq ~~~~ error";
        String body1= "hell word! hello rabbitmq ~~~~ info";
        String body2= "hell word! hello rabbitmq ~~~~ waring";
        channel.basicPublish(EXCHANGE,WARING,null,body2.getBytes());
//        channel.basicPublish(EXCHANGE,INFO,null,body1.getBytes());
//        channel.basicPublish(EXCHANGE,WARING,null,body2.getBytes());
        channel.close();
        connection.close();
    }
}
