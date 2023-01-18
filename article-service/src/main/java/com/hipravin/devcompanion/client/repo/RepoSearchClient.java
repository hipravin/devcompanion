package com.hipravin.devcompanion.client.repo;


import com.hipravin.devcompanion.api.PagedResponse;
import com.hipravin.devcompanion.repo.dto.FileSnippetsDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "repo", url = "${repo-service.url}", configuration = RepoSearchClientFeignConfiguration.class)
public interface RepoSearchClient {
    @RequestMapping(method = RequestMethod.GET, value = "/api/v1/repos/search/")
    PagedResponse<FileSnippetsDto> search(
            @RequestParam("q") String query,
            @RequestParam(value = "page", required = false) Integer page,
            @RequestParam(value = "pageSize", required = false) Integer pageSize);
}
