package com.nninjoon.apigatewayservice.filter;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import com.nninjoon.apigatewayservice.TokenProvider;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class AuthorizationHeaderFilter extends AbstractGatewayFilterFactory<AuthorizationHeaderFilter.Config> {

	private final TokenProvider tokenProvider;
	private static final String TOKEN_PREFIX = "Bearer ";

	public AuthorizationHeaderFilter(TokenProvider tokenProvider) {
		super(Config.class);
		this.tokenProvider = tokenProvider;
	}

	@Override
	public GatewayFilter apply(Config config) {
		return (exchange, chain) -> {
			String authorizationHeader = exchange.getRequest().getHeaders().getFirst(AUTHORIZATION);

			// 토큰 추출 및 검증
			String accessToken = getAccessToken(authorizationHeader);
			if (!tokenProvider.validateToken(accessToken)) {
				return onError(exchange, "Invalid JWT token", HttpStatus.UNAUTHORIZED);
			}

			// userId 추출
			String userId = tokenProvider.getUserIdFromToken(accessToken);

			// 요청 헤더에 userId 추가
			ServerWebExchange modifiedExchange = exchange.mutate()
				.request(exchange.getRequest().mutate()
					.header("X-User-Id", userId) // userId 추가
					.build())
				.build();

			return chain.filter(modifiedExchange);
		};
	}

	private String getAccessToken(String authorizationHeader) {
		if (authorizationHeader == null || !authorizationHeader.startsWith(TOKEN_PREFIX)) {
			throw new IllegalArgumentException("Missing or invalid Authorization header");
		}
		return authorizationHeader.substring(TOKEN_PREFIX.length());
	}

	private Mono<Void> onError(ServerWebExchange exchange, String err, HttpStatus httpStatus) {
		exchange.getResponse().setStatusCode(httpStatus);
		log.error(err);
		return exchange.getResponse().setComplete();
	}

	public static class Config {
		// 추가 설정이 필요 없으면 빈 클래스로 유지
	}
}
