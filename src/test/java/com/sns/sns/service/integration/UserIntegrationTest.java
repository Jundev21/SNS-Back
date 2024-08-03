package com.sns.sns.service.integration;

import java.nio.file.Path;
import java.nio.file.Paths;

import com.sns.sns.service.common.exception.BasicException;
import com.sns.sns.service.common.exception.ErrorCode;
import com.sns.sns.service.domain.member.dto.request.LoginRequest;
import com.sns.sns.service.domain.member.dto.response.RegisterResponse;
import com.sns.sns.service.domain.member.model.UserStatus;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.sns.sns.service.domain.member.dto.request.RegisterRequest;
import com.sns.sns.service.domain.member.model.entity.Member;
import com.sns.sns.service.domain.member.repository.MemberRepository;
import com.sns.sns.service.domain.member.service.MemberService;
import org.springframework.security.core.AuthenticationException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@ExtendWith({SpringExtension.class, MockitoExtension.class})
@ActiveProfiles("test")
public class UserIntegrationTest {

    @Autowired
    MemberService memberService;

    @Test
    @DisplayName("회원 탈퇴 로그인 확인")
    void widthDrawMember() {
        Member newMember = new Member(
                "testLoginId",
                "test userName",
                "password",
                "userEmail",
                "image",
                UserStatus.JOIN
        );

        RegisterRequest registerRequest = new RegisterRequest(
                "testLoginId",
                "test userName",
                "password",
                "password",
                "image"
        );

        RegisterResponse registeredMember = memberService.memberRegister(registerRequest);
        Assertions.assertEquals(registeredMember.userLoginId(), "testLoginId");

        LoginRequest loginRequest = new LoginRequest("testLoginId","password");

        Assertions.assertDoesNotThrow(() ->  memberService.memberLogin(loginRequest));

        memberService.softDeleteMember(newMember);

        AuthenticationException e = Assertions.assertThrows(AuthenticationException.class,
                () -> memberService.memberLogin(loginRequest));
        Assertions.assertEquals(e.getMessage(), ErrorCode.NOT_EXIST_MEMBER.getMsg());

    }


    @Test
    public void checkRegisterQuery() {

    }

    @Test
    public void checkLoginQuery() {
        Path imagePath = Paths.get("/", "Users", "jun", "Desktop", "uploadImages", "/");

    }
}
