package com.hipravin.devcompanion;

import com.hipravin.devcompanion.api.PageRequest;
import com.hipravin.devcompanion.api.PageUtil;
import com.hipravin.devcompanion.api.PagedResponse;
import com.hipravin.devcompanion.article.inmemory.model.Article;
import com.hipravin.devcompanion.article.search.ArticleSearchService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@Qualifier("repoArticleSearchService")
@Primary
public class TestRepoServiceArticleSearchService implements ArticleSearchService {
    private static final Logger log = LoggerFactory.getLogger(TestRepoServiceArticleSearchService.class);

    @Override
    public List<Article> findByAnyMatches(String searchString, int limit) {
        log.debug("Test findByAnyMatches with empty response");
        return Collections.emptyList();
    }

    @Override
    public PagedResponse<Article> findByAnyMatches(String searchString, PageRequest pageRequest) {
        log.debug("Test findByAnyMatches with empty response");
        return PageUtil.emptyPagedResponse(pageRequest.getPageSize());
    }
}
