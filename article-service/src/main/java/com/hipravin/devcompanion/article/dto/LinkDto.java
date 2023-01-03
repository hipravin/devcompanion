package com.hipravin.devcompanion.article.dto;

public class LinkDto {
    private String title;
    private String url;

    public LinkDto() {
    }

    public LinkDto(String title, String url) {
        this.title = title;
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
