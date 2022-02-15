package com.hipravin.devcompanion.article;

import com.hipravin.devcompanion.article.inmemory.ArticleInMemoryRepository;
import com.hipravin.devcompanion.config.ApplicationProperties;
import com.hipravin.devcompanion.util.FileWatchUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
public class ArticleStorageUpdateWatcher implements DisposableBean {
    private static final Logger log = LoggerFactory.getLogger(ArticleStorageUpdateWatcher.class);

    final ExecutorService watchExecutorService = Executors.newSingleThreadExecutor();

    final ApplicationProperties applicationProperties;
    final ArticleStorage articleStorage;
    final ArticleInMemoryRepository articleInMemoryRepository;

    public ArticleStorageUpdateWatcher(ApplicationProperties applicationProperties,
                                       ArticleStorage articleStorage,
                                       ArticleInMemoryRepository articleInMemoryRepository) {
        this.applicationProperties = applicationProperties;
        this.articleStorage = articleStorage;
        this.articleInMemoryRepository = articleInMemoryRepository;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void onApplicationReady(ApplicationReadyEvent applicationReadyEvent) {
        log.info("Application ready, starting watching on dir: '{}'", applicationProperties.getActiclesYamlPath());

        Runnable watchRunnable = FileWatchUtil
                .watchForUpdatesRunnable(
                        Paths.get(applicationProperties.getActiclesYamlPath()),
                        this::handleStorageUpdate);

        watchExecutorService.submit(watchRunnable);
    }

    void handleStorageUpdate(Set<Path> paths) {
        try {
            log.info("Storage paths updated: {}", paths);
            articleInMemoryRepository.fillFromStorage(articleStorage);
        } catch (RuntimeException e) {
            log.error(e.getMessage(), e);
        }
    }

    @Override
    public void destroy() throws Exception {
        watchExecutorService.shutdownNow();
    }
}
