package com.example.msablog.user.config.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.example.msablog.user.dto.JwtTokenDto;
import com.example.msablog.user.entity.User;
import com.example.msablog.user.repository.UserRepository;
import com.example.msablog.user.service.custom.CustomUserDetails;

import java.security.Key;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j // 롬복을 이용하여 로깅을 위한 Logger 선언
@Component
public class JwtTokenProvider {
    private final UserRepository userRepository;
    private final Key key; 

   // @Autowired
   // private RefreshTokenInfoRedisRepository refreshTokenInfoRepository; // RefreshToken 정보를 저장하기 위한 Repository

    public JwtTokenProvider(UserRepository userRepository, @Value("${jwt.secret}") String secretKey) {
        this.userRepository = userRepository;
        byte[] keyBytes = Decoders.BASE64.decode(secretKey); 
        this.key = Keys.hmacShaKeyFor(keyBytes); 
    }

    public JwtTokenDto generateToken(Authentication authentication) {
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Long userId = userDetails.getUserId();
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
                .claim("id", userId)
                .claim("auth", authorities)
                .setExpiration(new Date(now + 1800000)) // 토큰 만료 30분
                .setIssuedAt(issuedAt)
                .signWith(key, SignatureAlgorithm.HS256)
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
                .signWith(key, SignatureAlgorithm.HS256) 
                .compact(); 

        return JwtTokenDto.builder()
                .grantType("Bearer") 
                .accessToken(accessToken)
                .refreshToken(refreshToken) 
                .build(); 
    }

    public Authentication getAuthentication(String token) {
        Claims claims = parseClaims(token);

        if (claims.get("auth") == null) {
            log.error("No Authentication");
            throw new RuntimeException("No Authentication");
        }

        Collection<? extends GrantedAuthority> authorities = Arrays.stream(claims.get("auth").toString().split(","))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
        
        String email = (String) claims.get("email");
        User user = userRepository.findByEmail(email);
        if (user == null) {
            log.error("User Not Found");
            throw new UsernameNotFoundException("User Not Found");
        }

        CustomUserDetails principal = new CustomUserDetails(user);
        return new UsernamePasswordAuthenticationToken(principal, token, authorities);
    }

    // JWT 토큰의 유효성을 검증하는 메서드
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token); // 토큰 파싱하여 유효성 검증
            return true; // 유효한 토큰일 경우 true 반환
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.error(e.getMessage());
            return false; // 토큰이 잘못된 경우 예외 처리
        } catch (ExpiredJwtException e) {
            log.error(e.getMessage());
            return false; // 토큰이 만료된 경우 예외 처리
        } catch (UnsupportedJwtException | IllegalArgumentException e) {
            log.error(e.getMessage());
            return false; // 지원하지 않는 토큰이거나 잘못된 형식의 경우 예외 처리
        } catch (Exception e){
            log.error(e.getMessage());
            return false; // 그 외 예외 처리
        }
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

    // JWT 토큰을 파싱하여 클레임 정보를 반환하는 메서드
    private Claims parseClaims(String accessToken) {
        try {
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken).getBody(); // 토큰 파싱하여 클레임 정보 반환
        } catch (ExpiredJwtException e) {
            log.error("Expired Token");
            return e.getClaims(); // 만료된 토큰의 경우 클레임 정보 반환
        }
    }

    // JWT 토큰의 Header 정보를 생성하는 메서드
    private static Map<String, Object> createHeaders() {
        Map<String, Object> headers = new HashMap<>();
        headers.put("alg", "HS256"); // 알고리즘 정보 설정
        headers.put("typ", "JWT"); // 토큰 타입 정보 설정
        return headers; // 생성된 Header 정보 반환
    }
}