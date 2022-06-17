package com.hipravin.devcompanion.article;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public interface ArticleRepository<T, ID> {
    Optional<T> findById(ID articleId);

    /**
     * should be used in try-with-resources clause in order to properly close Stream
     */
    Stream<T> findAll();

    long count();
}
