package com.hipravin.devcompanion;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "application")
public class ApplicationProperties {
    private String acticlesYamlPath;
    private int articlesMaxResponseLimit;
    private int articlesDefaultResponseLimit;

    public String getActiclesYamlPath() {
        return acticlesYamlPath;
    }

    public void setActiclesYamlPath(String acticlesYamlPath) {
        this.acticlesYamlPath = acticlesYamlPath;
    }

    public int getArticlesMaxResponseLimit() {
        return articlesMaxResponseLimit;
    }

    public void setArticlesMaxResponseLimit(int articlesMaxResponseLimit) {
        this.articlesMaxResponseLimit = articlesMaxResponseLimit;
    }

    public int getArticlesDefaultResponseLimit() {
        return articlesDefaultResponseLimit;
    }

    public void setArticlesDefaultResponseLimit(int articlesDefaultResponseLimit) {
        this.articlesDefaultResponseLimit = articlesDefaultResponseLimit;
    }
}
