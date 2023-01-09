package com.hipravin.devcompanion.gateway;

import com.hipravin.devcompanion.gateway.httpserver.TrivialHttpServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpHeaders;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.util.SocketUtils;

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = {
                "ARTICLE_SERVICE_URI=http://localhost:${services.server.port}",
                "REPO_SERVICE_URI=http://localhost:${services.server.port}",
                "FRONTEND_URI=http://localhost:${services.server.port}"
        })
@ActiveProfiles({"test"})
class GatewayApplicationTest {

    @LocalServerPort
    int localServerPort = 0;

    static int servicesServerPort = 0;

    WebTestClient webClient;
    String baseUri;

    @MockBean
    private ReactiveClientRegistrationRepository clientRegistrationRepository;

    @BeforeAll
    public static void beforeClass() {
        servicesServerPort = SocketUtils.findAvailableTcpPort();
        System.setProperty("services.server.port", String.valueOf(servicesServerPort));

        TrivialHttpServer testHttpServer = new TrivialHttpServer("localhost", servicesServerPort);
        testHttpServer.start();
    }

    @AfterAll
    public static void afterClass() {
        System.clearProperty("services.server.port");
    }

    @BeforeEach
    public void setup() {
        baseUri = "http://localhost:" + localServerPort;

        this.webClient = WebTestClient.bindToServer().responseTimeout(Duration.ofSeconds(10)).baseUrl(baseUri).build();
    }

    @Test
    void testRoutesResponds() {
         webClient.get().uri("/api/v1/articles/search?q=kubectl").exchange().expectStatus().isOk();
         webClient.get().uri("/api/v1/repos/search?q=kubectl").exchange().expectStatus().isOk();
         webClient.get().uri("/index.html").exchange().expectStatus().isOk();
    }

    @Test
    void testRoutes() {
        webClient.get().uri("/api/v1/articles/search?q=kubectl").exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .consumeWith(result -> assertThat(result.getResponseBody()).contains("articles/search?q=kubectl"));

        webClient.get().uri("/api/v1/repos/search?q=kubectl").exchange().expectStatus().isOk()
                .expectBody(String.class)
                .consumeWith(result -> assertThat(result.getResponseBody()).contains("repos/search?q=kubectl"));

        webClient.get().uri("/index.html").exchange().expectStatus().isOk()
                .expectBody(String.class)
                .consumeWith(result -> assertThat(result.getResponseBody()).contains("index.html"));
    }
}