package com.hipravin.devcompanion.gateway.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

@Component
public class BasicAuthGatewayFilterFactory extends AbstractGatewayFilterFactory<Object> {
    private static final Logger log = LoggerFactory.getLogger(BasicAuthGatewayFilterFactory.class);

    public BasicAuthGatewayFilterFactory() {
        super(Object.class);
    }

    public GatewayFilter apply() {
        return this.apply((Object) null);
    }

    @Override
    public GatewayFilter apply(Object config) {
        return (exchange, chain) -> {
            ServerWebExchange exchangeWithBasic = exchange.mutate()
                    .request(r -> r.headers(h -> h.setBasicAuth("user", "uuser")))
                    .build();

            return chain.filter(exchangeWithBasic);
        };
    }
}
