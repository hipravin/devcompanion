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
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Repository
@Transactional
public class RepoDaoImpl implements RepoDao {
    private static final Logger log = LoggerFactory.getLogger(RepoDaoImpl.class);

    private static int MAX_SEARCH_TERMS = 10;
    //alternatively to {h-schema} placeholder one can try to create a trigger on login (not tested)
//    CREATE OR REPLACE TRIGGER db_logon AFTER logon ON DATABASE
//    WHEN
//            (
//                    USER = 'APP_ACCESS'
//            )
//    BEGIN
//    EXECUTE immediate 'ALTER SESSION SET CURRENT_SCHEMA = APP_OWNER';
//    END;


    private static final String FROM_REPO_FILE = "select * from {h-schema}repo_file where ";
    private static final String COUNT_FROM_REPO_FILE = "select count(*) as total from {h-schema}repo_file where ";
    private static final String ORDER_BY_ID = " order by id ";

    private final EntityManager em;
    private final RepoRepository repoRepository;
    private final RepoFileRepository repoFileRepository;

    public RepoDaoImpl(EntityManager em, RepoRepository repoRepository, RepoFileRepository repoFileRepository) {
        this.em = em;
        this.repoRepository = repoRepository;
        this.repoFileRepository = repoFileRepository;
    }

    @Override
    public Optional<RepoTextFileEntity> findFileById(long id) {
        return repoFileRepository.findById(id);
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
        if (searchTerms.length == 0) {
            throw new IllegalArgumentException("searchTerms should be not empty");
        }
        if (searchTerms.length > MAX_SEARCH_TERMS) {
            if (log.isDebugEnabled()) {
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

        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());

        Query countQuery = em.createNativeQuery(countQueryString);
        for (int i = 0; i < searchTerms.length; i++) {
            countQuery.setParameter(termI(i), "%" + searchTerms[i] + "%");
        }

        long total = ((Number) countQuery.getSingleResult()).longValue();
        List<RepoTextFileEntity> repoFilesPage = query.getResultList();

        fetchRepos(repoFilesPage);
        return new PageImpl<>(repoFilesPage, pageable, total);
    }

    private void fetchRepos(List<RepoTextFileEntity> textFileEntities) {
        Set<Long> repoIds = textFileEntities.stream()
                .map(rf -> rf.getRepo().getId())
                .collect(Collectors.toSet());
        repoRepository.findAllById(repoIds);//load into session in batch
        //replace proxies with actual objects gathered from l1 cache without querying database
        textFileEntities.forEach(tfe -> tfe.getRepo().getName());
    }

    private void fetchReposNaive(List<RepoTextFileEntity> textFileEntities) {
        //so-called N+1 query problem: will run one query for each distinct repo
        textFileEntities.forEach(tfe -> tfe.getRepo().getName());
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
