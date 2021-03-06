package com.hipravin.devcompanion;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.event.ContextStartedEvent;
import org.springframework.context.event.EventListener;

@SpringBootApplication
public class ArticleServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ArticleServiceApplication.class, args);
    }


//    //log all events for research purposes
//    @EventListener
//    public void handleContextStart(ApplicationEvent event) {
//        System.out.println("Event: " + event.getClass() + " " + event.toString());
//    }
}
