package com.hipravin.devcompanion.article.yml;

import com.hipravin.devcompanion.article.dto.ArticleDto;
import org.junit.jupiter.api.Test;

import java.io.UncheckedIOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class ArticleYmlFileRepositoryTest {
    final Path articleSampleRepo = Paths.get("src/test/resources/articles-repo-sample");

    @Test
    void testFindAll() {
        ArticleYmlFileRepository repository = new ArticleYmlFileRepository(articleSampleRepo);

        List<ArticleDto> articles = repository.findAll().toList();
        assertEquals(3, articles.size());

        Set<Long> ids = articles.stream()
                .map(ArticleDto::getId)
                .collect(Collectors.toSet());

        assertEquals(Set.of(1000001L, 1000002L, 1000003L), ids);
    }

    @Test
    void testLoadException() {
        assertThrows(UncheckedIOException.class,
                () -> ArticleYmlFileRepository.loadFromPath(Paths.get("badpath")));
    }

    @Test
    void testLoadFromPath() {
        Path sampleArticlePath = ArticleYmlFileRepository.findArticleFilesRecursively(articleSampleRepo)
                .stream().findFirst().orElseThrow();

        ArticleDto sampleArticle = ArticleYmlFileRepository.loadFromPath(sampleArticlePath);
        assertNotNull(sampleArticle);

        assertEquals(1000001, sampleArticle.getId());
        assertEquals(2, sampleArticle.getCodeBlocks().size());
    }

    @Test
    void testFindArticlesRecursively() {

        List<String> articleFiles = ArticleYmlFileRepository.findArticleFilesRecursively(articleSampleRepo)
                .stream()
                .map(p -> p.getFileName().toString())
                .toList();

        assertEquals(3, articleFiles.size());
        assertEquals(Set.of("sample-article.yml", "sample-article-2.yml", "sample-article-3.yml"),
                Set.copyOf(articleFiles));
    }
}