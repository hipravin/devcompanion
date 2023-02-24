package com.hipravin.playgroundspring;

import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.messaging.handler.annotation.Header;

import java.io.IOException;


@SpringBootApplication
public class PlaygroundSpringApplication {
    private static final Logger log = LoggerFactory.getLogger(PlaygroundSpringApplication.class);

    static final String TOPIC_EXCHANGE = "spring-boot-exchange";
    //    static final String DIRECT_EXCHANGE = "spring-boot-exchange-direct";
    private static final String queueName = "spring-boot";
    private static final String queueNameSecond = "spring-boot-second";

    private static final String DEAD_LETTER_EXCHANGE = TOPIC_EXCHANGE + "-dl";
    private static final String DEAD_LETTER_QUEUE = queueName + "-dl";


//    static final String queueName = "spring-boot-" + UUID.randomUUID();

    @Bean
    @Primary
    Queue queue() {
        return QueueBuilder.nonDurable(queueName)
                .deadLetterExchange(DEAD_LETTER_EXCHANGE)
                .deadLetterRoutingKey("deadletter")
                .build();
    }

    @Bean
    @Qualifier("queueSecond")
    Queue queueSecond() {
        return new Queue(queueNameSecond, false);
    }

    @Bean
    @Qualifier("deadLetterQueue")
    Queue deadLetterQueue() {
        return new Queue(DEAD_LETTER_QUEUE, false);
    }

    @Bean
    @Primary
    TopicExchange exchange() {
        return new TopicExchange(TOPIC_EXCHANGE);
    }

    @Bean
    @Qualifier("deadLetterExchange")
    FanoutExchange deadLetterExchange() {
        return new FanoutExchange(DEAD_LETTER_EXCHANGE);
    }

    @Bean
    Binding binding(Queue queue, TopicExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with("foo.bar.#");
    }

    @Bean
    Binding bindingSecond(@Qualifier("queueSecond") Queue queueSecond, TopicExchange exchange) {
        return BindingBuilder.bind(queueSecond).to(exchange).with("foo.bar.#");
    }

    @Bean
    Binding bindingDle(@Qualifier("deadLetterQueue") Queue deadLetterQueue,
                       @Qualifier("deadLetterExchange") FanoutExchange exchange) {
        return BindingBuilder.bind(deadLetterQueue).to(exchange);
    }

//    @Bean
//    Binding binding(Queue queue, DirectExchange exchange) {
//        return BindingBuilder.bind(queue).to(exchange).with("foo.bar.baz");
//    }

    @Bean
    public MessageConverter jackson2MessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

//    @Bean
//    SimpleMessageListenerContainer container(ConnectionFactory connectionFactory,
//                                             MessageListenerAdapter listenerAdapter) {
//        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
//        container.setConnectionFactory(connectionFactory);
//        container.setQueueNames(queueName);
//        container.setMessageListener(listenerAdapter);
//        return container;
//    }

//    @Bean
//    MessageListenerAdapter listenerAdapter(Receiver receiver) {
//        return new MessageListenerAdapter(receiver, "receiveMessage");
//    }

//    @RabbitListener(queues = queueName)
//    public void listen(String in) {
//        System.out.println("Received with @RabbitListener: " + in);
//    }

//    @RabbitListener(queues = queueName)
//    public void listen(SampleMessageDto received) {
//        System.out.println("Received with @RabbitListener: " + received);
//    }

    //https://www.rabbitmq.com/tutorials/tutorial-six-spring-amqp.html
    @RabbitListener(queues = queueName)
    // @SendTo("tut.rpc.replies") used when the
    // client doesn't set replyTo.
    public SampleMessageDto process(SampleMessageDto received, Channel channel,
                                    @Header(AmqpHeaders.DELIVERY_TAG) long tag) throws IOException {
        log.info("Received with @RabbitListener: " + received);
//        channel.basicReject(tag, false);
//        channel.basicNack(tag, false, false);

//        if (true) {
//            throw new RuntimeException("Something went wrong intentionally");
//        }

//        channel.basicAck(tag, false);
        return SampleMessageDto.createWithRandomId(received.getMessage() + " processed");
    }

    @RabbitListener(queues = queueNameSecond)
    public void processSecondQueue(SampleMessageDto received, Channel channel,
                                   @Header(AmqpHeaders.DELIVERY_TAG) long tag) throws IOException {

        log.info("Received from second queue: " + received);
//        channel.basicAck(tag, false);
    }

    public static void main(String[] args) {
        SpringApplication.run(PlaygroundSpringApplication.class, args).close();
    }
}
