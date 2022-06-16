package com.hipravin.devcompanion.article.yml;

import com.hipravin.devcompanion.article.ArticleStorage;
import com.hipravin.devcompanion.article.dto.ArticleDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.util.List;
import java.util.stream.Stream;

public class ArticleYmlFileStorage implements ArticleStorage {
    private static final Logger log = LoggerFactory.getLogger(ArticleYmlFileStorage.class);

    private final Path articlesRootPath;

    public ArticleYmlFileStorage(Path articlesRootPath) {
        this.articlesRootPath = articlesRootPath;
    }

    @Override
    public Stream<ArticleDto> loadAll() {
        return findArticleFilesRecursively(articlesRootPath).parallelStream()
                .map(ArticleYmlFileStorage::loadFromPath);
    }

    static ArticleDto loadFromPath(Path articleFilePath) {
        Yaml yaml = new Yaml(new Constructor(ArticleDto.class));
        try (BufferedReader reader = new BufferedReader(
                new FileReader(articleFilePath.toFile(), StandardCharsets.UTF_8))) {
            return yaml.load(reader);
        } catch (IOException e) {
            log.error("failed to load article from path '{}'", articleFilePath, e);
            throw new UncheckedIOException(e);
        }
    }

    static List<Path> findArticleFilesRecursively(Path root) {
        log.info("loading articles from path '{}'", root.toString());

        PathMatcher matcher = FileSystems.getDefault().getPathMatcher("glob:**.yml");
        try (Stream<Path> walkRoot = Files.walk(root)) {
            return walkRoot
                    .filter(Files::isRegularFile)
                    .filter(matcher::matches)
                    .toList();
        } catch (IOException e) {
            log.error("failed to load articles from path '{}'", root, e);
            throw new UncheckedIOException(e);
        }
    }
}
