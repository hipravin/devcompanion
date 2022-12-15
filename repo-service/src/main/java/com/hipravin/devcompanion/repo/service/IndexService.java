package com.hipravin.devcompanion.repo.service;

import com.hipravin.devcompanion.repo.load.RepoLoadService;
import com.hipravin.devcompanion.repo.model.Repo;
import com.hipravin.devcompanion.repo.persist.RepoDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Stream;

@Service
public class IndexService {
    private static final Logger log = LoggerFactory.getLogger(IndexService.class);

    private final RepoLoadService repoLoadService;
    private final RepoDao repoDao;


    private final Lock indexExclusive = new ReentrantLock();

    public IndexService(RepoLoadService repoLoadService, RepoDao repoDao) {
        this.repoLoadService = repoLoadService;
        this.repoDao = repoDao;
    }

    //for sure current approach only prevents multiple index operations on single node
    //however this may be sufficient since index is idempotent
    public void index() throws ConcurrentIndexOperationException {
        if (indexExclusive.tryLock()) {
            try {
                log.info("Index started.");
                Stream<Repo> repos = repoLoadService.loadAll();
                repos.forEach(r -> repoDao.saveOrUpdate(r));

                log.info("Index completed.");
            } catch (RuntimeException e) {
                log.error("Index operation failed", e);
            } finally {
                indexExclusive.unlock();
            }
        } else {
            log.error("Concurrent index request declined.");
            throw new ConcurrentIndexOperationException("Index already in progress");
        }
    }
}
