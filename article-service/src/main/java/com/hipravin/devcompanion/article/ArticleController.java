package com.hipravin.devcompanion.article;

import com.hipravin.devcompanion.article.dto.ArticleDto;
import com.hipravin.devcompanion.article.search.ArticleSearchService;
import com.hipravin.devcompanion.config.ApplicationProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/articles")
public class ArticleController {
    private static final Logger log = LoggerFactory.getLogger(ArticleController.class);

    private final ApplicationProperties applicationProperties;
    private final ArticleSearchService articleSearchService;

    public ArticleController(ApplicationProperties applicationProperties, ArticleSearchService articleSearchService) {
        this.applicationProperties = applicationProperties;
        this.articleSearchService = articleSearchService;
    }

    @GetMapping("/search")
    public ResponseEntity<?> findBySearchString(
            @RequestParam(value = "q", required = true) String query,
            @RequestParam(value = "lmt", required = false) Integer limit) {

        List<ArticleDto> articlesFound = articleSearchService.search(query, ensureCorrectLimit(limit));

        return ResponseEntity.ok(articlesFound);
    }

    private int ensureCorrectLimit(Integer limitParam) {
        int limit = Optional.ofNullable(limitParam)
                .orElse(applicationProperties.getArticlesDefaultResponseLimit());
        return Math.min(limit, applicationProperties.getArticlesMaxResponseLimit());
    }
}
