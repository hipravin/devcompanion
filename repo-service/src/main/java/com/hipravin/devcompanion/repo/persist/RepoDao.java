package com.hipravin.devcompanion.repo.persist;

import com.hipravin.devcompanion.repo.model.Repo;
import com.hipravin.devcompanion.repo.persist.entity.RepoEntity;

public interface RepoDao {
    /**
     * Transforms to Entity and saves RepoEntity with containing RepoTextFileEntities.
     * Deletes old data by name if found.
     */
    RepoEntity saveOrUpdate(Repo repo);
}
