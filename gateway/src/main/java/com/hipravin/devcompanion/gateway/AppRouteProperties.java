package com.hipravin.devcompanion.gateway;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.routes")
public class AppRouteProperties {
    private String articleServiceUri;

    public String getArticleServiceUri() {
        return articleServiceUri;
    }

    public void setArticleServiceUri(String articleServiceUri) {
        this.articleServiceUri = articleServiceUri;
    }
}
