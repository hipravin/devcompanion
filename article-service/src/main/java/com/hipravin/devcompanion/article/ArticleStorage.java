package com.hipravin.devcompanion.article;

import com.hipravin.devcompanion.article.dto.ArticleDto;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.ForkJoinPool;
import java.util.stream.Stream;

public interface ArticleStorage {
    Stream<ArticleDto> loadAll();
}
