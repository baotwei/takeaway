package com.lky.project.config;

import com.lky.project.controller.OrderController;
import com.lky.project.mapper.OrderMapper;
import com.lky.project.pojo.Order;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.listener.api.ChannelAwareMessageListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.util.Map;

@Component

public class MyAckReceiver implements ChannelAwareMessageListener {

    @Autowired
    private OrderMapper orderMapper;

    @Override
    public void onMessage(Message message, Channel channel) throws Exception {
        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        try {
            byte[] body = message.getBody();
            ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(body));
            Map<String,Object> msgMap = (Map<String,Object>) ois.readObject();
            String messageId =  msgMap.get("messageId").toString();
            String messageData = msgMap.get("messageData").toString();
            String createTime = msgMap.get("createTime").toString();
            ois.close();

            if ("OrderDirectQueue".equals(message.getMessageProperties().getConsumerQueue())){
//                System.out.println("消息成功消费到  messageId:"+messageId+"  messageData:"+messageData+"  createTime:"+createTime);
                System.out.println("执行OrderDirectQueue中的消息的业务处理流程......");

                if (orderMapper.findOrderByOrderNumber(messageId).getOrderStat()==2){
                    System.out.println("消费的消息来自的队列名为："+message.getMessageProperties().getConsumerQueue());
                    channel.basicAck(deliveryTag, false); //第二个参数，手动确认可以被批处理，当该参数为 true 时，则可以一次性确认 delivery_tag 小于等于传入值的所有消息
                    System.out.println("消息消费成功-------------");
                }else {
                    channel.basicNack( deliveryTag, false , true ) ;
                }
            }

//			channel.basicReject(deliveryTag, true);//第二个参数，true会重新放回队列，所以需要自己根据业务逻辑判断什么时候使用拒绝
        } catch (Exception e) {
            channel.basicReject(deliveryTag, false);
            e.printStackTrace();
        }
    }

}