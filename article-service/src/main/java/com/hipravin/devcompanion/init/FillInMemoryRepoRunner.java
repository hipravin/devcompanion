package com.hipravin.devcompanion.init;

import com.hipravin.devcompanion.article.ArticleStorage;
import com.hipravin.devcompanion.article.inmemory.ArticleInMemoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Profile(InitRunner.INIT_RUNNERS_PROFILE)
@Order(InitRunner.ORDER_FILL_INMEMORY)
public class FillInMemoryRepoRunner implements ApplicationRunner {
    private static final Logger log = LoggerFactory.getLogger(FillInMemoryRepoRunner.class);

    private final ArticleStorage articleStorage;
    private final ArticleInMemoryRepository articleInMemoryRepository;

    public FillInMemoryRepoRunner(ArticleStorage articleStorage,
                                  ArticleInMemoryRepository articleInMemoryRepository) {
        this.articleStorage = articleStorage;
        this.articleInMemoryRepository = articleInMemoryRepository;
    }

    @Override
    public void run(ApplicationArguments args) {
        log.info("Filling in-memory from storage started...");
        articleInMemoryRepository.fillFromStorage(articleStorage);

        log.info("Filling in-memory from storage - completed. Size: {}", articleInMemoryRepository.count());
    }
}
