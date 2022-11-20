package com.hipravin.devcompanion.repo.model;

import java.util.List;

public record RepoTextFile(
        RepoFileMetadata metadata,
        List<String> lines
) {
}
