package com.hipravin.devcompanion.repo.persist;

import com.hipravin.devcompanion.repo.load.RepoLoadService;
import com.hipravin.devcompanion.repo.model.Repo;
import com.hipravin.devcompanion.repo.persist.entity.RepoEntity;
import com.hipravin.devcompanion.repo.persist.entity.RepoTextFileEntity;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.opentest4j.AssertionFailedError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles({"test"})
@TestInstance(TestInstance.Lifecycle.PER_CLASS) //non-static @BeforeAll
class RepoRepositoryTest {
    @Autowired
    RepoRepository repoRepository;
    @Autowired
    RepoFileRepository repoFileRepository;
    @Autowired
    RepoDao repoDao;

    @Autowired
    RepoLoadService repoLoadService;

    @BeforeAll
    void persistSampleRepos() {
        Stream<Repo> repos = repoLoadService.loadAll();

        List<RepoEntity> repoEntities =
                repos.map(r -> repoDao.saveOrUpdate(r))
                        .toList();

        assertNotNull(repos);
        repoEntities.forEach(re -> assertNotNull(re.getId()));

        //check reindex doesn't fail
        repoLoadService.loadAll()
                .forEach(r -> repoDao.saveOrUpdate(r));
    }

    @Test
    void testNotFound() {
        Optional<RepoEntity> re = repoRepository.findByName("not-existing-repo-name");
        assertFalse(re.isPresent());

        List<RepoTextFileEntity> rtfes = repoFileRepository.findByRepoId(-1);
        assertTrue(rtfes.isEmpty());
    }

    @Test
    void testFindRepoAndFiles() {
        RepoEntity re = repoRepository.findByName("test-repo-1")
                .orElseThrow(() -> new AssertionFailedError("expected repo not found"));

        List<RepoTextFileEntity> files = repoFileRepository.findByRepoId(re.getId());

        assertFalse(files.isEmpty());
        files.forEach(f -> {
            assertEquals(f.getRepo().getId(), re.getId());
        });
    }
}