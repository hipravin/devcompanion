package com.hipravin.devcompanion.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalManagementPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles({"test","initrunners"})
class SecurityConfigTest {
    @LocalManagementPort
    int managementPort;
    @Autowired
    TestRestTemplate restTemplate;

    @Test
    void test401ActuatorWithoutAuthentication() {
        ResponseEntity<String> response = actuatorNoAuth("");

        assertNotNull(response);
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    void testOkActuatorHealthWithoutAuthentication() {
        ResponseEntity<String> response = actuatorNoAuth("/health/readiness");

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    ResponseEntity<String> actuatorNoAuth(String subUrl) {
        String uri = "http://localhost:" + managementPort + "/actuator" + subUrl;

        ResponseEntity<String> response = restTemplate.exchange(
                uri,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {},
                Map.of());

        return response;
    }
}