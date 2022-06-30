package com.hipravin.devcompanion.article.yml;

import com.hipravin.devcompanion.article.dto.ArticleDto;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.UncheckedIOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@Disabled
class ArticleYmlMainFileStorageIT {
    final Path articleSampleRepo = Paths.get("../article-repo");

    @Test
    void testFindAll() {
        ArticleYmlFileStorage repository = new ArticleYmlFileStorage(articleSampleRepo);

        List<ArticleDto> articles = repository.loadAll().toList();

        System.out.println("Loaded:" + articles.size());
    }
}