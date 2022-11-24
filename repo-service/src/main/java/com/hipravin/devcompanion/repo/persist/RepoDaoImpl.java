package com.hipravin.devcompanion.repo.persist;

import com.hipravin.devcompanion.repo.model.Repo;
import com.hipravin.devcompanion.repo.persist.entity.EntityMappers;
import com.hipravin.devcompanion.repo.persist.entity.RepoEntity;
import com.hipravin.devcompanion.repo.persist.entity.RepoTextFileEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public class RepoDaoImpl implements RepoDao {
    private final EntityManager em;

    public RepoDaoImpl(EntityManager em) {
        this.em = em;
    }

    @Override
    public RepoEntity save(Repo repo) {
        RepoEntity re = EntityMappers.from(repo);
        List<RepoTextFileEntity> rtfes = EntityMappers.from(repo, re);

        em.persist(re);
        rtfes.forEach(em::persist);

        return re;
    }
}
