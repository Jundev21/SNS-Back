package com.sns.sns.service.domain.search.controller;

// 검색 조건검색 api

import com.sns.sns.service.common.response.Response;
import com.sns.sns.service.domain.search.dto.request.SearchRequest;
import com.sns.sns.service.domain.search.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/search")
public class SearchController {

    //http mapping 을 통하여 전달받는다.
    //자동 검색 결과
    private final SearchService searchService;

    @GetMapping
    public Page<Response<SearchRequest>> getSearchResult(
            @PathVariable String searchTitle
    ){


        return searchService.getSearchWord(searchTitle);
    }



}
