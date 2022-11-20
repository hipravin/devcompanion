package com.hipravin.devcompanion.repo.load;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class FileUtil {
    private static final Logger log = LoggerFactory.getLogger(FileUtil.class);

    private FileUtil() {
    }

    /**
     * finds all subdirectories (i.e. walk with max depth equal to one)
     */
    public static List<Path> subdirectories(Path directoryPath) {
        var directoriesOnly = (DirectoryStream.Filter<Path>) Files::isDirectory;

        try (DirectoryStream<Path> paths = Files.newDirectoryStream(directoryPath, directoriesOnly)) {
            return StreamSupport.stream(paths.spliterator(), false)
                    .toList();
        } catch (IOException e) {
            log.error("Failed to load subdirectories from path: '{}'", directoryPath);
            throw new UncheckedIOException(e);
        }
    }

    public static List<String> loadTextFileContents(Path filePath) {
        try(Stream<String> lines = Files.lines(filePath, StandardCharsets.UTF_8)) {
            return lines.toList();
        } catch (IOException e) {
            log.error("Failed to load text file from path: '{}'", filePath);
            throw new UncheckedIOException(e);
        }
    }
}
