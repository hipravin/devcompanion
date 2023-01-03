package com.hipravin.devcompanion.repo.dto;

public class RepoTextFileDto {
    private RepoFileDescriptionDto fileDescription;
    private String content;

    public RepoFileDescriptionDto getFileDescription() {
        return fileDescription;
    }

    public void setFileDescription(RepoFileDescriptionDto fileDescription) {
        this.fileDescription = fileDescription;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
