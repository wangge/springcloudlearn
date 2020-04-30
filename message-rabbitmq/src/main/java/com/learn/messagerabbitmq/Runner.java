package com.learn.messagerabbitmq;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.concurrent.TimeUnit;

//@Component
public class Runner implements CommandLineRunner {

    private final RabbitTemplate rabbitTemplate;

    private final Receiver receiver;

    public Runner(Receiver receiver, RabbitTemplate rabbitTemplate){
        this.receiver = receiver;
        this.rabbitTemplate = rabbitTemplate;
    }
    @Override
    public void run(String... args) throws Exception {

        System.out.println("Send message...");
        rabbitTemplate.setMandatory(true);
        rabbitTemplate.setConfirmCallback(confirmCallback);
        rabbitTemplate.setReturnCallback(returnCallback);
        CorrelationData correlationData = new CorrelationData("1234567890"+ new Date());
        rabbitTemplate.convertAndSend(MessageRabbitmqApplication.topicExchangeName, "foo.bar.baz","hello from RabbitMQ!", correlationData);
        receiver.getLatch().await(10000, TimeUnit.MILLISECONDS);
    }
    final RabbitTemplate.ConfirmCallback confirmCallback = new RabbitTemplate.ConfirmCallback() {
        @Override
        public void confirm(CorrelationData correlationData, boolean b, String s) {
            System.out.println("correlationData:" + correlationData);
            System.out.println("ack:" + b);
            if(!b){
                System.out.println("errException");
            }
        }
    };

    final RabbitTemplate.ReturnCallback returnCallback = new RabbitTemplate.ReturnCallback() {
        @Override
        public void returnedMessage(Message message, int replayCode, String relayText, String exchange, String s2) {
            System.out.println("return exchange:" + exchange + relayText + replayCode + s2);
        }
    };

}
