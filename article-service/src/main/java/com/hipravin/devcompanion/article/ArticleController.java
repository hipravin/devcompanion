package com.hipravin.devcompanion.article;

import com.hipravin.devcompanion.article.dto.ArticleDto;
import com.hipravin.devcompanion.article.inmemory.model.Article;
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
    private final ArticleRepository<Article, Long> articleRepository;

    private static final String TITLE_SEARCH_PREFIX = "title:";

    public ArticleController(ApplicationProperties applicationProperties,
                             ArticleRepository<Article, Long> articleRepository) {
        this.applicationProperties = applicationProperties;
        this.articleRepository = articleRepository;
    }

    @GetMapping("/search")
    public ResponseEntity<?> findBySearchString(
            @RequestParam(value = "q", required = true) String query,
            @RequestParam(value = "lmt", required = false) Integer limit) {

        List<ArticleDto> articlesFound = search(query, ensureCorrectLimit(limit));
        return ResponseEntity.ok(articlesFound);
    }

    private List<ArticleDto> search(String query, int limit) {
        List<ArticleDto> result;

        if(query.toLowerCase().startsWith(TITLE_SEARCH_PREFIX)) {
            result = articleRepository.findByTitleMatches(query.substring(TITLE_SEARCH_PREFIX.length()), limit)
                    .stream().map(Article::toDto).toList();
        } else {
            result = articleRepository.findByAnyMatches(query, limit)
                    .stream().map(Article::toDto).toList();
        }

        return result;
    }

    private int ensureCorrectLimit(Integer limitParam) {
        int limit = Optional.ofNullable(limitParam)
                .orElse(applicationProperties.getArticlesDefaultResponseLimit());
        return Math.min(limit, applicationProperties.getArticlesMaxResponseLimit());
    }
}
