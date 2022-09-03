package com.hipravin.devcompanion.gateway.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.annotation.Order;
import org.springframework.security.oauth2.client.ReactiveOAuth2AuthorizedClientManager;
import org.springframework.stereotype.Component;

@Component
@Order(1)
public class GatewayDiagnosticFilter extends AbstractGatewayFilterFactory<Object> {
    private static final Logger log = LoggerFactory.getLogger(GatewayDiagnosticFilter.class);

    private final ObjectProvider<ReactiveOAuth2AuthorizedClientManager> clientManagerProvider;

    public GatewayDiagnosticFilter(ObjectProvider<ReactiveOAuth2AuthorizedClientManager> clientManagerProvider) {
        super(Object.class);
        this.clientManagerProvider = clientManagerProvider;
    }

    public GatewayFilter apply() {
        return this.apply((Object)null);
    }
    @Override
    public GatewayFilter apply(Object config) {
        return (exchange, chain) -> {
            log.info("Relay Authorization: " + exchange.getRequest().getHeaders().get("Authorization"));
            return chain.filter(exchange);
        };
    }
}
