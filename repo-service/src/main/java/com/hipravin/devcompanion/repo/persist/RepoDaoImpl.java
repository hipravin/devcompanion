package com.hipravin.devcompanion.repo.persist;

import com.hipravin.devcompanion.repo.model.Repo;
import com.hipravin.devcompanion.repo.persist.entity.EntityMappers;
import com.hipravin.devcompanion.repo.persist.entity.RepoEntity;
import com.hipravin.devcompanion.repo.persist.entity.RepoTextFileEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public class RepoDaoImpl implements RepoDao {
    private static final Logger log = LoggerFactory.getLogger(RepoDaoImpl.class);

    private final EntityManager em;

    public RepoDaoImpl(EntityManager em) {
        this.em = em;
    }

    @Override
    public RepoEntity save(Repo repo) {
        RepoEntity re = EntityMappers.from(repo);
        List<RepoTextFileEntity> rtfes = EntityMappers.from(repo, re);

        em.persist(re);

        log.debug("File count in repo {}: {}", repo.metadata().name(), rtfes.size());
        for (RepoTextFileEntity rtfe : rtfes) {
            log.debug("Persisting file: {}", rtfe.getRelativePath());
            em.persist(rtfe);
        }

        return re;
    }
}
