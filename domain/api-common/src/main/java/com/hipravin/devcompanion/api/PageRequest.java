package com.hipravin.devcompanion.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PageRequest {
    private final int page;
    private final int pageSize;

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public PageRequest(@JsonProperty("page") int page,
                       @JsonProperty("pageSize") int pageSize) {
        this.page = page;
        this.pageSize = pageSize;
    }

    public int getPage() {
        return page;
    }

    public int getPageSize() {
        return pageSize;
    }
}
