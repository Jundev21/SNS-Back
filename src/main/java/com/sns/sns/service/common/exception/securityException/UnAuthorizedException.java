package com.sns.sns.service.common.exception.securityException;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sns.sns.service.common.basicResponse.DataResponse;
import com.sns.sns.service.common.exception.ErrorCode;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class UnAuthorizedException implements AuthenticationEntryPoint {
	private final ObjectMapper objectMapper;

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException
	) throws IOException, ServletException {
		log.info("spring security 에러 체크");
		HttpStatus errorCode = null;
		String errorMessage = null;
		if(authException instanceof BadCredentialsException || authException instanceof InternalAuthenticationServiceException){
			errorMessage = ErrorCode.INVALID_LOGIN_INFO.getMsg();
			errorCode = ErrorCode.INVALID_LOGIN_INFO.getStatus();
		} else if(authException instanceof CredentialsExpiredException){
			errorMessage = ErrorCode.INVALID_TOKEN.getMsg();
			errorCode = ErrorCode.INVALID_TOKEN.getStatus();
		}
		else{
			errorMessage = ErrorCode.UNAUTHORIZED_USER.getMsg();
			errorCode = ErrorCode.UNAUTHORIZED_USER.getStatus();

		}
		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		response.setCharacterEncoding("UTF-8");
		response.getWriter()
			.write(
				objectMapper.writeValueAsString(
					DataResponse.failBodyResponse(errorCode, errorMessage)
				)
			);
		log.error(errorMessage +" " +errorCode);
	}

}