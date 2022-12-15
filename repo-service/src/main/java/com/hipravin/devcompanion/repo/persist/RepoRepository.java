package com.hipravin.devcompanion.repo.persist;

import com.hipravin.devcompanion.repo.persist.entity.RepoEntity;
import com.hipravin.devcompanion.repo.persist.entity.RepoTextFileEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RepoRepository extends JpaRepository<RepoEntity, Long> {
    Optional<RepoEntity> findByName(String name);
}
