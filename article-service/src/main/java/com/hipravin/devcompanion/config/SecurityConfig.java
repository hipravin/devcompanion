package com.hipravin.devcompanion.config;

import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    @Order(SecurityProperties.BASIC_AUTH_ORDER - 100)
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.antMatcher("/swagger-ui/**")
                .csrf().disable()
                .authorizeRequests().anyRequest().authenticated()
                .and()
                .httpBasic();
        return http.build();
    }
    @Bean(name = "apiSecurityFilterChain")
    @Order(SecurityProperties.BASIC_AUTH_ORDER - 90)
    public SecurityFilterChain articlesApifilterChain(HttpSecurity http) throws Exception {
        http.antMatcher("/api/**")
                .authorizeRequests().anyRequest().permitAll();
        return http.build();
    }
}
