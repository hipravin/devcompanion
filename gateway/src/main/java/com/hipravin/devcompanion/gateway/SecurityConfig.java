package com.hipravin.devcompanion.gateway;

import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestCustomizers;
import org.springframework.security.oauth2.client.web.server.DefaultServerOAuth2AuthorizationRequestResolver;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.ServerAuthenticationFailureHandler;
import org.springframework.security.web.server.csrf.CookieServerCsrfTokenRepository;
import org.springframework.security.web.server.util.matcher.PathPatternParserServerWebExchangeMatcher;
import reactor.core.publisher.Mono;

@Configuration
public class SecurityConfig {

    @Bean
    @Order(SecurityProperties.BASIC_AUTH_ORDER - 100)
    SecurityWebFilterChain actuatorSecurityFilterChain(ServerHttpSecurity http) {
        http
                .securityMatcher(new PathPatternParserServerWebExchangeMatcher("/actuator/**"))
                .authorizeExchange(authorize -> authorize.anyExchange().permitAll())
                .csrf(csrf -> csrf.disable());
        return http.build();
    }

    @Bean
    @Order(SecurityProperties.BASIC_AUTH_ORDER - 90)
    SecurityWebFilterChain bffFilterChain(ServerHttpSecurity http, ReactiveClientRegistrationRepository repository) {
        DefaultServerOAuth2AuthorizationRequestResolver pkceResolver = new DefaultServerOAuth2AuthorizationRequestResolver(repository);
        pkceResolver.setAuthorizationRequestCustomizer(OAuth2AuthorizationRequestCustomizers.withPkce());

        http
                .securityMatcher(new PathPatternParserServerWebExchangeMatcher("/**"))
                .headers(headers -> headers.contentSecurityPolicy("script-src 'self'"))
                .authorizeExchange((spec) -> spec.pathMatchers("/manifest.json", "/favicon.ico", "/static/**").permitAll())
                .authorizeExchange((spec) -> spec.anyExchange().authenticated()) //interesting thing: gateway itself doesn't check/refresh oidc jwt token stored as session attribute
                .oauth2Login(login -> login.authorizationRequestResolver(pkceResolver))
                .csrf(csrf -> csrf.csrfTokenRepository(CookieServerCsrfTokenRepository.withHttpOnlyFalse()));
        return http.build();
    }
}
