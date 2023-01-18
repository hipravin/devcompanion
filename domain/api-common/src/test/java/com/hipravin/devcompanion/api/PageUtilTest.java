package com.hipravin.devcompanion.api;

import org.junit.jupiter.api.Test;

import static com.hipravin.devcompanion.api.PageUtil.totalPages;
import static org.junit.jupiter.api.Assertions.*;

class PageUtilTest {
    @Test
    void testTotalPagesIllegal() {
        assertThrows(IllegalArgumentException.class, ()-> {
           totalPages(-1,1);
        });
        assertThrows(IllegalArgumentException.class, ()-> {
           totalPages(1,0);
        });
    }

    @Test
    void testTotalPagesSamples() {
        assertEquals(1, totalPages(0, 5));
        assertEquals(1, totalPages(5, 5));
        assertEquals(2, totalPages(6, 5));
        assertEquals(11, totalPages(51, 5));
        assertEquals(11, totalPages(55, 5));
    }
}