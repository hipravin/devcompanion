package com.hipravin.devcompanion.article.search;

import com.hipravin.devcompanion.article.inmemory.ArticleInMemoryRepository;
import com.hipravin.devcompanion.article.inmemory.model.Article;
import com.hipravin.devcompanion.client.repo.Converters;
import com.hipravin.devcompanion.client.repo.RepoSearchClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Qualifier("repoArticleSearchService")
public class RepoServiceArticleSearchService implements ArticleSearchService {
    private final RepoSearchClient repoSearchClient;

    public RepoServiceArticleSearchService(RepoSearchClient repoSearchClient) {
        this.repoSearchClient = repoSearchClient;
    }

    @Override
    public List<Article> findByTitleMatches(String searchString, int limit) {
        return findByAnyMatches(searchString, limit);
    }

    @Override
    public List<Article> findByAnyMatches(String searchString, int limit) {
        List<Article> artices = repoSearchClient.search(searchString)
                .map(fs -> Converters.fromRepoFileSnippets(fs))
                .toList();

        return artices;

    }
}
