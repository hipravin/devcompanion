package com.hipravin.devcompanion.article.search;

import com.hipravin.devcompanion.api.PageRequest;
import com.hipravin.devcompanion.api.PagedResponse;
import com.hipravin.devcompanion.article.inmemory.model.Article;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

@Service
@Qualifier("aggregateArticleSearchService")
public class AggregateArticleSearchService implements ArticleSearchService {
    private final ArticleSearchService inMemoryArticleSearchService;
    private final ArticleSearchService repoArticleSearchService;

    public AggregateArticleSearchService(
            @Qualifier("inMemoryArticleSearchService") ArticleSearchService inMemoryArticleSearchService,
            @Qualifier("repoArticleSearchService") ArticleSearchService repoArticleSearchService) {
        this.inMemoryArticleSearchService = inMemoryArticleSearchService;
        this.repoArticleSearchService = repoArticleSearchService;
    }

    @Override
    public List<Article> findByAnyMatches(String searchString, int limit) {
        CompletableFuture<List<Article>> resultRepoAsync = CompletableFuture
                .supplyAsync(() -> repoArticleSearchService.findByAnyMatches(searchString, limit));
        List<Article> resultInMemory = inMemoryArticleSearchService.findByAnyMatches(searchString, limit);
        List<Article> resultRepo = resultRepoAsync.join();

        return Stream.of(resultInMemory, resultRepo)
                .flatMap(l -> l.stream())
                .toList();
    }

    @Override
    public PagedResponse<Article> findByAnyMatches(String searchString, PageRequest pageRequest) {
        PagedResponse<Article> resultInMemory = inMemoryArticleSearchService.findByAnyMatches(searchString, pageRequest);

        //look for repositories only if no specific article is found
        if(resultInMemory.getTotalElements() == 0) {
            PagedResponse<Article> resultRepo = repoArticleSearchService.findByAnyMatches(searchString, pageRequest);
            return resultRepo;
        } else {
            return resultInMemory;
        }
    }
}
