package com.hipravin.devcompanion.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PagedResponse<T> {
    private final List<T> content;
    private final int pageNumber;
    private final int pageSize;
    private final int totalPages;
    private final long totalElements;

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public PagedResponse(@JsonProperty(value = "content", required = true) List<T> content,
                         @JsonProperty(value = "pageNumber", required = true) int pageNumber,
                         @JsonProperty(value = "pageSize", required = true) int pageSize,
                         @JsonProperty(value = "totalElements", required = true) long totalElements,
                         @JsonProperty(value = "totalPages", required = true) int totalPages) {
        this.content = Objects.requireNonNull(content);
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
        this.totalElements = totalElements;
        this.totalPages = totalPages;
    }

    public List<T> getContent() {
        return content;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public int getPageSize() {
        return pageSize;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public long getTotalElements() {
        return totalElements;
    }

}
