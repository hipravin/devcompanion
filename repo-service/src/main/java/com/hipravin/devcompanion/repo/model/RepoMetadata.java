package com.hipravin.devcompanion.repo.model;

public record RepoMetadata(
        RepoType repoType,
        String name,
        String location,
        String descriptionShort) {
}
