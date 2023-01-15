package com.hipravin.devcompanion.gateway;

import com.hipravin.devcompanion.gateway.filter.BasicAuthGatewayFilterFactory;
import com.hipravin.devcompanion.gateway.filter.GatewayLoggingDiagnosticFilterFactory;
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
    private GatewayLoggingDiagnosticFilterFactory gatewayDiagnosticFilter;
    @Autowired
    private BasicAuthGatewayFilterFactory basicAuthFilterFactory;
    @Autowired
    private AppRouteProperties routeProperties;

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        var repoServiceAuthConfig = new BasicAuthGatewayFilterFactory.BasicAuthConfig(routeProperties.getRepoServiceAuthEncoded());

        return builder.routes()
                .route("apidocs-article-service", r -> r.path("/api-docs/articles/**")
                        .uri(routeProperties.getArticleServiceUri()))
                .route("apidocs-repos-service", r -> r.path("/api-docs/repos/**")
                        .uri(routeProperties.getRepoServiceUri()))
                .route("article-service", r -> r.path("/api/v1/articles/**", "/api/v1/users/**")
                        .filters(f -> f.filters(tokenRelayfilterFactory.apply(), gatewayDiagnosticFilter.apply())
                                .removeRequestHeader("Cookie")) // Prevents cookie being sent downstream
                        .uri(routeProperties.getArticleServiceUri())) //springdev: we only take the host and port from the URI, the path must be set via the set path filter, it will not append it.
                .route("repo-service", r -> r.path("/api/v1/repos/**")
                        .filters(f -> f.filters(basicAuthFilterFactory.apply(repoServiceAuthConfig))
                                .removeRequestHeader("Cookie")) // Prevents cookie being sent downstream
                        .uri(routeProperties.getRepoServiceUri()))
                .route("frontend", r -> r.path("/**")
                        .uri(routeProperties.getFrontendUri()))
                .build();
    }

    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
    }

}
