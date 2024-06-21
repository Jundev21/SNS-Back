package com.sns.sns.service.domain.member.controller;


import com.sns.sns.service.common.basicResponse.BasicResponse;
import com.sns.sns.service.common.basicResponse.DataResponse;
import com.sns.sns.service.common.response.Response;
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

import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/register")
    public DataResponse<RegisterResponse> memberRegister(
            @RequestBody @Valid RegisterRequest registerRequest
    ) {
        return DataResponse.successBodyResponse(HttpStatus.OK,memberService.memberRegister(registerRequest));
    }

    @PostMapping("/login")
    public Response<LoginResponse> memberLogin(
            @RequestBody LoginRequest loginRequest
            ){
        return Response.success(memberService.memberLogin(loginRequest));
    }


    @GetMapping
    public Response<MemberInfoResponse> memberInfo(
            @AuthenticationPrincipal Member member
    ){
        return Response.success(memberService.getMemberInfo(member));
    }

    @PutMapping
    public Response<MemberInfoResponse> memberUpdate(
            @RequestBody MemberUpdateRequest memberUpdateRequest,
            @AuthenticationPrincipal Member member
    ){
        return Response.success(memberService.memberUpdate(memberUpdateRequest,member));
    }

    @DeleteMapping
    public Response<String> memberUpdate(
            @AuthenticationPrincipal Member member
    ){
        memberService.deleteMember(member);
        return Response.success("DELETED");
    }

}
