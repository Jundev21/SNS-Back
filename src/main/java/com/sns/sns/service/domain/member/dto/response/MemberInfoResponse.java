package com.sns.sns.service.domain.member.dto.response;

import com.sns.sns.service.domain.member.model.entity.Member;
import lombok.Builder;

@Builder
public record MemberInfoResponse(
        String userName,
        String userEmail

) {
    public static MemberInfoResponse memberInfo(Member member){
        return MemberInfoResponse.builder()
                .userName(member.getUsername())
                .userEmail(member.getUserEmail())
                .build();
    }
}
