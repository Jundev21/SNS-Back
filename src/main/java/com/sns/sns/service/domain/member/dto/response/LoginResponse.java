package com.sns.sns.service.domain.member.dto.response;



public record LoginResponse(

        String token,
        String refreshToken

) {
}
