package com.hipravin.devcompanion.config;

import com.hipravin.devcompanion.init.AwaitApplicationReadyInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    private final AwaitApplicationReadyInterceptor awaitApplicationReadyInterceptor;

    public WebMvcConfig(AwaitApplicationReadyInterceptor awaitApplicationReadyInterceptor) {
        this.awaitApplicationReadyInterceptor = awaitApplicationReadyInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(awaitApplicationReadyInterceptor);
    }
}
