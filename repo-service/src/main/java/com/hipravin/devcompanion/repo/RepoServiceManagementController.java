package com.hipravin.devcompanion.repo;

import com.hipravin.devcompanion.repo.service.ConcurrentIndexOperationException;
import com.hipravin.devcompanion.repo.service.IndexService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/manage/")
public class RepoServiceManagementController {
    private static final Logger log = LoggerFactory.getLogger(RepoServiceManagementController.class);

    private final IndexService indexService;

    public RepoServiceManagementController(IndexService indexService) {
        this.indexService = indexService;
    }

    @PostMapping("/reindex/all")
    ResponseEntity<Object> reindexAll() {
        try {
            indexService.index();
            //TODO: better return some key-value json
            return ResponseEntity.accepted().body("Index started");
        } catch (ConcurrentIndexOperationException e) {
            return ResponseEntity.ok("Already in progress");
        }
    }
}
