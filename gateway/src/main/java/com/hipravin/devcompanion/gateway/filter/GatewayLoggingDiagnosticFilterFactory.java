package com.hipravin.devcompanion.gateway.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

@Component
public class GatewayLoggingDiagnosticFilterFactory extends AbstractGatewayFilterFactory<Object> {
    private static final Logger log = LoggerFactory.getLogger(GatewayLoggingDiagnosticFilterFactory.class);

    public GatewayLoggingDiagnosticFilterFactory() {
        super(Object.class);
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
