package com.hipravin.devcompanion.gateway;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("local")
@Disabled //not sure it's feasible to mock all oath2 stuff in test in order to load a contest
class GatewayApplicationTests {

    @Test
    void contextLoads() {
    }

}
