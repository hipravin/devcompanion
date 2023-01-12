package com.hipravin.devcompanion.repo.load;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.UncheckedIOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class RepoFileUtilTest {
    static Path testResourcesPath = Paths.get("src/test/resources");
    static Path testRepoRoot = testResourcesPath.resolve("test-repo-root");

    @Test
    void testSubdirectoriesBadPath() {
        Path notExistingDirectory = Paths.get("bad-path");

        assertThrows(UncheckedIOException.class, () -> {
            RepoFileUtils.subdirectories(notExistingDirectory);
        });
    }

    @Test
    void testSubdirectories() {
        List<Path> subs = RepoFileUtils.subdirectories(testRepoRoot);

        assertNotNull(subs);
        assertEquals(3, subs.size());
        assertEquals(Set.of("test-repo-0","test-repo-1","test-repo-2"),
                subs.stream().map(p -> p.getFileName().toString()).collect(Collectors.toSet()));
    }

    @Test
    void testLoadTextFile() {
        Path sampleFile = RepoFileUtils.subdirectories(testRepoRoot).stream()
                .filter(p -> p.getFileName().toString().contains("test-repo-0"))
                .findAny().orElseThrow().resolve("SampleClass.java");

        String sampleContent = RepoFileUtils.loadTextFileContent(sampleFile);
        assertNotNull(sampleContent);
        assertFalse(sampleContent.isEmpty());

        System.out.println(sampleContent);
    }

    @Test
    void testFindRecursively() {
        Path dir = testRepoRoot.resolve("test-repo-1");

        List<Path> files = RepoFileUtils.findFilesRecursively(dir, RepoFileUtils.COMMON_BACKEND_TEXT_FILES);

        assertEquals(5, files.size());
    }

}