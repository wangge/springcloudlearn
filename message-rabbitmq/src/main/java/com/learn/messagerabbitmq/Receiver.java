package com.learn.messagerabbitmq;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.Serializable;
import java.util.concurrent.CountDownLatch;


@Component
public class Receiver{
    private CountDownLatch latch = new CountDownLatch(1);

    public void receiveMessage(Message message) throws IOException {
        System.out.println("received from python <"+ message.getBody() +">");
        System.out.println("received from python <"+ new String(message.getBody()) +">");
        //latch.countDown();
    }

    public CountDownLatch getLatch(){
        return latch;
    }
}
