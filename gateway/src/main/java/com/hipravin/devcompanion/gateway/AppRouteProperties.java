package com.hipravin.devcompanion.gateway;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.routes")
public class AppRouteProperties {
    private String articleServiceUri;
    private String frontendUri;

    public String getArticleServiceUri() {
        return articleServiceUri;
    }

    public void setArticleServiceUri(String articleServiceUri) {
        this.articleServiceUri = articleServiceUri;
    }

    public String getFrontendUri() {
        return frontendUri;
    }

    public void setFrontendUri(String frontendUri) {
        this.frontendUri = frontendUri;
    }
}
