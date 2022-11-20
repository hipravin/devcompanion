package com.hipravin.devcompanion.repo.load;

import com.hipravin.devcompanion.repo.model.Repo;

import java.nio.file.Path;
import java.util.stream.Stream;

public class RepoLoadServiceLocalDirectory implements RepoLoadService {

    private final Path rootReposPath;

    public RepoLoadServiceLocalDirectory(Path rootReposPath) {
        this.rootReposPath = rootReposPath;
    }

    @Override
    public Stream<Repo> loadAll() {
        return null;
    }

    Repo loadSingleRepoEagerly(Path repoPath) {


        return null;
    }


}
