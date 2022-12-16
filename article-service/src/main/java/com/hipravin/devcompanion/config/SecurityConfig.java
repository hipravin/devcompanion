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

@Configuration
public class SecurityConfig {
    //check out HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY for multiple completely independent security configurations

    @Bean(name = "apiSecurityFilterChain")
    @Order(SecurityProperties.BASIC_AUTH_ORDER - 100)
    public SecurityFilterChain articlesApifilterChain(HttpSecurity http) throws Exception {
        http.antMatcher("/api/**")
                .csrf(csrf -> csrf.disable())
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeHttpRequests((r) -> r.anyRequest().authenticated())
                .oauth2ResourceServer(OAuth2ResourceServerConfigurer::jwt);
        return http.build();
    }

    @Bean
    @Order(SecurityProperties.BASIC_AUTH_ORDER - 90)
    public SecurityFilterChain swaggerFilterChain(HttpSecurity http) throws Exception {
        http.requestMatchers().antMatchers("/swagger-ui/**", "/v3/api-docs/**")
                .and()
                .httpBasic(Customizer.withDefaults())
                .userDetailsService(hardcodedUserDetailsManager())
                .authorizeRequests().anyRequest().authenticated();
        return http.build();
    }

    @Bean
    @Order(SecurityProperties.BASIC_AUTH_ORDER - 80)
    public SecurityFilterChain actuatorFilterChain(HttpSecurity http) throws Exception {
        http.antMatcher("/actuator/**")
                .csrf().disable()
                .httpBasic(Customizer.withDefaults())
                .userDetailsService(hardcodedUserDetailsManager())
                .authorizeRequests().antMatchers("/actuator/health/**").permitAll()
                .anyRequest().hasAuthority("MANAGE");
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

    InMemoryUserDetailsManager hardcodedUserDetailsManager() {
        return new InMemoryUserDetailsManager(
                        new UserDetails[]{User.withUsername("admin").password("{noop}aadmin").authorities("MANAGE").build()});
    }
}
