package com.hipravin.devcompanion.repo.dto;

import java.util.List;

public class FileSearchResponseDto {
    private RepoFileDescriptionDto file;
    private List<CodeSnippetDto> snippets;

    public RepoFileDescriptionDto getFile() {
        return file;
    }

    public void setFile(RepoFileDescriptionDto file) {
        this.file = file;
    }

    public List<CodeSnippetDto> getSnippets() {
        return snippets;
    }

    public void setSnippets(List<CodeSnippetDto> snippets) {
        this.snippets = snippets;
    }
}
