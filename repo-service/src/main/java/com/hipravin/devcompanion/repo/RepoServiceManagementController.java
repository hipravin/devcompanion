package com.hipravin.devcompanion.repo;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/manage/")
public class RepoServiceManagementController {


    @PostMapping("/reindex/all")
    ResponseEntity<?> reindexAll() {

        //TODO: use exclusive lock to avoid concurrent reindexes

        return ResponseEntity.accepted().body("Reindex started");
    }
}
