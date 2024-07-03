package com.sns.sns.service.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sns.sns.service.common.exception.BasicException;
import com.sns.sns.service.common.exception.ErrorCode;
import com.sns.sns.service.domain.member.dto.request.LoginRequest;
import com.sns.sns.service.domain.member.dto.request.MemberUpdateRequest;
import com.sns.sns.service.domain.member.dto.request.RegisterRequest;
import com.sns.sns.service.domain.member.dto.response.LoginResponse;
import com.sns.sns.service.domain.member.dto.response.RegisterResponse;
import com.sns.sns.service.domain.member.service.MemberService;

// user mock data
// controller 테스트
// controller 만 테스트 하지만 service 가 엮여 있기때문에 service 를 목으로 생성하여 참고 만한다.

@SpringBootTest
//Controller test annotation
@AutoConfigureMockMvc
public class UserControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private MemberService memberService;

	@Autowired
	private ObjectMapper objectMapper;

	ObjectMapper mapper = new ObjectMapper();

	@Test
	@DisplayName("회원가입 성공")
	public void memberRegisterSuccess() throws Exception {
		String userLoginId = "loginId";
		String userName = "name";
		String userPassword = "password";

		mockMvc.perform(post("/api/v1/users/register")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsBytes(
					new RegisterRequest(userLoginId, userName, userPassword, userPassword, "email")))
			).andDo(print())
			.andExpect(status().isOk());

	}

	//    ================================================

	@Test
	@DisplayName("로그인 성공")
	public void memberLoginSuccess() throws Exception {
		String userName = "name";
		String userPassword = "password";

		when(memberService.memberLogin(new LoginRequest(userName, userPassword))).thenReturn(
			new LoginResponse("token", "refreshToken"));

		mockMvc.perform(post("/api/v1/users/login")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsBytes(new LoginRequest(userName, userPassword)))
			).andDo(print())
			.andExpect(status().isOk());

	}

	@Test
	@WithMockUser
	@DisplayName("회원정보 수정 성공")
	public void memberEditInfoSuccess() throws Exception {
		MemberUpdateRequest memberUpdateRequest = new MemberUpdateRequest("123", "email");
		String requestJson = mapper.writeValueAsString(memberUpdateRequest);

		MockMultipartFile imageFile = new MockMultipartFile(
			"file",
			"test.txt",
			"text/plain",
			"test file".getBytes(StandardCharsets.UTF_8));
		MockMultipartFile data = new MockMultipartFile(
			"memberUpdateRequest",
			"memberUpdateRequest.json",
			"application/json",
			requestJson.getBytes()
		);

		when(memberService.memberLogin(new LoginRequest("name", "password"))).thenReturn(
			new LoginResponse("token", "refreshToken"));

		mockMvc.perform(MockMvcRequestBuilders.multipart("/api/v1/users")
			.file(imageFile)
			.file(data))
			.andDo(print())
			.andExpect(status().isOk());

	}

	@Test
	@DisplayName("로그인시 없는 아이디 일 경우")
	public void memberLoginNotExistID() throws Exception {
		String userName = "name";
		String userPassword = "password";

		when(memberService.memberLogin(new LoginRequest(userName, userPassword))).thenThrow(new BasicException(
			ErrorCode.NOT_EXIST_MEMBER, "존재하지 않는 회원"));

		mockMvc.perform(post("/api/v1/users/login")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsBytes(new LoginRequest(userName, userPassword)))
			).andDo(print())
			.andExpect(status().isNotFound());
	}

	@Test
	@DisplayName("로그인시 비밀번호가 틀릴 경우")
	public void memberLoginInvalidPassword() throws Exception {
		String userName = "name";
		String userPassword = "password";

		when(memberService.memberLogin(new LoginRequest(userName, userPassword))).thenThrow(
			new BasicException(ErrorCode.INVALID_PASSWORD, "비밀번호 다릅니다."));

		mockMvc.perform(post("/api/v1/users/login")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsBytes(new LoginRequest(userName, userPassword)))
			).andDo(print())
			.andExpect(status().isUnauthorized());
	}

}
