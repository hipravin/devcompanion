package com.hipravin.devcompanion.repo.persist.entity;

import com.hipravin.devcompanion.repo.model.Repo;
import com.hipravin.devcompanion.repo.model.RepoTextFile;

import java.util.ArrayList;
import java.util.List;

public final class EntityMappers {
    private EntityMappers() {
    }

    /**
     * Maps model to entity leaving {@code id} and {@code repoEntity} empty.
     */
    public static RepoTextFileEntity from(RepoTextFile rtf) {
        RepoTextFileEntity rtfe = new RepoTextFileEntity();

        rtfe.setName(rtf.metadata().fileName());
        rtfe.setRelativePath(rtf.metadata().relativePath());
        rtfe.setSizeBytes(rtf.metadata().sizeBytes());
        rtfe.setContentType(rtf.metadata().contentType());
        rtfe.setContent(rtf.content());

        return rtfe;
    }

    /**
     * Maps model to entity leaving id empty.
     */
    public static RepoEntity from(Repo r) {
        RepoEntity re = new RepoEntity();
        re.setName(r.metadata().name());
        re.setRelativePath(r.metadata().relativePath());

        return re;
    }

    /**
     * Retrieves file entities from Repo and fills repo field with provided repoEntity
     */
    public static List<RepoTextFileEntity> from(Repo repo, RepoEntity repoEntity) {
        return repo.contents().stream()
                .map(rtf -> from(rtf))
                .peek(rtfe -> rtfe.setRepo(repoEntity))
                .toList();
    }
}
