package com.sns.sns.service.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;


@Component
public class JwtTokenUtil {


    @Value("${jwt.secret.key}")
    private String secretKey;

    @Value("${jwt.token.expired-time}")
    private Long expiredMax;


    public String generateToken(String userName){
        Claims claims = Jwts.claims();
        claims.put("userName", userName);
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiredMax))
                .signWith(getKey(secretKey), SignatureAlgorithm.HS256)
                .compact();
    }


    public Key getKey(String key){
        byte [] keyBytes = key.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);

    }

//    public boolean isTokenValid(String token){
//
//
//    }

    public String getUserName(String token){
      return  extractClaims(token).get("userName",String.class);
    }

    public boolean isTokenExpired(String token){
        Date expriedDate = extractClaims(token).getExpiration();
        return expriedDate.before(new Date());
    }

    private Claims extractClaims(String token){
        return Jwts.parserBuilder()
                .setSigningKey(getKey(secretKey))
                .build()
                .parseClaimsJws(token)
                .getBody();

    }
}
