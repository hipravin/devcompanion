package com.hipravin.devcompanion.repo;

import com.hipravin.devcompanion.repo.load.RepoLoadService;
import com.hipravin.devcompanion.repo.model.Repo;
import com.hipravin.devcompanion.repo.persist.RepoDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

@RestController
@RequestMapping("/api/v1/manage/")
public class RepoServiceManagementController {
    private static final Logger log = LoggerFactory.getLogger(RepoServiceManagementController.class);

    private final RepoLoadService repoLoadService;
    private final RepoDao repoDao;

    public RepoServiceManagementController(RepoLoadService repoLoadService, RepoDao repoDao) {
        this.repoLoadService = repoLoadService;
        this.repoDao = repoDao;
    }

    @PostMapping("/reindex/all")
    ResponseEntity<?> reindexAll() {
        log.info("Reindex started.");
        Stream<Repo> repos = repoLoadService.loadAll();

        CompletableFuture.runAsync(() -> {
            repos.forEach(r -> repoDao.save(r));
            log.info("Reindex completed.");
        }).exceptionally(t -> {
            log.error("Reindex failed: " + t.getMessage(), t);
            return null;
        });

        return ResponseEntity.accepted().body("Reindex started");
    }
}
