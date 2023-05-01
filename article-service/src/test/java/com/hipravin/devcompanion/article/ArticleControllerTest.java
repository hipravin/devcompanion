package com.hipravin.devcompanion.article;

import com.hipravin.devcompanion.api.PagedResponse;
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
        ResponseEntity<PagedResponse<ArticleDto>> response = search("java", 0, 10, false);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    void testPage0_1() {
        ResponseEntity<PagedResponse<ArticleDto>> response = search("java", 0, 1, true);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().getContent().size());
        assertTrue(response.getBody().getContent().get(0).getTitle().toLowerCase().contains("java"));
    }

    @Test
    void testFind() {
        ResponseEntity<PagedResponse<ArticleDto>> response = search("java @Value", 0, 20, true);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        List<ArticleDto> articleDtos = response.getBody().getContent();
        Set<Long> ids = articleDtos.stream()
                .map(ArticleDto::getId)
                .collect(Collectors.toSet());

        assertEquals(1, articleDtos.size());
        assertTrue(articleDtos.stream().allMatch(a -> a.getTitle().toLowerCase().contains("java")));
        assertEquals(Set.of(1000001L), ids);
    }

    ResponseEntity<PagedResponse<ArticleDto>> search(String query, int page, int pageSize, boolean withBearerAuth) {
        String uri = "http://localhost:" + port + "/api/v1/articles/search?q={query}&page={page}&pageSize={pageSize}";

        HttpHeaders headers = new HttpHeaders();
        if (withBearerAuth) {
            headers.add("Authorization", "Bearer " + eternalJwt);
        }

        ResponseEntity<PagedResponse<ArticleDto>> response = restTemplate.exchange(
                uri,
                HttpMethod.GET,
                new HttpEntity<>(headers),
                new ParameterizedTypeReference<>() {
                },
                Map.of("query", query, "page", page, "pageSize", pageSize));

        return response;
    }


}