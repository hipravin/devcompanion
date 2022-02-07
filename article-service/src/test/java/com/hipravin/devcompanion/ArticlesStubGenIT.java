package com.hipravin.devcompanion;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Stream;

@Disabled
public class ArticlesStubGenIT {
    @Test
    void testGenerate() throws IOException {
        for (int i = 0; i < 10000; i++) {
             copyReplacing(
                     Paths.get("src/test/resources/articles-stub.yml"),
                     Paths.get("../articles-stub-repo"),
                     "stub", String.valueOf(i), "article-stub-" + i + ".yml");

        }

    }

    static void copyReplacing(Path from, Path rootTo, String replace,
                              String replaceTo,String fileName) throws IOException {
        try(Stream<String> lines = Files.lines(from, StandardCharsets.UTF_8)) {
            List<String> replacedLines = lines
                    .map(s -> s.replaceAll(replace, replaceTo))
                    .toList();

            Files.write(rootTo.resolve(fileName), replacedLines, StandardCharsets.UTF_8);
        }
    }

}
