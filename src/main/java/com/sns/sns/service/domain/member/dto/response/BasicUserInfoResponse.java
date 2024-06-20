package com.sns.sns.service.domain.member.dto.response;

import com.sns.sns.service.domain.member.model.UserRole;
import com.sns.sns.service.domain.member.model.entity.Member;
import lombok.Builder;

@Builder
public record BasicUserInfoResponse(
        Long id,
        String userName,
        Long countVisited
) {
    public static BasicUserInfoResponse basicUserInfoResponse(Member member){
        return BasicUserInfoResponse.builder()
                .id(member.getId())
                .userName(member.getUsername())
                .countVisited(member.getVisitedTimes())
                .build();
    }
}
