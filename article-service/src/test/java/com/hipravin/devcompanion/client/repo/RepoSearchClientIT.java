package com.hipravin.devcompanion.client.repo;

import com.hipravin.devcompanion.api.PagedResponse;
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
        PagedResponse<FileSnippetsDto> found = repoSearchClient.search("java", null, null);

        assertEquals(5, found.getPageSize());
    }

    @Test
    void testSearchRepoFileEnormousPage() {
        PagedResponse<FileSnippetsDto> found = repoSearchClient.search("java", 0, Integer.MAX_VALUE);

        assertEquals(1, found.getTotalPages());
    }
}