package com.hipravin.devcompanion.config;

import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Configuration
    @Order(SecurityProperties.BASIC_AUTH_ORDER - 90)
    public static class PublicApiConfig extends WebSecurityConfigurerAdapter {

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.antMatcher("/api/v1/articles/**")
                    .authorizeRequests().anyRequest().permitAll();
        }
    }

    @Configuration
    @Order(SecurityProperties.BASIC_AUTH_ORDER - 100)
    public static class DenyApiConfig extends WebSecurityConfigurerAdapter {

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.antMatcher("/swagger-ui/**")
                    .csrf().disable()
                    .authorizeRequests().anyRequest().authenticated()
                    .and()
                    .httpBasic();
        }
    }


}
