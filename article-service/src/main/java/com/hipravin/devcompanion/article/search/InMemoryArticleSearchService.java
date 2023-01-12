package com.hipravin.devcompanion.article.search;

import com.hipravin.devcompanion.article.inmemory.ArticleInMemoryRepository;
import com.hipravin.devcompanion.article.inmemory.model.Article;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Qualifier("inMemoryArticleSearchService")
public class InMemoryArticleSearchService implements ArticleSearchService {
    private final ArticleInMemoryRepository articleInMemoryRepository;

    public InMemoryArticleSearchService(ArticleInMemoryRepository articleInMemoryRepository) {
        this.articleInMemoryRepository = articleInMemoryRepository;
    }

    @Override
    public List<Article> findByTitleMatches(String searchString, int limit) {
        return articleInMemoryRepository.findByTitleMatches(searchString, limit);
    }

    @Override
    public List<Article> findByAnyMatches(String searchString, int limit) {
        return articleInMemoryRepository.findByAnyMatches(searchString, limit);
    }
}
