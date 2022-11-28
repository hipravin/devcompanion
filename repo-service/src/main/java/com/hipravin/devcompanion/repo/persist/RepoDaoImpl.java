package com.hipravin.devcompanion.repo.persist;

import com.hipravin.devcompanion.repo.model.Repo;
import com.hipravin.devcompanion.repo.persist.entity.EntityMappers;
import com.hipravin.devcompanion.repo.persist.entity.RepoEntity;
import com.hipravin.devcompanion.repo.persist.entity.RepoTextFileEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Repository
@Transactional
public class RepoDaoImpl implements RepoDao {
    private static final Logger log = LoggerFactory.getLogger(RepoDaoImpl.class);

    private static int MAX_SEARCH_TERMS = 10;
    private static final String FROM_REPO_FILE = "select * from repo_file where ";
    private static final String COUNT_FROM_REPO_FILE = "select count(*) as total from repo_file where ";
    private static final String ORDER_BY_ID = " order by id ";

    private final EntityManager em;

    public RepoDaoImpl(EntityManager em) {
        this.em = em;
    }

    @Override
    public RepoEntity saveOrUpdate(Repo repo) {
        RepoEntity re = EntityMappers.from(repo);
        List<RepoTextFileEntity> rtfes = EntityMappers.from(repo, re);

        deleteRepoByRelativePath(re.getRelativePath());

        em.persist(re);

        log.debug("File count in repo {}: {}", repo.metadata().name(), rtfes.size());
        for (RepoTextFileEntity rtfe : rtfes) {
            log.debug("Persisting file: {}", rtfe.getRelativePath());
            em.persist(rtfe);
        }

        return re;
    }


    private static String[] ensureCorrect(String... searchTerms) {
        Objects.requireNonNull(searchTerms);
        if(searchTerms.length == 0) {
            throw new IllegalArgumentException("searchTerms should be not empty");
        }
        if(searchTerms.length > MAX_SEARCH_TERMS) {
            if(log.isDebugEnabled()) {
                log.debug("Query too long, max count: {}, provided: {}, value: {}",
                        MAX_SEARCH_TERMS, searchTerms.length, Arrays.toString(searchTerms));
            }
            searchTerms = Arrays.copyOf(searchTerms, MAX_SEARCH_TERMS);
        }

        return searchTerms;
    }

//        @Query("select e from EmployeeEntity e where e.firstName like %:contains% or e.lastName like %:contains%")
    @Override
    public List<RepoTextFileEntity> search(String... searchTerms) {
        return search(searchTerms, PageRequest.ofSize(Integer.MAX_VALUE))
                .toList();
    }

    @Override
    @SuppressWarnings("unchecked")
    public Page<RepoTextFileEntity> search(String[] searchTerms, Pageable pageable) {
        searchTerms = ensureCorrect(searchTerms);

        String contentLikeTerms = IntStream.range(0, searchTerms.length)
                .mapToObj(i -> " content ilike " + ":" + termI(i))
                .collect(Collectors.joining(" and "));

        String queryString = FROM_REPO_FILE + contentLikeTerms + ORDER_BY_ID;
        String countQueryString = COUNT_FROM_REPO_FILE + contentLikeTerms;

        Query query = em.createNativeQuery(queryString, RepoTextFileEntity.class);
        for (int i = 0; i < searchTerms.length; i++) {
            query.setParameter(termI(i), "%" + searchTerms[i] + "%");
        }

        query.setFirstResult((int)pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());

        Query countQuery = em.createNativeQuery(countQueryString);
        for (int i = 0; i < searchTerms.length; i++) {
            countQuery.setParameter(termI(i), "%" + searchTerms[i] + "%");
        }

        long total = ((Number) countQuery.getSingleResult()).longValue();

        return new PageImpl<RepoTextFileEntity>(query.getResultList(), pageable, total);
    }

    private static String termI(int i) {
        return "term" + i;
    }

    private void deleteRepoByRelativePath(String relativePath) {
        int deleted = em.createNamedQuery("RepoEntity.deleteByRelativePath")
                .setParameter("relativePath", relativePath)
                .executeUpdate();
        if (deleted > 0) {
            log.debug("Old repo state deleted: {}", relativePath);
        }
    }
}
