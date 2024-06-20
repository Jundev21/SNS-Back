package com.sns.sns.service.service;


import com.sns.sns.service.domain.exception.BasicException;
import com.sns.sns.service.domain.exception.ErrorCode;
import com.sns.sns.service.domain.member.dto.request.LoginRequest;
import com.sns.sns.service.domain.member.dto.request.RegisterRequest;
import com.sns.sns.service.domain.member.model.entity.Member;
import com.sns.sns.service.domain.member.repository.MemberRepository;
import com.sns.sns.service.domain.member.service.MemberService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


//서비스 테스트는 서비스를 테스트 해야하기 때문에 데이터 베이스를 목으로 생성하여 테스트한다.

@SpringBootTest
public class UserServiceTest {

    @Autowired
    private MemberService memberService;

    @MockBean
    private MemberRepository memberRepository;

    @MockBean
    private BCryptPasswordEncoder encoder;


    @Test
    @DisplayName("회원가입이 정상적으로 되었을 경우")
    public void successRegister(){

        String username = "username";
        String password = "password";

        when(memberRepository.findByUserName(username)).thenReturn(Optional.empty());
        when(memberRepository.save(any())).thenReturn(mock(Member.class));
//        when(memberRepository.save(any())).thenReturn(new Member(username, password));

//        Assertions.assertDoesNotThrow(()->memberService.memberRegister(
//                new RegisterRequest(username, password)
//        ));
    }

    @Test
    @DisplayName("회원가입이 정상적으로 되지 않았을 경우")
    public void failRegister(){

        String username = "username";
        String password = "password";

        when(memberRepository.findByUserName(username)).thenReturn(Optional.of(mock(Member.class)));

//        Assertions.assertThrows(BasicException.class, ()->memberService.memberRegister(
//                new RegisterRequest(username, password)
//        ));
    }


    @Test
    @DisplayName("로그인 정상적으로 되었을 경우")
    public void successLogin(){

        String username = "username";
        String password = "password";

//        Member newMember = new Member(username, password);

//        when(memberRepository.findByUserName(username)).thenReturn(Optional.of(newMember));
//        when(encoder.matches(password, newMember.getPassword())).thenReturn(true);

        Assertions.assertDoesNotThrow(()->memberService.memberLogin(
                new LoginRequest(username, password)
        ));
    }

    @Test
    @DisplayName("로그인 시 회원이 아닌경우")
    public void notExistMemberLogin(){

        String username = "username";
        String password = "password";

        when(memberRepository.findByUserName(username)).thenReturn(Optional.empty());

        BasicException error =  Assertions.assertThrows(BasicException.class, ()->memberService.memberLogin(
                new LoginRequest(username, password)
        ));

        Assertions.assertEquals(error.getErrorCode(), ErrorCode.NOT_EXIST_MEMBER);
    }

    @Test
    @DisplayName("로그인 시 비밀번호 틀린경우")
    public void wrongPasswordLogin(){

        String username = "username";
        String password = "password";
        String wrongPassword = "wrongPassword";

//        when(memberRepository.findByUserName(username)).thenReturn(Optional.of(new Member(username, password)));

        BasicException error =  Assertions.assertThrows(BasicException.class, () -> memberService.memberLogin(
                new LoginRequest(username, wrongPassword)
        ));

        Assertions.assertEquals(error.getErrorCode(), ErrorCode.INVALID_PASSWORD);

    }



}
