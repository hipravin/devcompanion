package com.hipravin.devcompanion.repo.persist;

import com.hipravin.devcompanion.repo.persist.entity.RepoTextFileEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RepoFileRepository extends JpaRepository<RepoTextFileEntity, Long> {
    List<RepoTextFileEntity> findByRepoId(long repoId);//named
}
