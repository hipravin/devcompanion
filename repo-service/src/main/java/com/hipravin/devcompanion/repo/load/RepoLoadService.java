package com.hipravin.devcompanion.repo.load;

import com.hipravin.devcompanion.repo.model.Repo;

import java.util.stream.Stream;

public interface RepoLoadService {
    Stream<Repo> loadAll();
}
