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
import org.springframework.security.oauth2.client.web.server.ServerOAuth2AuthorizedClientRepository;
import org.springframework.security.oauth2.client.web.server.WebSessionServerOAuth2AuthorizedClientRepository;

@SpringBootApplication
@EnableConfigurationProperties({
        AppRouteProperties.class})
public class GatewayApplication {

    @Autowired
    private TokenRelayGatewayFilterFactory tokenRelayfilterFactory;

    @Autowired
    private GatewayLoggingDiagnosticFilterFactory gatewayDiagnosticFilter;

    @Autowired
    private BasicAuthGatewayFilterFactory repoServiceBasicAuthFilterFactory;

    @Autowired
    private AppRouteProperties routeProperties;

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("article-service", r -> r.path("/api/v1/articles/**", "/api/v1/users/**")
                        .filters(f -> f.filters(tokenRelayfilterFactory.apply(), gatewayDiagnosticFilter.apply())
                                .removeRequestHeader("Cookie")) // Prevents cookie being sent downstream
                        .uri(routeProperties.getArticleServiceUri()))
                .route("repo-service", r -> r.path("/api/v1/repos/**")
                        .filters(f -> f.filters(repoServiceBasicAuthFilterFactory.apply())
                                .removeRequestHeader("Cookie")) // Prevents cookie being sent downstream
                        .uri(routeProperties.getRepoServiceUri()))
                .route("frontend", r -> r.path("/**")
                        .uri(routeProperties.getFrontendUri()))
                .build();
    }

    //https://github.com/spring-projects/spring-security/issues/7889
    @Bean
    public ServerOAuth2AuthorizedClientRepository authorizedClientRepository() {
        return new WebSessionServerOAuth2AuthorizedClientRepository();
    }

//    @Bean
//    public RedisSerializer<Object> springSessionRedisSerializer() {
//        return new GenericJackson2JsonRedisSerializer(objectMapper());
//    }
//
//    private ObjectMapper objectMapper() {
//        ObjectMapper mapper = new ObjectMapper();
//        mapper.registerModules(SecurityJackson2Modules.getModules(getClass().getClassLoader()));
//        return mapper;
//    }

    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
    }

}
