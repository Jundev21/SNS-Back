package com.sns.sns.service.domain.member.dto.request;

public record LoginRequest(
        String userName,
        String password
) {

}
