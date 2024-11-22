package com.nninjoon.apigatewayservice;

import javax.crypto.SecretKey;

import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class TokenProvider {

	private final Environment env;

	private SecretKey secretKey;

	@PostConstruct
	private void init() {
		String key = env.getProperty("token.secret");

		if (key == null || key.isEmpty()) {
			throw new IllegalStateException("token.secret 환경 변수가 설정되지 않았습니다!");
		}

		secretKey = Keys.hmacShaKeyFor(key.getBytes());
	}

	// JWT 토큰의 유효성을 검증하는 메서드
	public boolean validateToken(String token) {
		if (token == null || token.trim().isEmpty()) {
			return false; // 유효하지 않은 토큰으로 처리
		}

		try {
			Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token); // 토큰 파싱하여 유효성 검증
			return true; // 유효한 토큰일 경우 true 반환
		} catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
			log.error("Invalid JWT Token: {}", e.getMessage()); // 잘못된 토큰
			return false;
		} catch (ExpiredJwtException e) {
			// 만료된 토큰은 로그를 남기지 않음
			return false;
		} catch (UnsupportedJwtException | IllegalArgumentException e) {
			log.error("Unsupported or malformed JWT Token: {}", e.getMessage()); // 지원하지 않는 토큰이나 잘못된 형식
			return false;
		} catch (Exception e) {
			// 그 외 예외는 로그를 남기지 않음
			return false;
		}
	}

	public String getUserIdFromToken(String token) {
		try {
			Claims claims = Jwts.parserBuilder()
				.setSigningKey(secretKey)
				.build()
				.parseClaimsJws(token)
				.getBody();
			return claims.get("userId", String.class);
		} catch (Exception e) {
			log.error("Failed to extract userId from token: {}", e.getMessage());
			throw new IllegalArgumentException("Invalid token: unable to extract userId");
		}
	}
}
