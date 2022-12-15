package com.hipravin.devcompanion.repo.model;

public record RepoFileMetadata(
        String fileName,
        String relativePath,
        ContentType contentType,
        long sizeBytes
) {
}
