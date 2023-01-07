package com.hipravin.devcompanion.client.repo;

import feign.auth.BasicAuthRequestInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Objects;

@Configuration
public class RepoSearchClientFeignConfiguration {
    private final String userName;
    private final String userPassword;

    public RepoSearchClientFeignConfiguration(@Value("${repo-service.user.name}") String userName,
                                              @Value("${repo-service.user.password}") String userPassword) {
        this.userName = Objects.requireNonNull(userName);
        this.userPassword = Objects.requireNonNull(userPassword);
    }

    @Bean
    public BasicAuthRequestInterceptor basicAuthRequestInterceptor() {
        return new BasicAuthRequestInterceptor(userName, userPassword);
    }
}
