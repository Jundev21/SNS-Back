package com.sns.sns.service.domain.member.dto.request;

public record RegisterRequest(
        String userName,
        String password,
        String userEmail
) {
}
