package com.sns.sns.service.jwt;

import com.sns.sns.service.common.exception.ErrorCode;
import com.sns.sns.service.jwt.JwtTokenInfo;
import com.sns.sns.service.jwt.MemberDetailService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

	private final JwtTokenInfo jwtTokenInfo;
	private final MemberDetailService memberDetailService;
	//jwt filter 는 클라이언트 요청이 올때 서블릿에 바로 들어가지않고 먼저 필터링을 거친다.
	@Override
	protected void doFilterInternal(
		HttpServletRequest request, HttpServletResponse response, FilterChain filterChain
	) throws ServletException, IOException {

		//      필터링이 실행될때 가장먼저 헤더를 확인한다. 헤더에 jwt 토큰이 있는지 없는지를 먼저체크
		//      jwt 토큰을 헤더에 담는지 쿠키에담는지 먼저 체크확인

		final String getHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
		final String tokenPreFix = "Bearer ";
		final String jwtToken;
		try {

			if (getHeader == null || !getHeader.startsWith(tokenPreFix)) {
				filterChain.doFilter(request, response);
				return;
			} else {
				jwtToken = getHeader.split(" ")[1].trim();
			}

			if (jwtTokenInfo.isValidToken(jwtToken)) {
				log.error("토큰 만료되었습니다.");
				log.error(ErrorCode.INVALID_TOKEN.getMsg());
				filterChain.doFilter(request, response);
				return;
			}

			String userLoginId = jwtTokenInfo.extractLoginId(jwtToken);
			//여기서 데이터베이스에있는지 체크
			//데이터 체크가 완료됐으면 다음 UsernameToken 을 통하여 인증을 거친다.
			//AbstractAuthenticationToken은 Authentication을 상속받는다는 것을 알 수 있다.
			//즉, UsernamePasswordAuthenticationToken은 추후 인증이 끝나고 SecurityContextHolder.getContext()에 등록될 Authentication 객체이다.

			UserDetails memberDetails = memberDetailService.loadUserByUsername(userLoginId);
			UsernamePasswordAuthenticationToken authenticationToken
				= new UsernamePasswordAuthenticationToken(memberDetails, null, memberDetails.getAuthorities());
			SecurityContextHolder.getContext().setAuthentication(authenticationToken);

		} catch (RuntimeException e) {
			log.error("헤더를 가지고 오지 못했습니다.", e.toString());
		}
		filterChain.doFilter(request, response);
	}
}
