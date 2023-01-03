package com.lky.project.config;


import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
//@RabbitListener(queues = "OrderDirectQueue")//监听的队列名称 TestDirectQueue
public class DirectReceiver {

//    @RabbitHandler
//    public void process(Map testMessage) {
//        System.out.println("OrderDirectQueue消费者收到消息  : " + testMessage.toString());
//    }

}