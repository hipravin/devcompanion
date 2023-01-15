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
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.context.RequestAttributeSecurityContextRepository;

@Configuration
public class SecurityConfig {
    private static final String SC_REQUEST_ATTR_KEY_ACTUATOR = RequestAttributeSecurityContextRepository.class.getName()
            .concat(".SPRING_SECURITY_CONTEXT").concat("ACTUATOR");

    private static final String MANAGE_AUTHORITY_NAME = "MANAGE";

    @Bean(name = "apiSecurityFilterChain")
    @Order(SecurityProperties.BASIC_AUTH_ORDER - 100)
    public SecurityFilterChain apiFilterChain(HttpSecurity http) throws Exception {
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
    public SecurityFilterChain apiDocsFilterChain(HttpSecurity http) throws Exception {
        http.requestMatchers().antMatchers("/api-docs/**")
                .and()
                .csrf(csrf -> csrf.disable())
                .authorizeRequests().anyRequest().permitAll();
        return http.build();
    }

    @Bean
    @Order(SecurityProperties.BASIC_AUTH_ORDER - 80)
    public SecurityFilterChain actuatorFilterChain(HttpSecurity http) throws Exception {
        var requestAttrRepository = new RequestAttributeSecurityContextRepository(SC_REQUEST_ATTR_KEY_ACTUATOR);

        http.antMatcher("/actuator/**")
                .securityContext(config -> config.securityContextRepository(requestAttrRepository))
                .csrf().disable()
                .sessionManagement(customizer -> customizer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .httpBasic(Customizer.withDefaults())
                .userDetailsService(actuatorUsersUserDetailsManager())
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

    InMemoryUserDetailsManager actuatorUsersUserDetailsManager() {
        return new InMemoryUserDetailsManager(
                User.withUsername("admin").password("{bcrypt}$2a$10$e.FByjmyWgR3r97UL4GG/O53NrYpnZ5rlpXFHmi5dDqrEa/CKmzyS")
                        .authorities(MANAGE_AUTHORITY_NAME).build());
    }
}
