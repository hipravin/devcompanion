package com.hipravin.devcompanion.config;

import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
public class SecurityConfig {
    //check out HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY for multiple completely independent security configurations

    private static final String MANAGE_AUTHORITY_NAME = "MANAGE";
    private static final String USER_AUTHORITY_NAME = "USER";

    @Bean(name = "apiSecurityFilterChain")
    @Order(SecurityProperties.BASIC_AUTH_ORDER - 100)
    public SecurityFilterChain articlesApifilterChain(HttpSecurity http) throws Exception {
        http.antMatcher("/api/**")
                .csrf().disable()
                .httpBasic(withDefaults())
                .authorizeRequests().antMatchers("/api/v1/manage/**").hasAuthority(MANAGE_AUTHORITY_NAME)
                .and()
                .authorizeRequests().anyRequest().hasAuthority(USER_AUTHORITY_NAME);
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
                .httpBasic(Customizer.withDefaults())
                .authorizeRequests().antMatchers("/actuator/health/**").permitAll()
                .anyRequest().hasAuthority(MANAGE_AUTHORITY_NAME);
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

    @Bean
        public InMemoryUserDetailsManager fixedUserDetailsManager() {
        //to generate new hash use: new BCryptPasswordEncoder().encode("password")
        return new InMemoryUserDetailsManager(
                User.withUsername("admin").password("$2a$10$e.FByjmyWgR3r97UL4GG/O53NrYpnZ5rlpXFHmi5dDqrEa/CKmzyS")
                        .authorities(MANAGE_AUTHORITY_NAME, USER_AUTHORITY_NAME).build(),
                User.withUsername("user").password("$2a$10$AKGQ3a0NoVdEUlRaiAY29OonRmPlpKHfJBXRucv8OiS6DIyj2q9wy")
                        .authorities(USER_AUTHORITY_NAME).build());
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
