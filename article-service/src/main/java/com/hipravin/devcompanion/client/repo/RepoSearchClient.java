package com.hipravin.devcompanion.client.repo;


import com.hipravin.devcompanion.repo.dto.FileSnippetsDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "repo", url = "${repo-service.url}", configuration = RepoSearchClientFeignConfiguration.class)
public interface RepoSearchClient {
    @RequestMapping(method = RequestMethod.GET, value = "/api/v1/repos/search/")
    RestResponsePage<FileSnippetsDto> search(@RequestParam("q") String query);
}
