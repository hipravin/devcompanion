package com.hipravin.devcompanion.repo.load;

import org.junit.jupiter.api.Test;

import java.io.UncheckedIOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FileUtilTest {
    static Path testResourcesPath = Paths.get("src/test/resources");
    static Path testRepoRoot = testResourcesPath.resolve("test-repo-root");

    @Test
    void testSubdirectoriesBadPath() {
        assertThrows(UncheckedIOException.class, () -> {
            FileUtil.subdirectories(Paths.get("bad-path"));
        });
    }

    @Test
    void testSubdirectories() {
        List<Path> subs = FileUtil.subdirectories(testRepoRoot);

        assertNotNull(subs);
        assertEquals(3, subs.size());
        assertEquals("test-repo-0", subs.get(0).getFileName().toString());
    }

    @Test
    void testLoadTextFile() {
        Path sampleFile = FileUtil.subdirectories(testRepoRoot).get(0).resolve("SampleClass.java");

        List<String> sampleLines = FileUtil.loadTextFileContents(sampleFile);
        assertNotNull(sampleLines);
        assertEquals(569, sampleLines.size());

        sampleLines.forEach(s -> System.out.println(s));
    }
}