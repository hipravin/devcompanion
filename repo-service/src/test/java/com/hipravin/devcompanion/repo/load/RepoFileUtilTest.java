package com.hipravin.devcompanion.repo.load;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.UncheckedIOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RepoFileUtilTest {
    static Path testResourcesPath = Paths.get("src/test/resources");
    static Path testRepoRoot = testResourcesPath.resolve("test-repo-root");

    @Test
    void testSubdirectoriesBadPath() {
        assertThrows(UncheckedIOException.class, () -> {
            RepoFileUtils.subdirectories(Paths.get("bad-path"));
        });
    }

    @Test
    void testSubdirectories() {
        List<Path> subs = RepoFileUtils.subdirectories(testRepoRoot);

        assertNotNull(subs);
        assertEquals(3, subs.size());
        assertEquals("test-repo-0", subs.get(0).getFileName().toString());
    }

    @Test
    void testLoadTextFile() {
        Path sampleFile = RepoFileUtils.subdirectories(testRepoRoot).get(0).resolve("SampleClass.java");

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

    @Test
    @Disabled
    void playground() {
        Path dir = Paths.get("../").toAbsolutePath().normalize();

        RepoFileUtils.findFilesRecursively(dir, RepoFileUtils.COMMON_BACKEND_TEXT_FILES)
                .stream().limit(10_000)
                .forEach(p -> System.out.println(p));

    }

}