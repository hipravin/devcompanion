package com.hipravin.devcompanion.repo.load;

import com.hipravin.devcompanion.repo.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

import static com.hipravin.devcompanion.repo.load.RepoFileUtils.findReposRecursively;

public class RepoLoadServiceLocalDirectory implements RepoLoadService {
    private static final Logger log = LoggerFactory.getLogger(RepoLoadServiceLocalDirectory.class);

    private final Path reposRootPath;

    public RepoLoadServiceLocalDirectory(Path reposRootPath) {
        this.reposRootPath = reposRootPath;
    }

    @Override
    public Stream<Repo> loadAll() {
        List<Path> repoPaths = findReposRecursively(reposRootPath);
        log.debug("Repositories found under '{}': {}", reposRootPath.toAbsolutePath(), repoPaths);

        return repoPaths.stream()
                .map(this::loadSingleRepo);
    }

    Repo loadSingleRepo(Path repoPath) {
        String name = repoPath.getFileName().toString();
        String relativePath = withForwardSlash(reposRootPath.relativize(repoPath).normalize().toString());
        RepoMetadata metadata = new RepoMetadata(name, relativePath);

        log.debug("Loading files for repo '{}'", repoPath);
        List<RepoTextFile> repoTextFiles = loadRepoFiles(repoPath)
                .toList();
        log.debug("Completed loading files for repo '{}', loaded {} files", repoPath, repoTextFiles.size());
        return new Repo(metadata, repoTextFiles);
    }

    Stream<RepoTextFile> loadRepoFiles(Path repoPath) {
        List<Path> filePaths = RepoFileUtils.findFilesRecursively(repoPath, RepoFileUtils.COMMON_BACKEND_TEXT_FILES);

        return filePaths.stream()
                .map(f -> loadSingleFile(repoPath, f));
    }

    private RepoTextFile loadSingleFile(Path repoRootPath, Path filePath) {
        String fileName = filePath.getFileName().toString();
        String relativePath = withForwardSlash(repoRootPath.relativize(filePath).normalize().toString());
        long size = RepoFileUtils.fileSizeBytes(filePath);

        RepoFileMetadata metadata = new RepoFileMetadata(fileName, relativePath, ContentType.TEXT, size);

        String content = RepoFileUtils.loadTextFileContent(filePath);
        String sanitizedContent = sanitize(content);

        return new RepoTextFile(metadata, sanitizedContent);
    }

    private static String sanitize(String value) {
        String sanitized = value;
        sanitized = sanitized.replaceAll("\u0000", "");

        return sanitized;
    }

    private static String withForwardSlash(String relativePath) {
        return relativePath.replaceAll("\\\\", "/");
    }
}
