package com.hipravin.devcompanion.api;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class PageRequestTest {

    @Test
    void testMapping() {
        PageRequest original = new PageRequest(17, 20);

        PageRequest pageRequest = JsonUtil.fromJson(JsonUtil.toJson(original), PageRequest.class);

        assertNotNull(pageRequest);
        assertEquals(original.getPage(), pageRequest.getPage());
    }
}