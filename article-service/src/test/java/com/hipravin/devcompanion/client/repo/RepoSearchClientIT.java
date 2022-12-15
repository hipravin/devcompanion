package com.hipravin.devcompanion.client.repo;

import com.hipravin.devcompanion.repo.dto.FileSnippetsDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ActiveProfiles({"test", "it"})
class RepoSearchClientIT {

    @Autowired
    RepoSearchClient repoSearchClient;

    @Test
    void testSearchRepoFileSnippets() {
        Page<FileSnippetsDto> found = repoSearchClient.search("java");

        assertEquals(20, found.getNumberOfElements());
    }
}