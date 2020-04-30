package com.learn.messagerabbitmq;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.amqp.support.converter.AbstractMessageConverter;
import org.springframework.amqp.support.converter.MessageConversionException;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;

@SpringBootApplication
@Slf4j
public class MessageRabbitmqApplication {

    public static void main(String[] args) {
        SpringApplication.run(MessageRabbitmqApplication.class, args);
    }

    static final String topicExchangeName = "spring-exchange";
    static final String queueName = "spring-queue2";


    @Bean
    Queue queue(){
        return new Queue(queueName, true);
    }

    @Bean
    TopicExchange exchange(){
        return new TopicExchange(topicExchangeName, true, false);
    }

    @Bean
    Binding binding(Queue queue, TopicExchange topicExchange){
        return BindingBuilder.bind(queue).to(topicExchange).with("foo.bar.#");
    }

    @Bean
    SimpleMessageListenerContainer container(ConnectionFactory connectionFactory,
                                             MessageListenerAdapter listenerAdapter,
                                             MessageConverter byteMessageConverter){
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(queueName);//监听队列
        listenerAdapter.setMessageConverter(byteMessageConverter);
        container.setMessageListener(listenerAdapter);
        return container;
    }
    @Bean
    MessageListenerAdapter listenerAdapter(Receiver receiver){
        return new MessageListenerAdapter(receiver, "receiveMessage");
    }

    @Bean
    MessageConverter byteMessageConverter(){
        return new AbstractMessageConverter() {
            //发送消息时
            @Override
            protected Message createMessage(Object o, MessageProperties messageProperties) {
                return new Message(JSON.toJSONBytes(o), messageProperties);

            }

            //接受消息,python 传过来的需要转化,注解的方式监听已经自动转化
            @Override
            public Message fromMessage(Message message) throws MessageConversionException {
                byte[] body = message.getBody();
                MessageProperties messageProperties = message.getMessageProperties();
                //需要转为对应类的类名
                //String className = (String) messageProperties.getHeaders().get(Receiver.class);
                try {
                    return message;
                } catch (Exception e) {
                    log.error("mq转换错误: message ==> {} body ==> {}", JSON.toJSONString(message), new String(body), e);
                    throw new RuntimeException(e);
                }

            }
        };

    }
}
