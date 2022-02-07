package com.hipravin.devcompanion.article.inmemory;

import com.hipravin.devcompanion.article.ArticleRepository;
import com.hipravin.devcompanion.article.ArticleStorage;
import com.hipravin.devcompanion.article.inmemory.model.Article;

import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class ArticleInMemoryRepository implements ArticleRepository<Article, Long> {

    private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock(false);
    private final Lock readLock = readWriteLock.readLock();
    private final Lock writeLock = readWriteLock.writeLock();

    private final Map<Long, Article> articlesById = new HashMap<>();

    public void fillFromStorage(ArticleStorage articleStorage) {
        Stream<Article> articlesFromStorage = articleStorage.loadAll()
                .map(Article::fromDto);

        updateWithWriteLock(() -> {
            articlesById.clear();
            articlesFromStorage
                    .sequential() //storage may return parallel stream, but HashMap is not thread-safe
                    .forEach(a -> articlesById.put(a.id(), a));
        });
    }

    @Override
    public Optional<Article> findById(Long articleId) {
        return Optional.ofNullable(
                getWithReadLock(() -> articlesById.get(articleId)));
    }

    @Override
    public List<Article> findByTitleMatches(String titleSearchString, int limit) {
        return getWithReadLock(() -> byTitleMatches(titleSearchString, limit));
    }

    private List<Article> byTitleMatches(String titleSearchString, int limit) {
        List<String> termsLowerCase = Arrays.asList(
                titleSearchString.toLowerCase().split("\\s+"));

        return articlesById.values()
                .stream()
                .filter(article -> containsAllTerms(article.title().toLowerCase(), termsLowerCase))
                .limit(limit)
                .toList();
    }

    private static boolean containsAllTerms(String value, List<String> terms) {
        return terms.stream()
                .allMatch(value::contains);
    }

    /**
     * should be used in try-with-resources clause in order to properly close Stream
     * Until returned Stream instance is closed read lock is hold
     */
    @Override
    public Stream<Article> findAll() {
        readLock.lock();

        return articlesById.values()
                .stream()
                .onClose(readLock::unlock); //this looks very dangerous if result stream not closed,
        // but enforces consistency without necessity to create a copy of entire storage
    }

    <T> T getWithReadLock(Supplier<? extends T> supplier) {
        readLock.lock();
        try {
            return supplier.get();
        } finally {
            readLock.unlock();
        }
    }

    void updateWithWriteLock(Runnable updateFunction) {
        writeLock.lock();
        try {
            updateFunction.run();
        } finally {
            writeLock.unlock();
        }
    }
}
