package com.hipravin.devcompanion.repo.model;

public record RepoFileMetadata(
        String fileName,
        String extension,
        RepoFileType fileType,
        long sizeBytes
) {
}
