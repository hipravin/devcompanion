package com.hipravin.devcompanion.article.inmemory.model;

import com.hipravin.devcompanion.article.dto.ArticleDto;
import com.hipravin.devcompanion.article.dto.CodeBlockDto;
import com.hipravin.devcompanion.article.dto.LinkDto;

import java.util.List;
import java.util.stream.Stream;

public record Article(
        long id,
        String title,
        String description,
        List<Link> links,
        List<CodeBlock>codeBlocks) {

    public static Article fromDto(ArticleDto articleDto) {
        List<CodeBlock> codeBlocks = articleDto.getCodeBlocks().stream()
                .map(CodeBlock::fromDto).toList();

        List<Link> links = articleDto.getLinks().stream()
                .map(Link::from).toList();

        return new Article(articleDto.getId(), articleDto.getTitle(), articleDto.getDescription(),
                links, codeBlocks);
    }

    public ArticleDto toDto() {
        List<CodeBlockDto> codeBlockDtos = codeBlocks().stream()
                .map(CodeBlock::toDto)
                .toList();

        List<LinkDto> linkDtos = links().stream()
                .map(Link::toDto)
                .toList();

        return new ArticleDto(id, title, description, linkDtos, codeBlockDtos);
    }

    public Stream<String> textBlocks() {
        Stream<String> articleFields = Stream.of(title, description);
        Stream<String> blockFields = codeBlocks.stream()
                .flatMap(CodeBlock::textBlocks);

        return Stream.concat(articleFields, blockFields);
    }
}
