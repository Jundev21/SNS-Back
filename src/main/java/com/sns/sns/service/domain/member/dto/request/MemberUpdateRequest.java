package com.sns.sns.service.domain.member.dto.request;

public record MemberUpdateRequest(
        String password,
        String userEmail
) {
}
