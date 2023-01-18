package com.hipravin.devcompanion.api;

import org.junit.jupiter.api.Test;

import java.util.List;

import static com.hipravin.devcompanion.api.JsonUtil.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class PagedResponseTest {
    @Test
    void testMapping() {
        PagedResponse<String> original = new PagedResponse<>(List.of("a", "b"), 2 ,10, 40, 4);
        PagedResponse<String> response = pagedResponseFromJson(toJson(original), String.class);

        assertEquals(original.getPageNumber(), response.getPageNumber());
        assertEquals(original.getTotalPages(), response.getTotalPages());
        assertEquals(original.getPageSize(), response.getPageSize());
        assertEquals(original.getTotalElements(), response.getTotalElements());
        assertEquals(original.getContent(), response.getContent());
    }

    @Test
    void testMappingNullContent() {
        PagedResponse<String> original = new PagedResponse<>(null, 2 ,10, 40, 4);
        PagedResponse<String> response = pagedResponseFromJson(toJson(original), String.class);

        assertEquals(original.getPageNumber(), response.getPageNumber());
        assertEquals(original.getTotalPages(), response.getTotalPages());
        assertEquals(original.getContent(), response.getContent());
    }
}
