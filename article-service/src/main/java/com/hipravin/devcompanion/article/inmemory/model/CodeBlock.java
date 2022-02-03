package com.hipravin.devcompanion.article.inmemory.model;

import com.hipravin.devcompanion.article.dto.CodeBlockDto;

public record CodeBlock(
        String title,
        String code) {

    public static CodeBlock fromDto(CodeBlockDto dto) {
        return new CodeBlock(dto.getTitle(), dto.getCode());
    }

    public CodeBlockDto toDto() {
        return new CodeBlockDto(title, code);
    }
}
