package com.hipravin.devcompanion.repo;

import com.hipravin.devcompanion.api.PageRequest;
import com.hipravin.devcompanion.api.PagedResponse;
import com.hipravin.devcompanion.repo.dto.FileSnippetsDto;
import com.hipravin.devcompanion.repo.dto.RepoTextFileDto;
import com.hipravin.devcompanion.repo.service.RepoSearchService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.util.Optional;

@RestController
@Validated
@RequestMapping("/api/v1/repos/")
public class RepoServiceController {
    private static final Logger log = LoggerFactory.getLogger(RepoServiceController.class);

    private final RepoSearchService repoSearchService;

    public RepoServiceController(RepoSearchService repoSearchService) {
        this.repoSearchService = repoSearchService;
    }

    @GetMapping(path = "/search", params = {"q"})
    public ResponseEntity<PagedResponse<FileSnippetsDto>> searchRepoFiles(
            @RequestParam("q") @NotBlank String query,
            @RequestParam(value = "page", required = false, defaultValue = "0") @Min(0) int page,
            @RequestParam(value = "pageSize", required = false, defaultValue = "5") @Min(1) int pageSize) {

        Page<FileSnippetsDto> repoFilesPage = repoSearchService.findFilesOrderById(query, new PageRequest(page, pageSize));

        PagedResponse<FileSnippetsDto> response = new PagedResponse<>(repoFilesPage.getContent(), repoFilesPage.getNumber(),
                repoFilesPage.getSize(), repoFilesPage.getTotalElements(), repoFilesPage.getTotalPages());

        return ResponseEntity.ok(response);
    }

    @GetMapping(path = "/files/{id}")
    public ResponseEntity<Object> findFileById(@PathVariable("id") long id) {
        Optional<RepoTextFileDto> fileDtoOptional = repoSearchService.findFileById(id);

        if(fileDtoOptional.isPresent()) {
            return ResponseEntity.ok(fileDtoOptional.get());
        } else {
            return repoFileNotFound(id);
        }
    }

    @GetMapping(path = "/files/{id}/raw", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<Object> fileRawContentById(@PathVariable("id") long id) {
        Optional<RepoTextFileDto> fileDtoOptional = repoSearchService.findFileById(id);

        if(fileDtoOptional.isPresent()) {
            return ResponseEntity.ok(fileDtoOptional.get().getContent());
        } else {
            return repoFileNotFound(id);
        }
    }

    ResponseEntity<Object> repoFileNotFound(long id) {
        log.debug("File not found by id: {}", id);
        return new ResponseEntity<>("Repo file not found with id: " + id, HttpStatus.NOT_FOUND);
    }
}
