package com.hipravin.devcompanion.api;

import java.util.Collections;
import java.util.List;
import java.util.function.Function;

public final class PageUtil {
    private PageUtil() {
    }

    public static <T> PagedResponse<T> emptyPagedResponse(int pageSize) {
        return new PagedResponse<>(Collections.emptyList(), 0, pageSize, 0, 1);
    }

    public static int totalPages(long totalElements, int pageSize) {
        if(totalElements < 0) {
            throw new IllegalArgumentException("totalElements must be positive");
        }

        if(pageSize < 1) {
            throw new IllegalArgumentException("pageSize must be greater than one");
        }

        return (totalElements == 0) ? 1 : (int) Math.ceil((double) totalElements / (double) pageSize);
    }

    public static <T1,T2> PagedResponse<T2> map(PagedResponse<T1> original, Function<T1,T2> transformFunction) {
        List<T2> contentMapped = original.getContent()
                .stream()
                .map(transformFunction)
                .toList();

        return new PagedResponse<>(contentMapped, original.getPageNumber(), original.getPageSize(),
                original.getTotalElements(), original.getTotalPages());
    }
}
