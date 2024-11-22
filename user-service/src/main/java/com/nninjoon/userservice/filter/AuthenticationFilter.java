package com.nninjoon.userservice.filter;

import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nninjoon.userservice.domain.User;
import com.nninjoon.userservice.dto.RequestLogin;
import com.nninjoon.userservice.jwt.TokenProvider;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {

	private final TokenProvider tokenProvider;

	public AuthenticationFilter(TokenProvider tokenProvider) {
		this.tokenProvider = tokenProvider;
		setFilterProcessesUrl("/login");
	}

	@Override
	public Authentication attemptAuthentication(
		HttpServletRequest request,
		HttpServletResponse response
	) throws AuthenticationException {
		try{
			RequestLogin creds = new ObjectMapper().readValue(request.getInputStream(), RequestLogin.class);

			return getAuthenticationManager().authenticate(
				new UsernamePasswordAuthenticationToken(
					creds.getEmail(),
					creds.getPassword(), new ArrayList<>()
				)
			);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	protected void successfulAuthentication(
		HttpServletRequest request,
		HttpServletResponse response,
		FilterChain chain,
		Authentication auth
	) {
		String accessToken = tokenProvider.generateToken(auth).getAccessToken();

		response.addHeader("Authorization", "Bearer " + accessToken);
	}
}
