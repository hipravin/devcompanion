package com.hipravin.devcompanion.repo.load;

import com.hipravin.devcompanion.repo.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

import static com.hipravin.devcompanion.repo.load.FileUtil.subdirectories;

public class RepoLoadServiceLocalDirectory implements RepoLoadService {
    private static final Logger log = LoggerFactory.getLogger(RepoLoadServiceLocalDirectory.class);

    private final Path reposRootPath;

    public RepoLoadServiceLocalDirectory(Path reposRootPath) {
        this.reposRootPath = reposRootPath;
    }

    @Override
    public Stream<Repo> loadAll() {
        List<Path> repoPaths = subdirectories(reposRootPath);
        log.debug("Repositories found under '{}': {}", reposRootPath.toAbsolutePath(), repoPaths);

        return repoPaths.stream()
                .map(this::loadSingleRepo);
    }

    Repo loadSingleRepo(Path repoPath) {
        String name = repoPath.getFileName().toString();
        String location = repoPath.toAbsolutePath().normalize().toString();
        RepoMetadata metadata = new RepoMetadata(name, location);

        log.debug("Loading files for repo '{}'", repoPath);
        List<RepoTextFile> repoTextFiles = loadRepoFiles(repoPath)
                .toList();
        log.debug("Completed loading files for repo '{}', loaded {} files", repoPath, repoTextFiles.size());
        return new Repo(metadata, repoTextFiles);
    }

    Stream<RepoTextFile> loadRepoFiles(Path repoPath) {
        List<Path> filePaths = FileUtil.findFilesRecursively(repoPath, FileUtil.COMMON_BACKEND_TEXT_FILES);

        return filePaths.stream()
                .map(f -> loadSingleFile(repoPath, f));
    }

    private RepoTextFile loadSingleFile(Path repoRootPath, Path filePath) {
        String fileName = filePath.getFileName().toString();
        String relativePath = repoRootPath.relativize(filePath).normalize().toString();
        long size = FileUtil.fileSizeBytes(filePath);

        RepoFileMetadata metadata = new RepoFileMetadata(fileName, relativePath, ContentType.TEXT, size);

        String content = FileUtil.loadTextFileContent(filePath);
        String sanitizedContent = sanitize(content);

        return new RepoTextFile(metadata, sanitizedContent);
    }

    private static String sanitize(String value) {
        String sanitized = value;
        sanitized = sanitized.replaceAll("\u0000", "");

        return sanitized;
    }


}
