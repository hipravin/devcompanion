package com.hipravin.devcompanion.article;

import com.hipravin.devcompanion.article.dto.ArticleDto;

import java.util.stream.Stream;

public interface ArticleRepository {
    Stream<ArticleDto> findAll();
}
