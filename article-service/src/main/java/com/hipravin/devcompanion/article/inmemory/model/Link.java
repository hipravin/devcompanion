package com.hipravin.devcompanion.article.inmemory.model;

import com.hipravin.devcompanion.article.dto.LinkDto;

public record Link(
        String title,
        String url
) {
    public static Link from(LinkDto linkDto) {
        return new Link(linkDto.getTitle(), linkDto.getUrl());
    }

    public LinkDto toDto() {
        return new LinkDto(title, url);
    }
}
