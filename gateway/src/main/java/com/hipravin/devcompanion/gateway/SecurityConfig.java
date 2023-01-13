package com.hipravin.devcompanion.gateway;

import com.hipravin.devcompanion.gateway.filter.ValidateAuthorizedClientsTokenSecurityFilterFactory;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestCustomizers;
import org.springframework.security.oauth2.client.web.server.DefaultServerOAuth2AuthorizationRequestResolver;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.context.WebSessionServerSecurityContextRepository;
import org.springframework.security.web.server.csrf.CookieServerCsrfTokenRepository;
import org.springframework.security.web.server.util.matcher.OrServerWebExchangeMatcher;
import org.springframework.security.web.server.util.matcher.PathPatternParserServerWebExchangeMatcher;

@Configuration
public class SecurityConfig {
    private static final String MANAGE_AUTHORITY_NAME = "MANAGE";
    private static final String ACTUATOR_SECURITY_CONTEXT_WS_ATTR_NAME = "SPRING_SECURITY_CONTEXT_ACTUATOR";

    @Bean
    @Order(SecurityProperties.BASIC_AUTH_ORDER - 100)
    SecurityWebFilterChain actuatorSecurityFilterChain(ServerHttpSecurity http) {
        var securityContextRepository = new WebSessionServerSecurityContextRepository();
        securityContextRepository.setSpringSecurityContextAttrName(ACTUATOR_SECURITY_CONTEXT_WS_ATTR_NAME);
        var authManager = new UserDetailsRepositoryReactiveAuthenticationManager(actuatorUsersUserDetailsService());

        http
                .securityMatcher(new PathPatternParserServerWebExchangeMatcher("/actuator/**"))
                .securityContextRepository(securityContextRepository)
                .httpBasic(customizer -> customizer.authenticationManager(authManager))
                .authorizeExchange(authorize -> authorize
                        .pathMatchers("/actuator/health/**").permitAll()
                        .anyExchange().hasAuthority(MANAGE_AUTHORITY_NAME)
                )
                .csrf(csrf -> csrf.disable());
        return http.build();
    }

    @Bean
    @Order(SecurityProperties.BASIC_AUTH_ORDER - 90)
    SecurityWebFilterChain bffIgnoreSecurityFilterChain(ServerHttpSecurity http) {
        http
                .securityMatcher(new OrServerWebExchangeMatcher(
                        new PathPatternParserServerWebExchangeMatcher("/manifest.json"),
                        new PathPatternParserServerWebExchangeMatcher("/favicon.ico"),
                        new PathPatternParserServerWebExchangeMatcher("/static/**"),
                        new PathPatternParserServerWebExchangeMatcher("/logo*.png")))
                .headers(headers -> headers.contentSecurityPolicy("script-src 'self'"))
                .csrf(csrf -> csrf.disable());
        return http.build();
    }

    @Bean
    @Order(SecurityProperties.BASIC_AUTH_ORDER - 80)
    SecurityWebFilterChain bffFilterChain(ServerHttpSecurity http, ReactiveClientRegistrationRepository repository,
                                          ValidateAuthorizedClientsTokenSecurityFilterFactory validateFilterFactory) {
        DefaultServerOAuth2AuthorizationRequestResolver pkceResolver = new DefaultServerOAuth2AuthorizationRequestResolver(repository);
        pkceResolver.setAuthorizationRequestCustomizer(OAuth2AuthorizationRequestCustomizers.withPkce());

        http
                .securityMatcher(new PathPatternParserServerWebExchangeMatcher("/**"))
                .addFilterBefore(validateFilterFactory.apply(), SecurityWebFiltersOrder.SERVER_REQUEST_CACHE)
                .headers(headers -> headers.contentSecurityPolicy("script-src 'self'"))
                .authorizeExchange((spec) -> spec.anyExchange().authenticated()) //interesting observation: gateway itself doesn't check/refresh oidc jwt token stored as session attribute
                .oauth2Login(login -> login.authorizationRequestResolver(pkceResolver))
                .csrf(csrf -> csrf.csrfTokenRepository(CookieServerCsrfTokenRepository.withHttpOnlyFalse()));
        return http.build();
    }

//    @Bean
//    public MapReactiveUserDetailsService userDetailsService() {
//        UserDetails user = User.withDefaultPasswordEncoder()
//                .username("admin")
//                .password("aadmin")
//                .authorities(MANAGE_AUTHORITY_NAME)
//                .build();
//        return new MapReactiveUserDetailsService(user);
//    }


    public MapReactiveUserDetailsService actuatorUsersUserDetailsService() {
        UserDetails actuatorAdmin = User.builder()
                .username("admin")
                .password("{bcrypt}$2a$10$e.FByjmyWgR3r97UL4GG/O53NrYpnZ5rlpXFHmi5dDqrEa/CKmzyS")
                .authorities(MANAGE_AUTHORITY_NAME)
                .build();

        return new MapReactiveUserDetailsService(actuatorAdmin);
    }
}
