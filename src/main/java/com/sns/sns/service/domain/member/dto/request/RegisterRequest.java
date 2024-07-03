package com.sns.sns.service.domain.member.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record RegisterRequest(

		@NotNull(message = "아이디는 필수 값 입니다.")
		String userLoginId,
		@NotNull(message = "이름은 필수 값 입니다.")
		String userName,
		@NotNull(message = "비밀번호는 필수 값 입니다.")
		String password,
		@NotNull(message = "비밀번호는 필수 값 입니다.")
		String checkPassword,
		@Email(message = "이메일형태로 입력해주세요")
        String userEmail
) {
}
