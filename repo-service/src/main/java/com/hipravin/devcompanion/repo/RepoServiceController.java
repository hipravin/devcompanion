package com.hipravin.devcompanion.repo;

import com.hipravin.devcompanion.repo.dto.FileSnippetsDto;
import com.hipravin.devcompanion.repo.dto.RepoTextFileDto;
import com.hipravin.devcompanion.repo.service.RepoSearchService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;
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

    @GetMapping(path = "/search", params = {"q"})
    public ResponseEntity<?> searchRepoFiles(@RequestParam("q") String query) {
        Page<FileSnippetsDto> repoFiles = repoSearchService.findFilesOrderById(query, DEFAULT_FIRST_PAGE_PAGEABLE);
        return ResponseEntity.ok(repoFiles);
    }

    @GetMapping(path = "/files/{id}")
    public ResponseEntity<?> findFileById(@PathVariable("id") long id) {
        Optional<RepoTextFileDto> fileDtoOptional = repoSearchService.findFileById(id);

        if(fileDtoOptional.isPresent()) {
            return ResponseEntity.ok(fileDtoOptional.get());
        } else {
            log.debug("File not found by id: {}", id);
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping(path = "/files/{id}/raw", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<?> fileRawContentById(@PathVariable("id") long id) {
        Optional<RepoTextFileDto> fileDtoOptional = repoSearchService.findFileById(id);

        if(fileDtoOptional.isPresent()) {
            return ResponseEntity.ok(fileDtoOptional.get().getContent());
        } else {
            log.debug("File not found by id: {}", id);
            return ResponseEntity.notFound().build();
        }
    }
}
