package com.hipravin.devcompanion.repo.service;

import com.hipravin.devcompanion.repo.dto.CodeSnippetDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

//@SpringBootTest
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles({"test"})
class RepoSearchServiceTest {

    @Autowired
    private RepoSearchService repoSearchService;

    String testContent = """
            aaa b c
            d e f
            a
            c
            c
            b
            c
            d
            d
            c aaa
            d
            d
            d
            d
            f
            f
            f
            f
            aaa aaa
            a
            aaa""";

    @Test
    void testSnippets() {
        List<CodeSnippetDto> snippets = repoSearchService.snippetsFromFileContent(testContent, new String[]{"aaa"});

        assertEquals(3, snippets.size());
    }
}