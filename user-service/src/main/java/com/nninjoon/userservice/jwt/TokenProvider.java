package com.nninjoon.userservice.jwt;

import static io.jsonwebtoken.SignatureAlgorithm.HS256;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import javax.crypto.SecretKey;

import org.springframework.core.env.Environment;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import com.nninjoon.userservice.domain.User;
import com.nninjoon.userservice.dto.JwtTokenDto;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j // 롬복을 이용하여 로깅을 위한 Logger 선언
@Component
@RequiredArgsConstructor
public class TokenProvider {
	private final Environment env;

	// @Autowired
	// private RefreshTokenInfoRedisRepository refreshTokenInfoRepository; // RefreshToken 정보를 저장하기 위한 Repository
	private SecretKey secretKey;

	@PostConstruct
	private void init() {
		String key = env.getProperty("jwt.secret");

		if (key == null || key.isEmpty()) {
			throw new IllegalStateException("token.secret 환경 변수가 설정되지 않았습니다!");
		}
		secretKey = Keys.hmacShaKeyFor(key.getBytes());
	}

	public JwtTokenDto generateToken(Authentication authentication) {
		String authorities = authentication.getAuthorities().stream()
			.map(GrantedAuthority::getAuthority)
			.collect(Collectors.joining(","));

		User userDetails = (User) authentication.getPrincipal();
		Long userId = userDetails.getId();
		long now = (new Date()).getTime();
		Date issuedAt = new Date();

		Map<String, Object> headers = new HashMap<>();
		headers.put("alg", "HS256");
		headers.put("typ", "JWT");

		String accessToken = Jwts.builder()
			.setHeader(createHeaders())
			.setSubject("accessToken")
			.claim("iss", "off")
			.claim("email", authentication.getName())
			.claim("userId", userId)
			.claim("auth", authorities)
			.setExpiration(new Date(now + 1800000)) // 토큰 만료 30분
			.setIssuedAt(issuedAt)
			.signWith(secretKey, HS256)
			.compact();

		String refreshToken = Jwts.builder()
			.setHeader(createHeaders())
			.setSubject("refreshToken")
			.claim("iss", "off")
			.claim("email", authentication.getName())
			.claim("id", userId)
			.claim("auth", authorities)
			.claim("add", "ref")
			.setExpiration(new Date(now + 604800000))
			.setIssuedAt(issuedAt)
			.signWith(secretKey, HS256)
			.compact();

		return JwtTokenDto.builder()
			.grantType("Bearer")
			.accessToken(accessToken)
			.refreshToken(refreshToken)
			.build();
	}

	// // RefreshToken을 이용하여 AccessToken을 재발급하는 메서드
	// public JwtTokenDto refreshToken(String refreshToken) {
	//     try {
	//         // Refresh Token 복호화
	//         Authentication authentication = getAuthentication(refreshToken);
	//         // Redis에 저장된 Refresh Token 정보 가져오기
	//         RefreshTokenInfo redisRefreshTokenInfo = refreshTokenInfoRepository.findById(authentication.getName()).orElseThrow();

	//         JwtTokenDto refreshGetToken = null;
	//         // Redis에 저장된 Refresh Token과 요청된 Refresh Token이 일치할 경우
	//         if (refreshToken.equals(redisRefreshTokenInfo.getRefreshToken())) {
	//             refreshGetToken = generateToken(authentication); // 토큰 재발급

	//             saveToken(refreshGetToken, authentication); // Redis에 새로운 Refresh Token 정보 저장
	//             return refreshGetToken; // 새로운 토큰 반환
	//         } else {
	//             log.warn("does not exist Token"); // Redis에 저장된 Refresh Token이 존재하지 않을 경우
	//             throw new ApiException(ExceptionEnum.TOKEN_DOES_NOT_EXIST); // 해당 예외 처리
	//         }
	//     } catch (NullPointerException e) {
	//         log.warn("does not exist Token"); // Refresh Token이 존재하지 않을 경우
	//         throw new ApiException(ExceptionEnum.TOKEN_DOES_NOT_EXIST); // 해당 예외 처리
	//     } catch (SignatureException e) {
	//         log.warn("Invalid Token Info"); // 토큰 정보가 잘못된 경우
	//         throw new ApiException(ExceptionEnum.INVALID_TOKEN_INFO); // 해당 예외 처리
	//     } catch (NoSuchElementException e) {
	//         log.warn("no such Token value"); // Redis에 해당 토큰이 존재하지 않을 경우
	//         throw new ApiException(ExceptionEnum.TOKEN_DOES_NOT_EXIST); // 해당 예외 처리
	//     }
	// }



	// JWT 토큰의 Header 정보를 생성하는 메서드
	private static Map<String, Object> createHeaders() {
		Map<String, Object> headers = new HashMap<>();
		headers.put("alg", "HS256"); // 알고리즘 정보 설정
		headers.put("typ", "JWT"); // 토큰 타입 정보 설정
		return headers; // 생성된 Header 정보 반환
	}
}
