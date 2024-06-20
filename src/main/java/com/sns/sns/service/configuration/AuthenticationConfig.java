package com.sns.sns.service.configuration;


import com.sns.sns.service.configuration.filter.JwtFilter;
import com.sns.sns.service.domain.exception.CustomAuthenticationEntryPoint;
import com.sns.sns.service.domain.member.service.MemberService;
import com.sns.sns.service.jwt.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.CorsConfigurer;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.annotation.web.configurers.FormLoginConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import static org.springframework.security.web.util.matcher.RegexRequestMatcher.regexMatcher;

@Configuration
@RequiredArgsConstructor
public class AuthenticationConfig{

    private final JwtTokenUtil jwtTokenUtil;
    private final MemberService memberService;

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring()
                .requestMatchers(new AntPathRequestMatcher("/h2-console/**"))
                .requestMatchers(regexMatcher("^(?!/api/).*"));

    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .httpBasic(AbstractHttpConfigurer::disable)
                .csrf(CsrfConfigurer::disable)
                .cors(CorsConfigurer::disable)
                .formLogin(FormLoginConfigurer::disable)
                .authorizeHttpRequests((request) -> request
                        .requestMatchers("/api/v1/users/notification/subscribe/**").permitAll()
                        .requestMatchers("/api/*/users/join").permitAll()
                        .requestMatchers("/api/*/users/login").permitAll()
                        .requestMatchers(HttpMethod.GET,"/api/v1/board").permitAll()
                        .requestMatchers("/api/v1/users/login").permitAll()
                        .requestMatchers("/api/v1/users/register").permitAll()
                        .requestMatchers(new AntPathRequestMatcher("/api/v1/favorite/board/*")).permitAll()
                        .requestMatchers(new AntPathRequestMatcher("/api/v1/user/board/*/comment")).permitAll()
                        .requestMatchers("/api/**").authenticated()
                        .anyRequest().permitAll()

                )

                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(new JwtFilter(jwtTokenUtil, memberService), UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling((exceptionConfig) ->
                        exceptionConfig.authenticationEntryPoint(new CustomAuthenticationEntryPoint())
                )
                .build();

    }

}
