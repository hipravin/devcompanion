package com.hipravin.devcompanion.article.inmemory;

import com.hipravin.devcompanion.article.ArticleRepository;
import com.hipravin.devcompanion.article.ArticleStorage;
import com.hipravin.devcompanion.article.ArticleStorageUpdateWatcher;
import com.hipravin.devcompanion.article.inmemory.model.Article;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class ArticleInMemoryRepository implements ArticleRepository<Article, Long> {
    private static final Logger log = LoggerFactory.getLogger(ArticleInMemoryRepository.class);

    private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock(false);
    private final Lock readLock = readWriteLock.readLock();
    private final Lock writeLock = readWriteLock.writeLock();

    private volatile Map<Long, Article> articlesById = new HashMap<>();

    public synchronized void fillFromStorage(ArticleStorage articleStorage) {
        log.info("Start loading in-memory from storage, size before: {}", articlesById.size());

        Stream<Article> articlesFromStorage = articleStorage.loadAll()
                .map(Article::fromDto);

        final Map<Long, Article> articleByIdUpdated = new HashMap<>();

        articlesFromStorage
                .sequential() //storage may return parallel stream, but HashMap is not thread-safe
                .forEach(a -> putOnDuplicateThrow(articleByIdUpdated, a));//in case of exception on reload keep old reference

        //won't switch references until all readers are completed and write lock is acquired
        updateWithWriteLock(() -> {
            articlesById = articleByIdUpdated;
        });
        log.info("Finished loading in-memory from storage, size after: {}", articlesById.size());
    }

    static void putOnDuplicateThrow(Map<Long, Article> map, Article a) {
        map.merge(a.id(), a, (a1, a2) -> {
            throw new IllegalArgumentException(
                    "Duplicate key for article with id: %d, '%s' / '%s'".formatted(a.id(), a1.title(), a2.title()));
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

    @Override
    public List<Article> findByAnyMatches(String titleSearchString, int limit) {
        return getWithReadLock(() -> byAnyMatches(titleSearchString, limit));
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
    private List<Article> byAnyMatches(String titleSearchString, int limit) {
        List<String> termsLowerCase = Arrays.asList(
                titleSearchString.toLowerCase().split("\\s+"));

        return articlesById.values()
                .stream()
                .filter(article -> containsAllTerms(article.textBlocks(), termsLowerCase))
                .limit(limit)
                .toList();
    }
    private static boolean containsAllTerms(String value, List<String> terms) {
        return terms.stream()
                .allMatch(value::contains);
    }
    private static boolean containsAllTerms(Stream<String> textBlocks, List<String> terms) {
        final Set<String> notMatchedTerms = new HashSet<>(terms);

        textBlocks.forEach(tb -> {
            notMatchedTerms.removeIf(term -> tb.toLowerCase().contains(term));
        });

        return notMatchedTerms.isEmpty();
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

    @Override
    public long count() {
        return getWithReadLock(articlesById::size);
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
