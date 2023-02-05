package com.wy;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Consumer_Routing01 {
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
        final String queueName01 = "fanout01";
        final String ERROR = "error";
        final String INFO = "info";
        final String WARING = "waring";
        final String EXCHANGE = "Routing_direct";
        channel.queueDeclare(queueName01,true,false,false,null);
        DefaultConsumer consumer = new DefaultConsumer(channel){
            /*
             * String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body
             * */
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                /*System.out.println("consumerTag:"+consumerTag);
                System.out.println("Exchange:"+envelope.getExchange());
                System.out.println("RoutingKey:"+envelope.getRoutingKey());
                System.out.println("properties:"+properties.getAppId());*/
                System.out.println(new String(body));
                System.out.println("将消息打印到数据库~~~");
            }
        };
        channel.basicConsume(queueName01,true,consumer);
    }

}
