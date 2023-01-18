package com.hipravin.devcompanion.article.inmemory;

import com.hipravin.devcompanion.api.PageRequest;
import com.hipravin.devcompanion.api.PageUtil;
import com.hipravin.devcompanion.api.PagedResponse;
import com.hipravin.devcompanion.article.ArticleRepository;
import com.hipravin.devcompanion.article.ArticleStorage;
import com.hipravin.devcompanion.article.inmemory.model.Article;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
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

    private static final Supplier<Map<Long, Article>> newArticlesMapSupplier = TreeMap::new;
    private Map<Long, Article> articlesById = newArticlesMapSupplier.get();

    public synchronized void fillFromStorage(ArticleStorage articleStorage) {
        log.info("Start loading in-memory from storage, size before: {}", articlesById.size());

        Stream<Article> articlesFromStorage = articleStorage.loadAll()
                .map(Article::fromDto);

        final Map<Long, Article> articleByIdUpdated = newArticlesMapSupplier.get();

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

    public PagedResponse<Article> findByAnyMatches(String searchString, PageRequest pageRequest) {
        return getWithReadLock(() -> byAnyMatchesPaged(searchString, pageRequest));
    }

    public List<Article> findByAnyMatches(String searchString, int limit) {
        return getWithReadLock(() -> byAnyMatches(searchString, limit));
    }

    private PagedResponse<Article> byAnyMatchesPaged(String searchString, PageRequest pageRequest) {
        List<String> termsLowerCase = Arrays.asList(searchString.toLowerCase().split("\\s+"));

        Supplier<Stream<Article>> matchedArticlesStreamSupplier = () -> articlesById.values()
                .stream()
                .filter(article -> containsAllTerms(article.textBlocks(), termsLowerCase));

        long totalElements = matchedArticlesStreamSupplier.get().count();

        List<Article> pageContent = Collections.emptyList();
        int elementsToSkip = pageRequest.getPage() * pageRequest.getPageSize();
        if(elementsToSkip < totalElements) {
            pageContent = matchedArticlesStreamSupplier.get()
                    .skip(elementsToSkip)
                    .limit(pageRequest.getPageSize())
                    .toList();
        }

        int totalPages = PageUtil.totalPages(totalElements, pageRequest.getPageSize());
        return new PagedResponse<>(pageContent, pageRequest.getPage(), pageRequest.getPageSize(), totalElements, totalPages);
    }

    private List<Article> byAnyMatches(String searchString, int limit) {
        List<String> termsLowerCase = Arrays.asList(
                searchString.toLowerCase().split("\\s+"));

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

        Iterator<String> textBlockIterator = textBlocks.iterator();
        while(textBlockIterator.hasNext() && !notMatchedTerms.isEmpty()) {
            String textBlock = textBlockIterator.next();
            notMatchedTerms.removeIf(term -> textBlock.toLowerCase().contains(term));
        }

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
