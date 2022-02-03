package com.hipravin.devcompanion.article.inmemory;

import com.hipravin.devcompanion.article.inmemory.model.Article;
import com.hipravin.devcompanion.article.yml.ArticleYmlFileStorage;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ArticleInMemoryRepositoryTest {
    static final Path articleSampleRepo = Paths.get("src/test/resources/articles-repo-sample");
    static final ArticleYmlFileStorage articleYmlFileStorage = new ArticleYmlFileStorage(articleSampleRepo);
    static final ArticleInMemoryRepository articleInMemoryRepository = new ArticleInMemoryRepository();

    public static final int lmt = 2;

    @BeforeAll
    static void setUp() {
        articleInMemoryRepository.fillFromStorage(articleYmlFileStorage);
    }

    @Test
    void testFindByIdNotFound() {
        Optional<Article> notFound = articleInMemoryRepository.findById(-1L);
        assertTrue(notFound.isEmpty());
    }

    @Test
    void testFindById2() {
        long id2 = 1000002;
        Optional<Article> a2optional = articleInMemoryRepository.findById(id2);
        assertTrue(a2optional.isPresent());

        Article a2 = a2optional.get();
        assertEquals(id2, a2.id());
        assertEquals("some2 java", a2.title());
    }

    @Test
    void testFindAll() {
        try(Stream<Article> articles = articleInMemoryRepository.findAll()) {
            assertEquals(3, articles.count());
        }
    }

    @Test
    void testFindByTermsEmpty() {
        String terms = "notfound";

        List<Article> result = articleInMemoryRepository.findByTitleMatches(terms, lmt);

        assertEquals(0, result.size());
    }

    @Test
    void testFindByTermsTooMuch() {
        String terms = "";
        List<Article> result = articleInMemoryRepository.findByTitleMatches(terms, lmt);

        assertEquals(lmt, result.size());
    }

    @Test
    void testFindByTerms01() {
        String terms = "spring javA classpath";

        List<Article> result = articleInMemoryRepository.findByTitleMatches(terms, lmt);

        assertEquals(1, result.size());
        assertEquals(1000001L, result.get(0).id());
    }

    @Test
    void testFindByTerms02() {
        String terms = "jav";

        List<Article> result = articleInMemoryRepository.findByTitleMatches(terms, lmt);
        assertEquals(2, result.size());
    }
}