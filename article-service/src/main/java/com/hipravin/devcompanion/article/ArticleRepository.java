package com.hipravin.devcompanion.article;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public interface ArticleRepository<ID, T> {
    Optional<T> findById(ID articleId);

    /**
     * Finds best matches of articles with title containing terms from titleSearchString.
     * If result size exceeds limit then random top limit is returned
     */
    List<T> findByTitleMatches(String titleSearchString, int limit);

    /**
     * should be used in try-with-resources clause in order to properly close Stream
     */
    Stream<T> findAll();
}