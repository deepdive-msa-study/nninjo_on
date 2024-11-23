package com.nninjoon.userservice.filter;

import java.io.IOException;
import java.util.ArrayList;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nninjoon.userservice.dto.request.UserLoginRequest;
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
			UserLoginRequest req = new ObjectMapper().readValue(request.getInputStream(), UserLoginRequest.class);

			return getAuthenticationManager().authenticate(
				new UsernamePasswordAuthenticationToken(
					req.email(),
					req.password(), new ArrayList<>()
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
		String accessToken = tokenProvider.generateToken(auth).accessToken();

		response.addHeader("Authorization", "Bearer " + accessToken);
	}
}
