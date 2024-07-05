package com.sns.sns.service.configuration;

import static org.springframework.security.web.util.matcher.RegexRequestMatcher.*;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.CorsConfigurer;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.annotation.web.configurers.FormLoginConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.sns.sns.service.common.exception.securityException.UnAuthorizedException;
import com.sns.sns.service.jwt.JwtFilter;
import com.sns.sns.service.jwt.JwtTokenInfo;
import com.sns.sns.service.jwt.MemberDetailService;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class AuthenticationConfig {

	private final JwtTokenInfo jwtTokenInfo;
	private final MemberDetailService memberDetailService;
	private final UnAuthorizedException unAuthorizedException;

	@Bean
	public WebSecurityCustomizer webSecurityCustomizer() {
		return (web) -> web.ignoring()
			.requestMatchers(new AntPathRequestMatcher("/h2-console/**"))
			.requestMatchers(regexMatcher("^(?!/api/).*"));
	}

	private static final String[] SWAGGER_PAGE = {
		"/swagger-ui/**", "/v3/api-docs/**", "/api-docs/**"
	};

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
		return config.getAuthenticationManager();
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
				.requestMatchers(SWAGGER_PAGE).permitAll()
				.requestMatchers("/api/*/users/join").permitAll()
				.requestMatchers("/api/*/users/login").permitAll()
				.requestMatchers(HttpMethod.GET, "/api/v1/board").permitAll()
				.requestMatchers("/api/v1/users/login").permitAll()
				.requestMatchers("/api/v1/users/register").permitAll()
				.requestMatchers(new AntPathRequestMatcher("/api/v1/favorite/board/*")).permitAll()
				.requestMatchers(new AntPathRequestMatcher("/api/v1/user/board/*/comment")).permitAll()
				.requestMatchers(new AntPathRequestMatcher("/api/v1/board/searching/*")).permitAll()
				.requestMatchers("/api/**").authenticated()
				.anyRequest().permitAll()

			)
			.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
			.addFilterBefore(new JwtFilter(jwtTokenInfo, memberDetailService),
				UsernamePasswordAuthenticationFilter.class)
			.exceptionHandling((exceptionConfig) ->
				exceptionConfig.authenticationEntryPoint(unAuthorizedException)
			)
			.build();

	}

}
