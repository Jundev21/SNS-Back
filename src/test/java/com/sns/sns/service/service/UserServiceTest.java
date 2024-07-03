package com.sns.sns.service.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.sns.sns.service.common.exception.BasicException;
import com.sns.sns.service.domain.member.dto.request.LoginRequest;
import com.sns.sns.service.domain.member.dto.request.RegisterRequest;
import com.sns.sns.service.domain.member.model.entity.Member;
import com.sns.sns.service.domain.member.repository.MemberRepository;
import com.sns.sns.service.domain.member.service.MemberService;
import com.sns.sns.service.jwt.JwtTokenInfo;

//서비스 테스트는 서비스를 테스트 해야하기 때문에 데이터 베이스를 목으로 생성하여 테스트한다.

@SpringBootTest
public class UserServiceTest {

	@Autowired
	private MemberService memberService;

	@MockBean
	private MemberRepository memberRepository;
	@MockBean
	private AuthenticationManager authenticationManager;
	@MockBean
	private BCryptPasswordEncoder encoder;
	@MockBean
	private JwtTokenInfo jwtTokenInfo;

	@Test
	@DisplayName("회원가입이 정상적으로 되었을 경우")
	public void successRegister() {

		String username = "username";
		String password = "password";

		when(memberRepository.findByUserName(username)).thenReturn(Optional.empty());
		when(memberRepository.save(any())).thenReturn(mock(Member.class));
		when(memberRepository.save(any())).thenReturn(new Member("loginId", username, password, "email","img"));

		Assertions.assertDoesNotThrow(() -> memberService.memberRegister(
			new RegisterRequest("loginId", username, password, password, "email")
		));
	}

	@Test
	@DisplayName("회원이 이미 존재하여 회원가입이 정상적으로 되지 않았을 경우")
	public void failRegister() {

		String username = "username";
		String password = "password";

		when(memberRepository.existsByUserLoginId(anyString())).thenReturn(true);
		Assertions.assertThrows(BasicException.class, () -> memberService.memberRegister(
			new RegisterRequest("loginId", username, password, password, "email")
		));

	}

	@Test
	@DisplayName("로그인 정상적으로 되었을 경우")
	public void successLogin() {

		String username = "username";
		String password = "password";

		Member newMember = new Member("loginId", username, password, "email","img");

		Authentication authentication = mock(Authentication.class);
		when(authentication.getPrincipal()).thenReturn(newMember);

		when(authenticationManager.authenticate(any())).thenReturn(authentication);
		when(jwtTokenInfo.generateToken(anyString())).thenReturn("generated jwt token");

		memberService.memberLogin(
			new LoginRequest("loginId", password));
	}

	@Test
	@DisplayName("로그인 시 회원이 아닌경우")
	public void notExistMemberLogin() {

		String username = "username";
		String password = "password";

		Member newMember = new Member("loginId", username, password, "email","img");

		//when
		when(authenticationManager.authenticate(any()))
			.thenThrow(new BadCredentialsException("로그인 정보 오류"));

		// then
		assertThrows(BadCredentialsException.class, () -> {
			memberService.memberLogin(
				new LoginRequest(username, password));
		});
	}

	@Test
	@DisplayName("로그인 시 비밀번호 틀린경우")
	public void wrongPasswordLogin() {

		String username = "username";
		String password = "password";
		String wrongPassword = "wrongPassword";

		Member newMember = new Member("loginId", username, password, "email","img");

		Authentication authentication = mock(Authentication.class);
		when(authentication.getPrincipal()).thenReturn(newMember);

		when(authenticationManager.authenticate(any())).thenReturn(authentication);

		BasicException error = Assertions.assertThrows(BasicException.class, () -> memberService.memberLogin(
			new LoginRequest(username, wrongPassword)
		));

	}

}
