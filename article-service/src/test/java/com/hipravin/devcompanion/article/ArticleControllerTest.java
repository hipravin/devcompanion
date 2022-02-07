package com.hipravin.devcompanion.article;

import com.hipravin.devcompanion.article.dto.ArticleDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles({"test","initrunners"})
class ArticleControllerTest {
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void testFindLimit1() {
        ResponseEntity<List<ArticleDto>> response = search("java", 1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        assertTrue(response.getBody().get(0).getTitle().toLowerCase().contains("java"));
    }

    @Test
    void testFind() {
        ResponseEntity<List<ArticleDto>> response = search("java", 20);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        List<ArticleDto> articleDtos = response.getBody();
        Set<Long> ids = articleDtos.stream()
                .map(ArticleDto::getId)
                .collect(Collectors.toSet());

        assertEquals(2, articleDtos.size());
        assertTrue(articleDtos.stream().allMatch(a -> a.getTitle().toLowerCase().contains("java")));
        assertEquals(Set.of(1000001L,1000002L), ids);
    }

    ResponseEntity<List<ArticleDto>> search(String query, int limit) {
        String uri = "http://localhost:" + port + "/api/v1/articles/search?q={query}&lmt={limit}";

        ResponseEntity<List<ArticleDto>> response = restTemplate.exchange(
                uri,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {},
                Map.of("query", query, "limit", limit));

        return response;
    }


}