package com.hipravin.devcompanion.article;

import com.hipravin.devcompanion.article.dto.ArticleDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles({"test"})
class ArticleControllerTest {
    @LocalServerPort
    int port;

    @Autowired
    TestRestTemplate restTemplate;

    @Value("${app.test.eternaljwt}")
    String eternalJwt;

    @Test
    void testFindUnauthorized() {
        ResponseEntity<List<ArticleDto>> response = search("java", 1, false);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    void testFindLimit1() {
        ResponseEntity<List<ArticleDto>> response = search("java", 1, true);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        assertTrue(response.getBody().get(0).getTitle().toLowerCase().contains("java"));
    }

    @Test
    void testFindByTitle() {
        ResponseEntity<List<ArticleDto>> response = search("title:java", 20, true);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        List<ArticleDto> articleDtos = response.getBody();
        Set<Long> ids = articleDtos.stream()
                .map(ArticleDto::getId)
                .collect(Collectors.toSet());

        assertEquals(2, articleDtos.size());
        assertTrue(articleDtos.stream().allMatch(a -> a.getTitle().toLowerCase().contains("java")));
        assertEquals(Set.of(1000001L,1000002L), ids);
    }
    @Test
    void testFind() {
        ResponseEntity<List<ArticleDto>> response = search("java @Value", 20, true);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        List<ArticleDto> articleDtos = response.getBody();
        Set<Long> ids = articleDtos.stream()
                .map(ArticleDto::getId)
                .collect(Collectors.toSet());

        assertEquals(1, articleDtos.size());
        assertTrue(articleDtos.stream().allMatch(a -> a.getTitle().toLowerCase().contains("java")));
        assertEquals(Set.of(1000001L), ids);
    }

    ResponseEntity<List<ArticleDto>> search(String query, int limit, boolean withBearerAuth) {
        String uri = "http://localhost:" + port + "/api/v1/articles/search?q={query}&lmt={limit}";

        HttpHeaders headers = new HttpHeaders();
        if(withBearerAuth) {
            headers.add("Authorization", "Bearer " + eternalJwt);
        }

        ResponseEntity<List<ArticleDto>> response = restTemplate.exchange(
                uri,
                HttpMethod.GET,
                new HttpEntity<>(headers),
                new ParameterizedTypeReference<>() {},
                Map.of("query", query, "limit", limit));

        return response;
    }


}