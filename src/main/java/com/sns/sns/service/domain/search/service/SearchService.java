package com.sns.sns.service.domain.search.service;


import com.sns.sns.service.common.response.Response;
import com.sns.sns.service.domain.board.repository.BoardRepository;
import com.sns.sns.service.domain.search.dto.request.SearchRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.ReadOnlyProperty;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SearchService {

    private final BoardRepository boardRepository;
    @Transactional(readOnly = true)
    public Page<Response<SearchRequest>> getSearchWord(String searchTitle) {

        


    return null;

    }
}
