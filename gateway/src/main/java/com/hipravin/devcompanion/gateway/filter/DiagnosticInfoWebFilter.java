package com.hipravin.devcompanion.gateway.filter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.server.Cookie;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.web.server.csrf.CsrfToken;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class DiagnosticInfoWebFilter implements WebFilter {
    private static final String POD_IP_HEADER_PREFIX = "X-K8s-Pod-Ip-";

    @Value("${POD_IP}")
    private String podIp;

    @Value("${spring.application.name}")
    private String applicationName;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        HttpHeaders httpHeaders = exchange.getResponse().getHeaders();
        addPodIpHeader(httpHeaders);

        return chain.filter(exchange);
    }

    void addPodIpHeader(HttpHeaders httpHeaders) {
        String podIdHeaderName = POD_IP_HEADER_PREFIX + applicationName;
        httpHeaders.add(podIdHeaderName, podIp);
    }
}
