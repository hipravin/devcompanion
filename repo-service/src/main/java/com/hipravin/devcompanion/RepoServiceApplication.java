package com.hipravin.devcompanion;

import com.hipravin.devcompanion.config.ApplicationProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({
        ApplicationProperties.class})
public class RepoServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(RepoServiceApplication.class, args);
    }

}
