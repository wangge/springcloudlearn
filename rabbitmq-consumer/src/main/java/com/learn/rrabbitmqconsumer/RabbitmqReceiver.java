package com.learn.rrabbitmqconsumer;

import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class RabbitmqReceiver {

    private static final Logger log = LoggerFactory.getLogger(RabbitmqReceiver.class);
    @RabbitHandler
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "spring-queue", durable = "true"),
            exchange = @Exchange(value = "spring-exchange", type = ExchangeTypes.TOPIC, durable = "true", ignoreDeclarationExceptions = "false"),
            key="foo.bar.*"
    ))//不能放在类上，放在方法上
    public void onMessage(Message message, Channel channel) throws IOException {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Receive rabbitmq message :" + message.toString());
        log.info("recevie rabbitmq message:"+ message.toString());
        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        channel.basicAck(deliveryTag, true);

        System.out.println("Receive --11:" + message.toString());

        //python 发送过来的是Byte类型,转化一下
        System.out.println("from python:"+ new String(message.getBody(), "UTF-8"));


    }
}
