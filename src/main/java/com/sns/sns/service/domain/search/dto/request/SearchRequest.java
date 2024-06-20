package com.sns.sns.service.domain.search.dto.request;


import lombok.Builder;
import lombok.Getter;


public record SearchRequest(
        String searchWord
) {
}
