package com.hipravin.devcompanion.repo.persist;

import com.hipravin.devcompanion.repo.model.Repo;
import com.hipravin.devcompanion.repo.persist.entity.RepoEntity;
import com.hipravin.devcompanion.repo.persist.entity.RepoTextFileEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface RepoDao {
    /**
     * Transforms to Entity and saves RepoEntity with containing RepoTextFileEntities.
     * Deletes old data by name if found.
     */
    RepoEntity saveOrUpdate(Repo repo);

    List<RepoTextFileEntity> search(String... searchTerms);

    /**
     * {@code pageable.sort is ignored}
     */
    Page<RepoTextFileEntity> search(String[] searchTerms, Pageable pageable);
}
