package com.sns.sns.service.domain.member.controller;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sns.sns.service.common.basicResponse.DataResponse;
import com.sns.sns.service.domain.member.dto.request.LoginRequest;
import com.sns.sns.service.domain.member.dto.request.MemberUpdateRequest;
import com.sns.sns.service.domain.member.dto.request.RegisterRequest;
import com.sns.sns.service.domain.member.dto.response.LoginResponse;
import com.sns.sns.service.domain.member.dto.response.MemberInfoResponse;
import com.sns.sns.service.domain.member.dto.response.RegisterResponse;
import com.sns.sns.service.domain.member.model.entity.Member;
import com.sns.sns.service.domain.member.service.MemberService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class MemberController {

	private final MemberService memberService;

	@PostMapping("/register")
	public DataResponse<RegisterResponse> memberRegister(
		@RequestBody @Valid RegisterRequest registerRequest
	) {
		return DataResponse.successBodyResponse(HttpStatus.OK, memberService.memberRegister(registerRequest));
	}

	@PostMapping("/login")
	public DataResponse<LoginResponse> memberLogin(
		@RequestBody LoginRequest loginRequest
	) {
		return DataResponse.successBodyResponse(HttpStatus.OK, memberService.memberLogin(loginRequest));
	}

	@GetMapping
	public DataResponse<MemberInfoResponse> memberInfo(
		@AuthenticationPrincipal Member member
	) {
		return DataResponse.successBodyResponse(HttpStatus.OK, memberService.getMemberInfo(member));
	}

	@PutMapping
	public DataResponse<MemberInfoResponse> memberUpdate(
		@RequestBody MemberUpdateRequest memberUpdateRequest,
		@AuthenticationPrincipal Member member
	) {
		return DataResponse.successBodyResponse(HttpStatus.OK, memberService.memberUpdate(memberUpdateRequest, member));
	}

	@DeleteMapping
	public DataResponse<String> memberUpdate(
		@AuthenticationPrincipal Member member
	) {
		memberService.deleteMember(member);
		return DataResponse.successBodyResponse(HttpStatus.OK, "사용자가 삭제되었습니다.");
	}

}
