package com.hipravin.devcompanion.repo.model;

import java.util.List;

/**
 * Comprehensive information about actual repository state: metadata and contents.
 * It's assumed that any repository with its required contents (which are mostly text files)
 * can entirely be loaded, fit into memory and processed as required.
 * This will simplify overall loading and indexing logic.
 */
public record Repo(
        RepoMetadata metadata,
        List<RepoTextFile> contents
) {
}
