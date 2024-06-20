package com.sns.sns.service.configuration.filter;

import com.sns.sns.service.domain.member.model.entity.Member;
import com.sns.sns.service.domain.member.service.MemberService;
import com.sns.sns.service.jwt.JwtTokenUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;


@Slf4j
@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtTokenUtil jwtTokenUtil;
    private final MemberService memberService;
    private final static List<String> TOKEN_IN_PARAM_URLS = List.of("/api/v1/users/notification/subscribe");

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

            if (TOKEN_IN_PARAM_URLS.contains(request.getRequestURI())) {
                jwtToken = request.getQueryString().split("=")[1].trim();
            } else if (getHeader == null || !getHeader.startsWith("Bearer ")) {
                filterChain.doFilter(request, response);
                return;
            } else {
                jwtToken = getHeader.split(" ")[1].trim();
            }

            if (jwtTokenUtil.isTokenExpired(jwtToken)) {
                log.error("토큰 만료되었습니다.");
                filterChain.doFilter(request, response);
                return;
            }

            String userName = jwtTokenUtil.getUserName(jwtToken);
            Member member = memberService.loadMemberByMemberName(userName);

            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    member, null, member.getAuthorities());

            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authentication);

        } catch (RuntimeException e) {
            log.error("헤더를 가지고 오지 못했습니다.", e.toString());
            filterChain.doFilter(request, response);
            return;
        }

        filterChain.doFilter(request, response);
    }
}
