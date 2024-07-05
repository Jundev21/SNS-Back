package com.sns.sns.service.domain.member.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.sns.sns.service.common.basicResponse.DataResponse;
import com.sns.sns.service.domain.member.dto.request.LoginRequest;
import com.sns.sns.service.domain.member.dto.request.MemberUpdateRequest;
import com.sns.sns.service.domain.member.dto.request.RegisterRequest;
import com.sns.sns.service.domain.member.dto.response.LoginResponse;
import com.sns.sns.service.domain.member.dto.response.MemberInfoResponse;
import com.sns.sns.service.domain.member.dto.response.RegisterResponse;
import com.sns.sns.service.domain.member.model.entity.Member;
import com.sns.sns.service.domain.member.service.MemberService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class MemberController {

	private final MemberService memberService;

	@Operation(summary = "사용자 회원가입 API")
	@ApiResponses(value = {
		@ApiResponse(
			description = "회원가입 성공",
			responseCode = "200",
			useReturnTypeSchema = true
		),
		@ApiResponse(
			description = "이미 존재하는 회원일 경우",
			responseCode = "409",
			content = {@Content(schema = @Schema(implementation = DataResponse.class))}
		)
	})
	@PostMapping("/register")
	public DataResponse<RegisterResponse> memberRegister(
		@RequestBody @Valid RegisterRequest registerRequest
	) {
		return DataResponse.successBodyResponse(HttpStatus.OK, memberService.memberRegister(registerRequest));
	}
	@Operation(summary = "사용자 로그인 API")
	@ApiResponses(value = {
		@ApiResponse(
			description = "로그인 성공",
			responseCode = "200",
			useReturnTypeSchema = true
		),
		@ApiResponse(
			description = "로그인 정보가 일치하지 않을경우",
			responseCode = "400",
			content = {@Content(schema = @Schema(implementation = DataResponse.class))}
		)
	})
	@PostMapping("/login")
	public DataResponse<LoginResponse> memberLogin(
		@RequestBody LoginRequest loginRequest
	) {
		return DataResponse.successBodyResponse(HttpStatus.OK, memberService.memberLogin(loginRequest));
	}
	@Operation(summary = "사용자 정보 조회 API")
	@ApiResponses(value = {
		@ApiResponse(
			description = "사용자 정보 조회 성공",
			responseCode = "200",
			useReturnTypeSchema = true
		),
		@ApiResponse(
			description = "존재하지 않는 회원일경우",
			responseCode = "404",
			content = {@Content(schema = @Schema(implementation = DataResponse.class))}
		)
	})
	@GetMapping
	public DataResponse<MemberInfoResponse> memberInfo(
		@AuthenticationPrincipal Member member
	) {
		return DataResponse.successBodyResponse(HttpStatus.OK, memberService.getMemberInfo(member));
	}
	@Operation(summary = "사용자 정보 수정 API")
	@ApiResponses(value = {
		@ApiResponse(
			description = "사용자 정보 수정 성공",
			responseCode = "200",
			useReturnTypeSchema = true
		),
		@ApiResponse(
			description = "존재하지 않는 회원일경우",
			responseCode = "404",
			content = {@Content(schema = @Schema(implementation = DataResponse.class))}
		)
	})
	@PostMapping
	public DataResponse<MemberInfoResponse> memberUpdate(
		@RequestPart MemberUpdateRequest memberUpdateRequest,
		@RequestPart(required = false) MultipartFile image,
		@AuthenticationPrincipal Member member
	) {
		return DataResponse.successBodyResponse(HttpStatus.OK,
			memberService.memberUpdate(memberUpdateRequest, image, member));
	}
	@Operation(summary = "사용자 회원 탈퇴 API")
	@ApiResponses(value = {
		@ApiResponse(
			description = "탈퇴 성공",
			responseCode = "200",
			useReturnTypeSchema = true
		),
		@ApiResponse(
			description = "존재하지 않는 회원일경우",
			responseCode = "404",
			content = {@Content(schema = @Schema(implementation = DataResponse.class))}
		)
	})
	@DeleteMapping
	public DataResponse<String> memberUpdate(
		@AuthenticationPrincipal Member member
	) {
		memberService.deleteMember(member);
		return DataResponse.successBodyResponse(HttpStatus.OK, "사용자가 삭제되었습니다.");
	}

}
