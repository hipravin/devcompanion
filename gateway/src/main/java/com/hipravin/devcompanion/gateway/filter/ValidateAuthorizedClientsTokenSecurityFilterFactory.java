package com.hipravin.devcompanion.gateway.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.ReactiveOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.client.web.server.ServerOAuth2AuthorizedClientRepository;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.time.Instant;

@Component
public class ValidateAuthorizedClientsTokenSecurityFilterFactory {
    private static final Logger log = LoggerFactory.getLogger(ValidateAuthorizedClientsTokenSecurityFilterFactory.class);

    private final ServerOAuth2AuthorizedClientRepository serverOAuth2AuthorizedClientRepository;
    private final ObjectProvider<ReactiveOAuth2AuthorizedClientManager> clientManagerProvider;

    public ValidateAuthorizedClientsTokenSecurityFilterFactory(
            ServerOAuth2AuthorizedClientRepository serverOAuth2AuthorizedClientRepository,
            ObjectProvider<ReactiveOAuth2AuthorizedClientManager> clientManagerProvider) {
        this.serverOAuth2AuthorizedClientRepository = serverOAuth2AuthorizedClientRepository;
        this.clientManagerProvider = clientManagerProvider;
    }

    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        Mono<OAuth2AuthorizedClient> auth2AuthorizedClientMono = exchange.getPrincipal()
                .filter(principal -> principal instanceof OAuth2AuthenticationToken)
                .cast(OAuth2AuthenticationToken.class)
                .flatMap(principal -> serverOAuth2AuthorizedClientRepository
                        .loadAuthorizedClient(principal.getAuthorizedClientRegistrationId(), principal, exchange));

        return auth2AuthorizedClientMono.doOnSuccess(client -> {
            if (client != null && client.getAccessToken() != null && client.getRefreshToken() != null) {
                log.debug("Authorized Client access token: expires {}, {}",
                        client.getAccessToken().getExpiresAt(), client.getAccessToken().getTokenValue());
                log.debug("Authorized Client refresh token: expires {}, {}",
                        client.getRefreshToken().getExpiresAt(), client.getRefreshToken().getTokenValue());

                if (tokenExpired(client.getAccessToken())) {
                    //TODO: using .block() because of lack of reactive experience.
                    // also use refresh token instead of access
                    // at the moment keycloak refresh jwt token has proper 'exp' key, but it's ignored and refreshToken.expiresAt() is null
                    //so far randomly trying to make session expiration work
                    exchange.getSession().map(ws -> {
                        log.info("OAuth token expired, invalidating session: " + ws.getId());
                        return ws.invalidate();
                    }).block();
                    exchange.getResponse().setStatusCode(HttpStatus.PERMANENT_REDIRECT);
                    exchange.getResponse().getHeaders().setLocation(URI.create("/"));
                }
            } else {
                log.debug("Authorized Client or tokens is null");
            }
        }).then(chain.filter(exchange));
    }

    public WebFilter apply() {
        return (exchange, chain) -> filter(exchange, chain);
    }

    boolean tokenExpired(OAuth2AccessToken token) {
        return (token != null) && (token.getExpiresAt() != null)
                && token.getExpiresAt().isBefore(Instant.now());
    }
}
