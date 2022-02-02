package com.hipravin.devcompanion.article.dto;

import java.util.ArrayList;
import java.util.List;

public class ArticleDto {
    private long id;
    private String title;
    private String description;

    private List<CodeBlockDto> codeBlocks = new ArrayList<>();

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<CodeBlockDto> getCodeBlocks() {
        return codeBlocks;
    }

    public void setCodeBlocks(List<CodeBlockDto> codeBlocks) {
        this.codeBlocks = codeBlocks;
    }
}
