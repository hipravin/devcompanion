package com.hipravin.devcompanion.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.i18n.FixedLocaleResolver;

import java.util.Locale;

@Configuration
public class ValidationMessageLocaleConfig {
    @Bean
    FixedLocaleResolver localeResolver() {
        return new FixedLocaleResolver(Locale.ENGLISH);
    }
}