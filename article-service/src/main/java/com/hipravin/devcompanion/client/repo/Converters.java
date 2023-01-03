package com.hipravin.devcompanion.client.repo;

import com.hipravin.devcompanion.article.inmemory.model.Article;
import com.hipravin.devcompanion.article.inmemory.model.CodeBlock;
import com.hipravin.devcompanion.article.inmemory.model.Link;
import com.hipravin.devcompanion.repo.dto.CodeSnippetDto;
import com.hipravin.devcompanion.repo.dto.FileSnippetsDto;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class Converters {
    private Converters() {}

    public static Article fromRepoFileSnippets(FileSnippetsDto fileSnippets) {

        String title = "Repository: %s".formatted(fileSnippets.getFile().getRepo().getRelativePath());
        String description = "File: %s".formatted(fileSnippets.getFile().getRelativePath());
        List<CodeBlock> blocks = fileSnippets.getSnippets().stream()
                .map(s -> fromSnippet(s))
                .toList();

        List<Link> links = new ArrayList<>();
        links.add(new Link(fileSnippets.getFile().getFileName(), rawFileUrl(fileSnippets.getFile().getId())));

        return new Article(fileSnippets.getFile().getId(), title, description, links, blocks);
    }

    static CodeBlock fromSnippet(CodeSnippetDto snippetDto) {
        String title = "Lines %d - %d".formatted(snippetDto.getLineFrom(), snippetDto.getLineTo());

        return new CodeBlock(title, snippetDto.getContent());
    }

    static String rawFileUrl(long id) {
        return "/api/v1/repos/files/%d/raw".formatted(id);
    }

}
