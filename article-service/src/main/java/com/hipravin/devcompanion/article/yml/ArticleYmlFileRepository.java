package com.hipravin.devcompanion.article.yml;

import com.hipravin.devcompanion.article.ArticleRepository;
import com.hipravin.devcompanion.article.dto.ArticleDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.util.List;
import java.util.stream.Stream;

public class ArticleYmlFileRepository implements ArticleRepository {
    private static final Logger log = LoggerFactory.getLogger(ArticleYmlFileRepository.class);

    private final Path articlesRootPath;

    public ArticleYmlFileRepository(Path articlesRootPath) {
        this.articlesRootPath = articlesRootPath;
    }

    @Override
    public Stream<ArticleDto> findAll() {
        return findArticleFilesRecursively(articlesRootPath).parallelStream()
                .map(ArticleYmlFileRepository::loadFromPath);
    }

    static ArticleDto loadFromPath(Path articleFilePath) {
        Yaml yaml = new Yaml(new Constructor(ArticleDto.class));
        try (BufferedReader reader = new BufferedReader(
                new FileReader(articleFilePath.toFile(), StandardCharsets.UTF_8))) {
            return yaml.load(reader);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    static List<Path> findArticleFilesRecursively(Path root) {
        PathMatcher matcher = FileSystems.getDefault().getPathMatcher("glob:**.yml");
        try {
            return Files.walk(root)
                    .filter(Files::isRegularFile)
                    .filter(matcher::matches)
                    .toList();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
