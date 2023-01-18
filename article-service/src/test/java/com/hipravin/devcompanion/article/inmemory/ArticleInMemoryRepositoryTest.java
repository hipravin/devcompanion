package com.hipravin.devcompanion.article.inmemory;

import com.hipravin.devcompanion.api.PageRequest;
import com.hipravin.devcompanion.api.PagedResponse;
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
    static final Path articleSampleRepo = Paths.get("src/test/resources/article-repo-sample");
    static final ArticleYmlFileStorage articleYmlFileStorage = new ArticleYmlFileStorage(articleSampleRepo);
    static final ArticleInMemoryRepository articleInMemoryRepository = new ArticleInMemoryRepository();

    public static final int lmt = 2;

    @BeforeAll
    static void setUp() {
        articleInMemoryRepository.fillFromStorage(articleYmlFileStorage);
    }

    @Test
    void testCount() {
        long count = articleInMemoryRepository.count();
        assertEquals(3, count);
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
    void testFindPaged() {
        PagedResponse<Article> p21 = articleInMemoryRepository.findByAnyMatches("o", new PageRequest(1,2));

        PagedResponse<Article> p11 = articleInMemoryRepository.findByAnyMatches("o", new PageRequest(0,1));
        PagedResponse<Article> p12 = articleInMemoryRepository.findByAnyMatches("o", new PageRequest(1,1));
        PagedResponse<Article> p13 = articleInMemoryRepository.findByAnyMatches("o", new PageRequest(2,1));
        PagedResponse<Article> p14 = articleInMemoryRepository.findByAnyMatches("o", new PageRequest(3,1));


        assertEquals(3, p11.getTotalElements());
        assertEquals(3, p12.getTotalElements());
        assertEquals(3, p13.getTotalElements());
        assertEquals(3, p14.getTotalElements());
        assertEquals(3, p21.getTotalElements());

        assertEquals(1000001, p11.getContent().get(0).id());
        assertEquals(1000002, p12.getContent().get(0).id());
        assertEquals(1000003, p13.getContent().get(0).id());
        assertTrue(p14.getContent().isEmpty());
        assertEquals(1000003, p21.getContent().get(0).id());
    }

    @Test
    void testFindBynyMatches01() {
        String terms = "spring Bufferedreader";

        List<Article> result = articleInMemoryRepository.findByAnyMatches(terms, lmt);

        assertEquals(1, result.size());
        assertEquals(1000001, result.get(0).id());
    }

    @Test
    void testFindBynyMatches02() {
        String terms = "t3";

        List<Article> result = articleInMemoryRepository.findByAnyMatches(terms, lmt);

        assertEquals(1, result.size());
        assertEquals(1000003, result.get(0).id());
    }
}