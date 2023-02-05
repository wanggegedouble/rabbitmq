package com.wy;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Producer_HelloWord {
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
        //5 创建队列 queue
        /*
        * String var1, boolean var2, boolean var3, boolean var4, Map<String, Object> var5
        * var1 队列名称 queue
        * var2 durable 是否持久化 当Mq重启的时候还在
        * var3 exclusive 是否独占 只能有一个消费者监听这个队列 当connection 是否删除队列
        * var4 autoDelete 当没有consumer时是否自动删除
        * var5 arguments 参数
        * */
        //如果没有一个名字叫 Hello的队列会创建一个该队列 存在则不会
        channel.queueDeclare("Hello",true,false,false,null);
        //6 发送消息 String var1, String var2, AMQP.BasicProperties var3, byte[] var4
        /*
        * var1 exchange 交换机 简单模式下默认为""
        * var2 routingKey 路由名称
        * var3 properties 配置信息
        * var4 body 发送的消息数据
         */
        String body = "Word";
        String body1 = "RabbitMQ";
        channel.basicPublish("","Hello",null,body.getBytes());
        channel.basicPublish("","Hello",null,body1.getBytes());
        //7 释放资源
        channel.close();
        connection.close();
    }
}
