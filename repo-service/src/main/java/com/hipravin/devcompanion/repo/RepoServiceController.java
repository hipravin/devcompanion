package com.hipravin.devcompanion.repo;

import com.hipravin.devcompanion.repo.dto.FileSnippetsDto;
import com.hipravin.devcompanion.repo.service.RepoSearchService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ThreadLocalRandom;

@RestController
@RequestMapping("/api/v1/repos/")
public class RepoServiceController {
    private static final Logger log = LoggerFactory.getLogger(RepoServiceController.class);

    private static final int DEFAULT_PAGE_SIZE = 20;
    private static final Pageable DEFAULT_FIRST_PAGE_PAGEABLE = Pageable.ofSize(DEFAULT_PAGE_SIZE);

    private final RepoSearchService repoSearchService;

    public RepoServiceController(RepoSearchService repoSearchService) {
        this.repoSearchService = repoSearchService;
    }

    @GetMapping(value = "/search-sample", params = {"q"})
    public ResponseEntity<?> searchSample(@RequestParam("q") String query) {
        String responseBody = "results of search for query: '%s'".formatted(query);

        if(ThreadLocalRandom.current().nextInt(10) < 2) {
            throw new RuntimeException("Something went wrong randomly");
        }

        return ResponseEntity.ok(responseBody);
    }

    @GetMapping(value = "/search", params = {"q"})
    public ResponseEntity<?> searchRepoFiles(@RequestParam("q") String query) {
        Page<FileSnippetsDto> repoFiles = repoSearchService.findFilesOrderById(query, DEFAULT_FIRST_PAGE_PAGEABLE);

        return ResponseEntity.ok(repoFiles);
    }

}
