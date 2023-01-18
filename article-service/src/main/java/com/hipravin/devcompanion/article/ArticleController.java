package com.hipravin.devcompanion.article;

import com.hipravin.devcompanion.api.PageRequest;
import com.hipravin.devcompanion.api.PagedResponse;
import com.hipravin.devcompanion.article.dto.ArticleDto;
import com.hipravin.devcompanion.article.dto.SearchRequestDto;
import com.hipravin.devcompanion.article.search.ArticleSearchService;
import com.hipravin.devcompanion.config.ApplicationProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RestController
@Validated
@RequestMapping("/api/v1/articles")
public class ArticleController {
    private static final Logger log = LoggerFactory.getLogger(ArticleController.class);

    private final ApplicationProperties applicationProperties;
    private final ArticleSearchService articleSearchService;

    public ArticleController(ApplicationProperties applicationProperties,
                             @Qualifier("aggregateArticleSearchService") ArticleSearchService articleSearchService) {
        this.applicationProperties = applicationProperties;
        this.articleSearchService = articleSearchService;
    }

    @GetMapping("/search-old")
    public ResponseEntity<List<ArticleDto>> findBySearchString(
            @RequestParam(name = "q", required = true) String query,
            @RequestParam(name = "lmt", required = false) Integer limit) {

        List<ArticleDto> articlesFound = articleSearchService.search(query, ensureCorrectLimit(limit));

        return ResponseEntity.ok(articlesFound);
    }

    @GetMapping(value = "/search", params = {"q"})
    public ResponseEntity<PagedResponse<ArticleDto>> findBySearchString(
            @RequestParam("q") @NotBlank String query,
            @RequestParam(value = "page", required = false, defaultValue = "0") @Min(0) int page,
            @RequestParam(value = "pageSize", required = false, defaultValue = "5") @Min(1) int pageSize) {


        PagedResponse<ArticleDto> articlesFound = articleSearchService.search(query, new PageRequest(page, pageSize));

        return ResponseEntity.ok(articlesFound);
    }

    //synthetic endpoint just to test POST method which should be csrf-secure
    @PostMapping("/search-post")
    public ResponseEntity<List<ArticleDto>> findBySearchStringPost(
            @RequestBody SearchRequestDto searchRequestDto) {

        Objects.requireNonNull(searchRequestDto);
        List<ArticleDto> articlesFound = articleSearchService.search(searchRequestDto.getQuery(), ensureCorrectLimit(searchRequestDto.getLimit()));

        return ResponseEntity.ok(articlesFound);
    }

    private int ensureCorrectLimit(Integer limitParam) {
        int limit = Optional.ofNullable(limitParam)
                .orElse(applicationProperties.getArticlesDefaultResponseLimit());
        return Math.min(limit, applicationProperties.getArticlesMaxResponseLimit());
    }
}
