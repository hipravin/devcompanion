package com.hipravin.devcompanion.repo.load;

import com.hipravin.devcompanion.repo.model.Repo;
import com.hipravin.devcompanion.repo.model.RepoTextFile;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.opentest4j.AssertionFailedError;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RepoLoadServiceLocalDirectoryTest {
    static Path testResourcesPath = Paths.get("src/test/resources");
    static Path testRepoRoot = testResourcesPath.resolve("test-repo-root");

    @Test
    void testLoadRepo() {
        RepoLoadService loadService = new RepoLoadServiceLocalDirectory(testRepoRoot);
        List<Repo> repos = loadService.loadAll().toList();

        assertEquals(3, repos.size());

        Repo r1 = repos.stream()
                .filter(r -> "test-repo-1".equals(r.metadata().name()))
                .findAny()
                .orElseThrow(() -> new AssertionFailedError("repo not found"));

        RepoTextFile arrayListFile = r1.contents().stream()
                .filter(f -> "ArrayList.java".equals(f.metadata().fileName()))
                .findAny().orElseThrow(() -> new AssertionFailedError("file not found"));

        assertEquals("subdir/ArrayList.java", arrayListFile.metadata().relativePath());
    }
}