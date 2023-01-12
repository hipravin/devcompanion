package com.hipravin.devcompanion.repo.load;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.Charset;
import java.nio.charset.MalformedInputException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class RepoFileUtils {
    private static final Logger log = LoggerFactory.getLogger(RepoFileUtils.class);

    public static FileFilter COMMON_BACKEND_TEXT_FILES = new FileFilter(
            Set.of(),
            Set.of(".idea", "node_modules", ".mvn", "target"),
            Set.of("java", "yml", "md", "conf", "txt", "xml", "properties"),
            Set.of("Dockerfile"),
            100 * 1024 * 1024 //100kb
    );

    private RepoFileUtils() {
    }

    /**
     * Filter priority: ignoredSubPaths > maxBytesSize > allowedFileNames > ignoredFiles > allowedExtensions
     */
    public record FileFilter(
            Set<String> ignoredFiles,
            Set<String> ignoredSubPaths,
            Set<String> allowedExtensions,
            Set<String> allowedFileNames,
            long maxSizeBytes) {

        public Predicate<? super Path> asPathPredicate() {
            return f -> accept(f);
        }

        private boolean accept(Path file) {
            String fileName = file.getFileName().toString();
            String extensionLowerCase = detectFileExtension(fileName).toLowerCase();

            if(containsIgnoredSubPaths(file)) {
                log.trace("Ignoring file due to contains ignored sub paths: {}", file);
                return false;
            }

            boolean acceptedByNameAndType = allowedFileNames.contains(fileName)
                    || (!ignoredFiles.contains(fileName) && allowedExtensions.contains(extensionLowerCase));

            return acceptedByNameAndType && (fileSizeBytes(file) < maxSizeBytes);
        }

        private boolean containsIgnoredSubPaths(Path path) {
            if(ignoredSubPaths.isEmpty()) {
                return false;
            }

            Set<String> pathElements = new HashSet<>();
            path.forEach(p -> pathElements.add(p.toString()));
            pathElements.retainAll(ignoredSubPaths);

            return !pathElements.isEmpty();
        }
    }



    static String detectFileExtension(String fileName) {
        int index = fileName.lastIndexOf('.');
        if (index > 0) {
            return fileName.substring(index + 1);
        } else {
            return "";
        }
    }

    static long fileSizeBytes(Path file) {
        try {
            return Files.size(file);
        } catch (IOException e) {
            log.error("Failed to determine size of file: '{}'", file, e);
            return 0;
        }
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


    /**
     * Finds top level directories that match provided {@code directoryPredicate}
     */
    public static List<Path> findReposRecursively(Path searchRoot) {
        List<Path> result = new ArrayList<>();

        Predicate<Path> containsGit = p -> Files.exists(p.resolve(".git"));
        Predicate<Path> containsGitStub = p -> Files.exists(p.resolve(".gitstub"));
        Predicate<Path> isRepoRootPredicate = containsGit.or(containsGitStub);

        try {
            Files.walkFileTree(searchRoot, new SearchFileVisitor(result, isRepoRootPredicate));
        } catch (IOException e) {
            log.error("Failed to find repositories in directory: '{}'", searchRoot);
            throw new UncheckedIOException(e);
        }

        return result;
    }

    public static List<Path> findFilesRecursively(Path dir, FileFilter fileFilter) {
        return findFilesRecursively(dir, fileFilter.asPathPredicate());
    }

    public static List<Path> findFilesRecursively(Path dir, Predicate<? super Path> filePathPredicate) {
        try (Stream<Path> dirWalkStream = Files.walk(dir)) {
            return dirWalkStream
                    .filter(Files::isRegularFile)
                    .filter(filePathPredicate)
                    .toList();
        } catch (IOException e) {
            log.error("Failed to list files in dir '{}'", dir, e);
            throw new UncheckedIOException(e);
        }
    }

    public static String loadTextFileContent(Path filePath) {
        try {
            return tryLoadWithEncoding(filePath, StandardCharsets.UTF_8)
                    .or(() -> tryLoadWithEncoding(filePath, StandardCharsets.UTF_16))
                    .or(() -> tryLoadWithEncoding(filePath, Charset.forName("windows-1251")))
                    .orElse("");
        } catch(UncheckedIOException e) {
            log.error("Failed to load text file from path: '{}'", filePath);
            return "";
        }
    }

    private static Optional<String> tryLoadWithEncoding(Path filePath, Charset charset) {
        try {
            log.trace("Trying to load file from path '{}' with encoding '{}'", filePath, charset);
            return Optional.of(Files.readString(filePath, charset));
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        } catch (UncheckedIOException e) {
            if(e.getCause() instanceof MalformedInputException) {
                log.error("Failed to load text file from path because of wrong encoding {}: '{}'", filePath, charset.name());
                return Optional.empty();
            }
            throw e;
        }
    }

    public static class SearchFileVisitor extends SimpleFileVisitor<Path> {
        private final List<Path> foundDirecotires;
        private final Predicate<? super Path> directoryPredicate;

        public SearchFileVisitor(List<Path> foundDirecotires, Predicate<? super Path> directoryPredicate) {
            this.foundDirecotires = foundDirecotires;
            this.directoryPredicate = directoryPredicate;
        }

        @Override
        public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
            if(directoryPredicate.test(dir)) {
                foundDirecotires.add(dir);
                return FileVisitResult.SKIP_SUBTREE;
            } else {
                return FileVisitResult.CONTINUE;
            }
        }
    }
}
