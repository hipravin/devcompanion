package com.hipravin.devcompanion;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.assertNotNull;

//@SpringBootTest
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles({"test"})
class RepoServiceApplicationTests {
    private static final Logger log = LoggerFactory.getLogger(RepoServiceApplicationTests.class);

    @Autowired
    ApplicationContext context;

    @Test
    void contextLoads() {
        assertNotNull(context);
        log.debug("Application context class: " + context.getClass());
    }

    @Test
    void bcrypt() {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        String encoded = bCryptPasswordEncoder.encode("uuser");

        System.out.println("Encoded:");
        System.out.println(encoded);
        boolean matches = bCryptPasswordEncoder.matches("uuser", encoded);
        System.out.println("Matches: " + matches);

        System.out.println(bCryptPasswordEncoder.encode("uuser"));
        System.out.println(bCryptPasswordEncoder.encode("aadmin"));
    }
}
