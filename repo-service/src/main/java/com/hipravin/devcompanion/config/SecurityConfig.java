package com.hipravin.devcompanion.config;

import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
public class SecurityConfig {
    //check out HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY for multiple completely independent security configurations

    @Bean(name = "apiSecurityFilterChain")
    @Order(SecurityProperties.BASIC_AUTH_ORDER - 100)
    public SecurityFilterChain articlesApifilterChain(HttpSecurity http) throws Exception {
        http.antMatcher("/api/**")
                .csrf().disable()
                .httpBasic(withDefaults())
                .authorizeRequests().antMatchers("/api/v1/manage/**").hasAuthority("MANAGE")
                .and()
                .authorizeRequests().anyRequest().hasAuthority("USER");
        return http.build();
    }

    @Bean
    @Order(SecurityProperties.BASIC_AUTH_ORDER - 90)
    public SecurityFilterChain swaggerFilterChain(HttpSecurity http) throws Exception {
        http.requestMatchers().antMatchers("/swagger-ui/**", "/v3/api-docs/**")
                .and()
                .authorizeRequests().anyRequest().permitAll();
        return http.build();
    }

    @Bean
    @Order(SecurityProperties.BASIC_AUTH_ORDER - 80)
    public SecurityFilterChain actuatorFilterChain(HttpSecurity http) throws Exception {
        http.antMatcher("/actuator/**")
                .csrf().disable()
//                .authorizeRequests().anyRequest().hasAuthority("ACTUATOR");
                .authorizeRequests().anyRequest().permitAll();
        return http.build();
    }

    @Bean
    @Order(SecurityProperties.BASIC_AUTH_ORDER - 70)
    public SecurityFilterChain errorFilterChain(HttpSecurity http) throws Exception {
        http.antMatcher("/error")
                .authorizeRequests().anyRequest().permitAll();
        return http.build();
    }


    @Bean
    @Order(SecurityProperties.BASIC_AUTH_ORDER - 60)
    public SecurityFilterChain fallbackFilterChain(HttpSecurity http) throws Exception {
        http.antMatcher("/**")
                .authorizeRequests().anyRequest().denyAll();
        return http.build();
    }

    //TODO: No passowords in code, don't you know???
    @Bean
    public InMemoryUserDetailsManager hardcodedUserDetailsManager() {
        return new InMemoryUserDetailsManager(
                User.withUsername("admin").password("{noop}aadmin").authorities("ADMIN", "MANAGE", "USER", "ACTUATOR").build(),
                User.withUsername("actuator").password("{noop}aactuator").authorities("ACTUATOR").build(),
                User.withUsername("user").password("{noop}uuser").authorities("USER").build());
    }
}
