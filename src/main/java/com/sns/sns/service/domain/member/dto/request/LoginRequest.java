package com.sns.sns.service.domain.member.dto.request;

import jakarta.validation.constraints.NotNull;

public record LoginRequest(
		@NotNull(message = "로그인 아이디를 입력해주세요")
        String userLoginId,
		@NotNull(message = "비밀번호를 입력해주세요")
        String password
) {

}
