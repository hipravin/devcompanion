package com.hipravin.devcompanion.config;

import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.context.RequestAttributeSecurityContextRepository;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
public class SecurityConfig {
    private static final String SC_REQUEST_ATTR_KEY_ACTUATOR = RequestAttributeSecurityContextRepository.class.getName()
            .concat(".SPRING_SECURITY_CONTEXT").concat("ACTUATOR");

    private static final String MANAGE_AUTHORITY_NAME = "MANAGE";
    private static final String USER_AUTHORITY_NAME = "USER";

    @Bean(name = "apiSecurityFilterChain")
    @Order(SecurityProperties.BASIC_AUTH_ORDER - 100)
    public SecurityFilterChain articlesApifilterChain(HttpSecurity http) throws Exception {
        http.antMatcher("/api/**")
                .csrf().disable()
                .httpBasic(withDefaults())
                .userDetailsService(apiUsersUserDetailsManager())
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
        var requestAttrRepository = new RequestAttributeSecurityContextRepository(SC_REQUEST_ATTR_KEY_ACTUATOR);

        http.antMatcher("/actuator/**")
                .securityContext(config -> config.securityContextRepository(requestAttrRepository))
                .csrf().disable()
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

    public InMemoryUserDetailsManager actuatorUsersUserDetailsManager() {
        return new InMemoryUserDetailsManager(
                User.withUsername("admin").password("{bcrypt}$2a$10$e.FByjmyWgR3r97UL4GG/O53NrYpnZ5rlpXFHmi5dDqrEa/CKmzyS") //aadmin
                        .authorities(MANAGE_AUTHORITY_NAME).build());
    }

    public InMemoryUserDetailsManager apiUsersUserDetailsManager() {
        return new InMemoryUserDetailsManager(
                User.withUsername("admin").password("{bcrypt}$2a$10$5tsjQ2Eq4n/oYZnmUCOzFOOL3FdC1Q2Rhf99xsPgvGFnwJLWTejYG") //aaadmin
                        .authorities(MANAGE_AUTHORITY_NAME, USER_AUTHORITY_NAME).build(),
                User.withUsername("user").password("{bcrypt}$2a$10$AKGQ3a0NoVdEUlRaiAY29OonRmPlpKHfJBXRucv8OiS6DIyj2q9wy") //uuser
                        .authorities(USER_AUTHORITY_NAME).build());
    }
}
