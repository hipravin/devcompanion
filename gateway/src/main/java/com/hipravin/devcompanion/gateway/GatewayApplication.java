package com.hipravin.devcompanion.gateway;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;

@Controller
@SpringBootApplication
@EnableConfigurationProperties({
        AppRouteProperties.class})
public class GatewayApplication {

    @Autowired
    private AppRouteProperties appRouteProperties;

//    @Autowired
//    private TokenRelayGatewayFilterFactory filterFactory;

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("article-service", r -> r.path("/api/v1/articles/**")
//                        .filters(f -> f.filters(filterFactory.apply())
//                                .removeRequestHeader("Cookie")) // Prevents cookie being sent downstream
                        .uri(appRouteProperties.getArticleServiceUri()))
                .build();
    }

//    @GetMapping("/")
//    public String index(Model model,
//                        @RegisteredOAuth2AuthorizedClient OAuth2AuthorizedClient authorizedClient,
//                        @AuthenticationPrincipal OAuth2User oauth2User) {
//        model.addAttribute("userName", oauth2User.getName());
//        model.addAttribute("clientName", authorizedClient.getClientRegistration().getClientName());
//        model.addAttribute("userAttributes", oauth2User.getAttributes());
//        return "index";
//    }



    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
    }

}
