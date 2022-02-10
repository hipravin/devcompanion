package com.hipravin.devcompanion.article;

import com.hipravin.devcompanion.config.ApplicationProperties;
import com.hipravin.devcompanion.article.dto.ArticleDto;
import com.hipravin.devcompanion.article.inmemory.model.Article;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/articles")
public class ArticleController {
    private static final Logger log = LoggerFactory.getLogger(ArticleController.class);

    private final ApplicationProperties applicationProperties;
    private final ArticleRepository<Article, Long> articleRepository;

    public ArticleController(ApplicationProperties applicationProperties,
                             ArticleRepository<Article, Long> articleRepository) {
        this.applicationProperties = applicationProperties;
        this.articleRepository = articleRepository;
    }

    @GetMapping("/log")
    public ResponseEntity<?> log(HttpServletRequest request, HttpServletResponse response) {
        System.out.println(request);
        return ResponseEntity.ok("article-service-log");
    }

    @GetMapping("/search")
    public ResponseEntity<?> findBySearchString(
            @RequestParam(value = "q", required = true) String query,
            @RequestParam(value = "lmt", required = false) Integer limit) {

        List<ArticleDto> articlesFound = search(query, ensureCorrectLimit(limit));
        return ResponseEntity.ok(articlesFound);
    }

    private List<ArticleDto> search(String query, int limit) {
        List<ArticleDto> result = articleRepository.findByTitleMatches(query, limit)
                .stream().map(Article::toDto).toList();

        return result;
    }

    private int ensureCorrectLimit(Integer limitParam) {
        int limit = Optional.ofNullable(limitParam)
                .orElse(applicationProperties.getArticlesDefaultResponseLimit());
        return Math.min(limit, applicationProperties.getArticlesMaxResponseLimit());
    }
}
