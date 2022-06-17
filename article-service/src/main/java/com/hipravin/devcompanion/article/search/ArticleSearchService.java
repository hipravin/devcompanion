package com.hipravin.devcompanion.article.search;

import com.hipravin.devcompanion.article.dto.ArticleDto;
import com.hipravin.devcompanion.article.inmemory.model.Article;

import java.util.List;

public interface ArticleSearchService {
    String TITLE_SEARCH_PREFIX = "title:";


    /**
     * Finds best matches of articles with title containing terms from titleSearchString.
     * If result size exceeds limit then random top limit is returned
     */
    List<Article> findByTitleMatches(String searchString, int limit);

    /**
     * Similar to findByTitleMatches, but takes all article fields into consideration
     */
    List<Article> findByAnyMatches(String searchString, int limit);


    default  List<ArticleDto> search(String query, int limit) {
        List<ArticleDto> result;

        if(query.toLowerCase().startsWith(TITLE_SEARCH_PREFIX)) {
            result = findByTitleMatches(query.substring(TITLE_SEARCH_PREFIX.length()), limit)
                    .stream().map(Article::toDto).toList();
        } else {
            result = findByAnyMatches(query, limit)
                    .stream().map(Article::toDto).toList();
        }

        return result;
    }
}
