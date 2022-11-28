package com.hipravin.devcompanion.repo;

import com.hipravin.devcompanion.repo.service.RepoSearchService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/repos/")
public class RepoServiceController {
    private static final Logger log = LoggerFactory.getLogger(RepoServiceController.class);

    private final RepoSearchService repoSearchService;

    public RepoServiceController(RepoSearchService repoSearchService) {
        this.repoSearchService = repoSearchService;
    }


}
