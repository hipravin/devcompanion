package com.hipravin.devcompanion.gateway.httpserver;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.SocketUtils;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TrivialHttpServerTest {
    private RestTemplate restTemplate = new RestTemplate();
    private static TrivialHttpServer trivialHttpServer;
    private static int localServerPort;
    private static String baseUrl;

    @BeforeAll
    static void beforeAll() {
        localServerPort = SocketUtils.findAvailableTcpPort();
        trivialHttpServer = new TrivialHttpServer("localhost", localServerPort);
        trivialHttpServer.start();
        baseUrl = "http://localhost:" + localServerPort + "/";
    }

    @AfterAll
    static void afterAll() {
        trivialHttpServer.stop();
    }

    @Test
    void testGetSample1() {
        ResponseEntity<String> sampleGet = restTemplate.getForEntity(baseUrl + "index.html?p=v", String.class);
        assertEquals(HttpStatus.OK, sampleGet.getStatusCode());
        assertEquals("GET /index.html?p=v", sampleGet.getBody());
    }

    @Test
    void testGetSample2() {
        ResponseEntity<String> sampleGet = restTemplate.getForEntity(baseUrl + "/api/v1/articles/search?q=v", String.class);
        assertEquals(HttpStatus.OK, sampleGet.getStatusCode());
        assertEquals("GET /api/v1/articles/search?q=v", sampleGet.getBody());
    }
}