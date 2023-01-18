package com.hipravin.devcompanion.repo.service;

import com.hipravin.devcompanion.api.PageRequest;
import com.hipravin.devcompanion.repo.dto.*;
import com.hipravin.devcompanion.repo.persist.RepoDao;
import com.hipravin.devcompanion.repo.persist.entity.RepoEntity;
import com.hipravin.devcompanion.repo.persist.entity.RepoTextFileEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
public class RepoSearchService {
    private final RepoDao repoDao;

    private int additionalLinesInSnippet = 4;
    private int blockMergeLineCount = additionalLinesInSnippet * 2;
    private int maxSnippetsFromFile = 5;

    public RepoSearchService(RepoDao repoDao) {
        this.repoDao = repoDao;
    }

    public Page<FileSnippetsDto> findFilesOrderById(String query, PageRequest pageRequest) {
        String[] searchTerms = parseQuery(query);

        Pageable pageable = org.springframework.data.domain.PageRequest.of(pageRequest.getPage(), pageRequest.getPageSize());

        Page<RepoTextFileEntity> repoFiles = repoDao.search(searchTerms, pageable);
        return repoFiles.map(rf -> convertToDto(rf, searchTerms));
    }

    public Optional<RepoTextFileDto> findFileById(long id) {
        return repoDao.findFileById(id)
                .map(fe -> mapToDto(fe));
    }

    RepoTextFileDto mapToDto(RepoTextFileEntity rtfe) {
        RepoTextFileDto rtfDto = new RepoTextFileDto();

        RepoFileDescriptionDto descriptionDto = mapToDescriptionDto(rtfe);

        rtfDto.setFileDescription(descriptionDto);
        rtfDto.setContent(rtfe.getContent());

        return rtfDto;
    }

    String[] parseQuery(String query) {
        return query.split("\s+");
    }

    FileSnippetsDto convertToDto(RepoTextFileEntity rtfe, String[] searchTerms) {
        FileSnippetsDto result = new FileSnippetsDto();

        List<CodeSnippetDto> snippets = snippetsFromFileContent(rtfe.getContent(), searchTerms);
        result.setSnippets(snippets);
        result.setFile(mapToDescriptionDto(rtfe));

        return result;
    }

    RepoFileDescriptionDto mapToDescriptionDto(RepoTextFileEntity rtfe) {
        RepoFileDescriptionDto rfdDto = new RepoFileDescriptionDto();

        rfdDto.setId(rtfe.getId());
        rfdDto.setFileName(rtfe.getName());
        rfdDto.setRelativePath(rtfe.getRelativePath());
        rfdDto.setRepo(mapToDto(rtfe.getRepo()));

        return rfdDto;
    }

    RepoDescriptionDto mapToDto(RepoEntity re) {
        RepoDescriptionDto rdDto = new RepoDescriptionDto();
        rdDto.setId(re.getId());
        rdDto.setName(re.getName());
        rdDto.setRelativePath(re.getRelativePath());

        return rdDto;
    }

    record Block(int from, int to) {
        public Block expandForward(int newTo) {
            return new Block(this.from, Math.max(this.to, newTo));
        }
    }

    List<CodeSnippetDto> snippetsFromFileContent(String content, String[] terms) {
        NavigableMap<Integer, String> linesNumbered = splitLines(content);

        //find matches
        Predicate<String> containsAnyTerm = (line) -> Arrays.stream(terms)
                    .anyMatch(t -> line.contains(t.toLowerCase()));

        List<Integer> matchedLineNumbers = linesNumbered.entrySet().stream()
                .filter(e -> containsAnyTerm.test(e.getValue().toLowerCase()))
                .map(e -> e.getKey())
                .limit(maxSnippetsFromFile)
                .toList();

        //merge into blocks
        List<Block> merged = new ArrayList<>();
        for (Integer matchedLineNumber : matchedLineNumbers) {
            Block lastBlock = merged.isEmpty() ? null : merged.get(merged.size() - 1);
            if (lastBlock == null || lastBlock.to() + blockMergeLineCount < matchedLineNumber) {
                merged.add(new Block(matchedLineNumber, matchedLineNumber));
            } else {
                Block expandedLastBlock = lastBlock.expandForward(matchedLineNumber);
                merged.set(merged.size() - 1, expandedLastBlock);
            }
        }
        //blocks to snippets
        return merged.stream()
                .map(b -> createSnippet(linesNumbered, b.from(), b.to()))
                .toList();
    }

    CodeSnippetDto createSnippet(NavigableMap<Integer, String> linesNumbered, int lineFrom, int lineTo) {
        CodeSnippetDto snippet = new CodeSnippetDto();

        int snippetLineFrom = lineFrom - additionalLinesInSnippet;
        int snippetLineTo = lineTo + additionalLinesInSnippet;

        NavigableMap<Integer, String> subMap = linesNumbered.subMap(snippetLineFrom, true, snippetLineTo, true);
        String snippetContent = subMap
                .entrySet().stream()
                .map(e -> e.getValue())
                .collect(Collectors.joining("\n"));

        snippet.setContent(snippetContent);
        snippet.setLineFrom(subMap.firstKey());
        snippet.setLineTo(subMap.lastKey());
        return snippet;
    }

    /**
     * @return [line number -> line content]
     */
    NavigableMap<Integer, String> splitLines(String content) {
        String[] lines = content.split("\n");
        NavigableMap<Integer, String> result = new TreeMap<>();
        for (int i = 0; i < lines.length; i++) {
            String line = lines[i];
            result.put(i, line);
        }

        return result;
    }
}
