package com.hipravin.devcompanion.repo.dto;

public class RepoFileDescriptionDto {
    private Long id;
    private String fileName;
    private String relativePath;

    private RepoDescriptionDto repo;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getRelativePath() {
        return relativePath;
    }

    public void setRelativePath(String relativePath) {
        this.relativePath = relativePath;
    }

    public RepoDescriptionDto getRepo() {
        return repo;
    }

    public void setRepo(RepoDescriptionDto repo) {
        this.repo = repo;
    }
}
