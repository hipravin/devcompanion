package com.hipravin.devcompanion.gateway.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import java.nio.charset.StandardCharsets;

@Component
public class BasicAuthGatewayFilterFactory extends AbstractGatewayFilterFactory<BasicAuthGatewayFilterFactory.BasicAuthConfig> {
    public BasicAuthGatewayFilterFactory() {
        super(BasicAuthConfig.class);
    }

    @Override
    public GatewayFilter apply(BasicAuthConfig config) {
        return (exchange, chain) -> {
            ServerWebExchange exchangeWithBasic = exchange.mutate()
                    .request(r -> r.headers(h -> h.setBasicAuth(config.getAuthEncoded())))
                    .build();

            return chain.filter(exchangeWithBasic);
        };
    }

    public static class BasicAuthConfig {
        private final String authEncoded;

        public BasicAuthConfig(String authEncoded) {
            this.authEncoded = authEncoded;
        }

        public BasicAuthConfig(String username, String password) {
            this.authEncoded = HttpHeaders.encodeBasicAuth(username, password, StandardCharsets.UTF_8);
        }

        public String getAuthEncoded() {
            return authEncoded;
        }
    }
}
