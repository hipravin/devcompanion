package com.hipravin.devcompanion.gateway;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.routes")
public class AppRouteProperties {
    private String articleServiceUri;
    private String repoServiceUri;
    private String repoServiceAuthEncoded;
    private String frontendUri;

    public String getArticleServiceUri() {
        return articleServiceUri;
    }

    public void setArticleServiceUri(String articleServiceUri) {
        this.articleServiceUri = articleServiceUri;
    }

    public String getRepoServiceUri() {
        return repoServiceUri;
    }

    public void setRepoServiceUri(String repoServiceUri) {
        this.repoServiceUri = repoServiceUri;
    }

    public String getRepoServiceAuthEncoded() {
        return repoServiceAuthEncoded;
    }

    public void setRepoServiceAuthEncoded(String repoServiceAuthEncoded) {
        this.repoServiceAuthEncoded = repoServiceAuthEncoded;
    }

    public String getFrontendUri() {
        return frontendUri;
    }

    public void setFrontendUri(String frontendUri) {
        this.frontendUri = frontendUri;
    }
}
