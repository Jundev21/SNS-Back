package com.sns.sns.service.jwt;

import com.sns.sns.service.common.exception.ErrorCode;
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
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtTokenInfo jwtTokenInfo;
    private final MemberDetailService memberDetailService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request, HttpServletResponse response, FilterChain filterChain
    ) throws ServletException, IOException {
        final String getHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String tokenPreFix = "Bearer ";
        final String jwtToken;
            if (getHeader != null && getHeader.startsWith(tokenPreFix)) {
                jwtToken = getHeader.split(" ")[1].trim();
                if (jwtTokenInfo.isValidToken(jwtToken)) {
                    log.error("토큰 만료되었습니다.");
                    log.error(ErrorCode.INVALID_TOKEN.getMsg());
                    filterChain.doFilter(request, response);
                    return;
                }

                String userLoginId = jwtTokenInfo.extractLoginId(jwtToken);
                UserDetails memberDetails = memberDetailService.loadUserByUsername(userLoginId);
                UsernamePasswordAuthenticationToken authenticationToken
                        = new UsernamePasswordAuthenticationToken(memberDetails, null, memberDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }

        filterChain.doFilter(request, response);
    }
}
