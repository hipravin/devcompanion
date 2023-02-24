package com.hipravin.playgroundspring;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;

@Component
public class Runner implements CommandLineRunner {
    private static final Logger log = LoggerFactory.getLogger(Runner.class);


    private final RabbitTemplate rabbitTemplate;
    private final Receiver receiver;

    public Runner(Receiver receiver, RabbitTemplate rabbitTemplate) {
        this.receiver = receiver;
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void run(String... args) throws Exception {
        log.info("Sending message...");

        String message = "Hello from RabbitMQ!";
//        rabbitTemplate.convertAndSend(PlaygroundSpringApplication.topicExchangeName, "foo.bar.baz", message);
//        rabbitTemplate.convertAndSend(PlaygroundSpringApplication.topicExchangeName, "foo.bar.baz",
//                SampleMessageDto.createWithRandomId(message));


        SampleMessageDto response = rabbitTemplate.convertSendAndReceiveAsType(
                PlaygroundSpringApplication.TOPIC_EXCHANGE,
                "foo.bar.baz",
                SampleMessageDto.createWithRandomId("request dto"),
                new ParameterizedTypeReference<>() {
                });

        log.info("Got response: {}", response);

        Thread.sleep(160000);

//        receiver.getLatch().await(5, TimeUnit.SECONDS);
    }


//    Exchange	spring-boot-exchange
//    Routing Key	foo.bar.baz
//    Properties
//    reply_to:	amq.rabbitmq.reply-to.g1h2AA5yZXBseUA2MzgyNDMwNQAAD5sAAAABY+tojg==.Iz7pyUoCvMeyiSoF5PyUDw==
//    correlation_id:	1
//    priority:	0
//    delivery_mode:	2
//    headers:
//    __TypeId__:	com.hipravin.playgroundspring.SampleMessageDto
//    content_encoding:	UTF-8
//    content_type:	application/json
//            Payload

}