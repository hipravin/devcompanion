package com.hipravin.devcompanion.config;

import com.hipravin.devcompanion.repo.load.RepoLoadService;
import com.hipravin.devcompanion.repo.load.RepoLoadServiceLocalDirectory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.nio.file.Paths;

@Configuration
public class RepoConfig {

    @Bean
    public RepoLoadService repoLoadService(ApplicationProperties applicationProperties) {
        return new RepoLoadServiceLocalDirectory(Paths.get(applicationProperties.getRepoPath()));
    }
}

