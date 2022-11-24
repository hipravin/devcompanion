package com.hipravin.devcompanion.repo.persist;

import com.hipravin.devcompanion.repo.model.Repo;
import com.hipravin.devcompanion.repo.persist.entity.RepoEntity;

public interface RepoDao {
    /**
     * Transforms to Entity and saves new RepoEntity with containing RepoTextFileEntities
     */
    RepoEntity save(Repo repo);
}
