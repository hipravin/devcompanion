package com.hipravin.devcompanion.config;

import com.hipravin.devcompanion.article.ArticleStorage;
import com.hipravin.devcompanion.article.inmemory.ArticleInMemoryRepository;
import com.hipravin.devcompanion.article.yml.ArticleYmlFileStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.nio.file.Paths;

@Configuration
@EnableConfigurationProperties({
        ApplicationProperties.class})
public class ArticleRepositoryConfig {

    @Autowired
    ApplicationProperties applicationProperties;

    @Bean
    ArticleStorage articleStorage() {
        return new ArticleYmlFileStorage(Paths.get(applicationProperties.getActiclesYamlPath()));
    }

    @Bean
    ArticleInMemoryRepository articleInMemoryRepository() {
        return new ArticleInMemoryRepository();
    }


    public ApplicationProperties getApplicationProperties() {
        return applicationProperties;
    }

    public void setApplicationProperties(ApplicationProperties applicationProperties) {
        this.applicationProperties = applicationProperties;
    }
}
