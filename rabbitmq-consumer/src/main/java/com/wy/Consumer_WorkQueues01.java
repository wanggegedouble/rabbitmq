package com.wy;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Consumer_WorkQueues01 {
    public static void main(String[] args) throws IOException, TimeoutException {
        //1 创建连接工厂
        ConnectionFactory factory = new ConnectionFactory();
        // 设置参数
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
        channel.queueDeclare("workQueues",true,false,false,null);
        DefaultConsumer consumer = new DefaultConsumer(channel){
            /*
             * String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body
             * */
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                System.out.println("consumerTag:"+consumerTag);
                System.out.println("Exchange:"+envelope.getExchange());
                System.out.println("RoutingKey:"+envelope.getRoutingKey());
                System.out.println("properties:"+properties.getAppId());
                System.out.println(new String(body));
            }
        };
        channel.basicConsume("workQueues",true,consumer);
    }
}
