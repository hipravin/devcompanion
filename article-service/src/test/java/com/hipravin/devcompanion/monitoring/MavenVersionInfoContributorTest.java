package com.hipravin.devcompanion.monitoring;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@ActiveProfiles({"test"})
class MavenVersionInfoContributorTest {
    @Autowired
    MavenVersionInfoContributor versionInfoContributor;

    @Test
    void testInitialized() {
        assertNotNull(versionInfoContributor);

        assertNotNull(versionInfoContributor.versionInfoMap);
        Map<String, String> vm = versionInfoContributor.versionInfoMap;

        assertEquals(4, vm.size());
        assertEquals("article-service", vm.get("artifactId"));
    }
}