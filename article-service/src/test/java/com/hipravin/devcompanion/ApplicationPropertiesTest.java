package com.hipravin.devcompanion;

import com.hipravin.devcompanion.config.ApplicationProperties;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ApplicationPropertiesTest {
    @Autowired
    ApplicationProperties applicationProperties;

    @Test
    void testPropertiesValues() {
        assertEquals(20, applicationProperties.getArticlesDefaultResponseLimit());
        assertEquals(1000, applicationProperties.getArticlesMaxResponseLimit());
    }
}