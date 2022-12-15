package com.hipravin.devcompanion.repo.model;

public record RepoTextFile(
        RepoFileMetadata metadata,
        String content
) {
}
