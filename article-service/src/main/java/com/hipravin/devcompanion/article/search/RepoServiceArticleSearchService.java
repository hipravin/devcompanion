package com.hipravin.devcompanion.article.search;

import com.hipravin.devcompanion.api.PageRequest;
import com.hipravin.devcompanion.api.PageUtil;
import com.hipravin.devcompanion.api.PagedResponse;
import com.hipravin.devcompanion.article.inmemory.model.Article;
import com.hipravin.devcompanion.client.repo.Converters;
import com.hipravin.devcompanion.client.repo.RepoSearchClient;
import com.hipravin.devcompanion.repo.dto.FileSnippetsDto;
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
    public PagedResponse<Article> findByAnyMatches(String searchString, PageRequest pageRequest) {

        PagedResponse<FileSnippetsDto> repos =
                repoSearchClient.search(searchString, pageRequest.getPage(), pageRequest.getPageSize());

        PagedResponse<Article> articles = PageUtil.map(repos, Converters::fromRepoFileSnippets);
        return articles;

    }

    @Override
    public List<Article> findByAnyMatches(String searchString, int limit) {
        List<Article> articles = repoSearchClient.search(searchString, 0, limit)
                .getContent()
                .stream()
                .map(Converters::fromRepoFileSnippets)
                .toList();

        return articles;
    }
}
