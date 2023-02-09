package com.hipravin.devcompanion.gateway.filter;

import com.hipravin.devcompanion.gateway.event.OAuthUserAuthenticatedEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import org.springframework.web.server.WebSession;
import reactor.core.publisher.Mono;

@Component
public class AuthCapturingFilterFactory {
    private static final String AUTH_LOGGED_SESSION_ATTR_KEY = "AUTH_LOGGED_KEY";

    private final ApplicationEventPublisher applicationEventPublisher;

    public AuthCapturingFilterFactory(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        Mono<OAuth2AuthenticationToken> auth2AuthorizedClientMono = exchange.getPrincipal()
                .filter(principal -> principal instanceof OAuth2AuthenticationToken)
                .cast(OAuth2AuthenticationToken.class);

        return auth2AuthorizedClientMono.zipWith(exchange.getSession()).doOnNext(tuple2 -> {
            OAuth2AuthenticationToken token = tuple2.getT1();
            WebSession session = tuple2.getT2();

            session.getAttributes().computeIfAbsent(AUTH_LOGGED_SESSION_ATTR_KEY, key -> {
                applicationEventPublisher.publishEvent(new OAuthUserAuthenticatedEvent(this, token ));
                return true;
            });

        }).then(chain.filter(exchange));
    }

    public WebFilter apply() {
        return (exchange, chain) -> filter(exchange, chain);
    }
}
