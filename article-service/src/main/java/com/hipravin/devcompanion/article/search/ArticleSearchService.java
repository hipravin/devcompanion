package com.hipravin.devcompanion.article.search;

import com.hipravin.devcompanion.api.PageRequest;
import com.hipravin.devcompanion.api.PageUtil;
import com.hipravin.devcompanion.api.PagedResponse;
import com.hipravin.devcompanion.article.dto.ArticleDto;
import com.hipravin.devcompanion.article.inmemory.model.Article;

import java.util.List;

public interface ArticleSearchService {
    PagedResponse<Article> findByAnyMatches(String searchString, PageRequest pageRequest);

    List<Article> findByAnyMatches(String searchString, int limit);

    default  List<ArticleDto> search(String query, int limit) {
        return findByAnyMatches(query, limit)
                    .stream().map(Article::toDto).toList();
    }

    default  PagedResponse<ArticleDto> search(String query, PageRequest pageRequest) {
        return PageUtil.map(findByAnyMatches(query, pageRequest), Article::toDto);
    }
}
