package com.hipravin.devcompanion.gateway;

import com.hipravin.devcompanion.gateway.filter.GatewayDiagnosticFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.gateway.filter.factory.TokenRelayGatewayFilterFactory;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableConfigurationProperties({
        AppRouteProperties.class})
public class GatewayApplication {

    @Autowired
    private TokenRelayGatewayFilterFactory tokenRelayfilterFactory;

    @Autowired
    private GatewayDiagnosticFilter gatewayDiagnosticFilter;

    @Autowired
    private AppRouteProperties routeProperties;

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("article-service", r -> r.path("/api/v1/articles/**", "/api/v1/users/**")
                        .filters(f -> f.filters(tokenRelayfilterFactory.apply(), gatewayDiagnosticFilter.apply())
                                .removeRequestHeader("Cookie")) // Prevents cookie being sent downstream
                        .uri(routeProperties.getArticleServiceUri()))
                .route("frontend", r -> r.path("/**")
                        .uri(routeProperties.getFrontendUri()))
                .build();
    }

    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
    }

}
