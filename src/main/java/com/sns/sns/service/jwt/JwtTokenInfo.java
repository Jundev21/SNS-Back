package com.sns.sns.service.jwt;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

//jwt 에 대한정보들 - jwt 유효기간, jwt 정보 추출,
@Component
@Slf4j
public class JwtTokenInfo {

	@Value("${jwt.secret.key}")
	private String secretKey;

	@Value("${jwt.token.expired-time}")
	private Long expiredMax;

	public String generateToken(String loginId) {
		Claims claims = Jwts.claims();
		claims.put("loginId", loginId);
		return Jwts.builder()
			.setClaims(claims)
			.setIssuedAt(new Date(System.currentTimeMillis()))
			.setExpiration(new Date(System.currentTimeMillis() + expiredMax))
			.signWith(getKey(secretKey), SignatureAlgorithm.HS256)
			.compact();
	}

	public String extractLoginId(String jwt) {
		return extractClaim(jwt).get("loginId", String.class);
	}

	public boolean isValidToken(String jwt) {

		Date expriedDate = extractClaim(jwt).getExpiration();
		log.info("토큰 유효 체크 / 유효기간" + expriedDate);
		return expriedDate.before(new Date());
	}

	//key 값 생성
	public Key getKey(String key) {
		byte[] keyBytes = key.getBytes(StandardCharsets.UTF_8);
		return Keys.hmacShaKeyFor(keyBytes);
	}

	public Claims extractClaim(String jwt) {
		return Jwts.parserBuilder()
			.setSigningKey(getKey(secretKey))
			.build()
			.parseClaimsJws(jwt)
			.getBody();
	}
}
