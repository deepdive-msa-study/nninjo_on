package com.nninjoon.userservice.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import com.nninjoon.userservice.filter.AuthenticationFilter;
import com.nninjoon.userservice.filter.TokenAuthenticationFilter;
import com.nninjoon.userservice.jwt.TokenProvider;
import com.nninjoon.userservice.service.CustomUserDetailsService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@AllArgsConstructor
@EnableWebSecurity
public class SecurityConfig {

	private final TokenProvider tokenProvider;
	private final CustomUserDetailsService customUserDetailsService;

	@Bean
	public SecurityFilterChain userSecurityFilterChain(HttpSecurity http) throws Exception {
		log.info("Init Security Filter");
		return http
			.csrf(AbstractHttpConfigurer::disable)
			.sessionManagement(sessionManagement
				-> sessionManagement.sessionCreationPolicy(
				SessionCreationPolicy.STATELESS
			))
			.authorizeHttpRequests((authorizeRequests) ->
					authorizeRequests
						.requestMatchers(SWAGGER_PATTERNS).permitAll()
						.requestMatchers(STATIC_RESOURCES_PATTERNS).permitAll()
						.requestMatchers(PERMIT_ALL_PATTERNS).permitAll()
						.requestMatchers(PUBLIC_ENDPOINTS).permitAll()
						.anyRequest().authenticated()
			)
			.addFilter(authenticationFilter())
			.addFilterBefore(tokenAuthenticationFilter(tokenProvider), UsernamePasswordAuthenticationFilter.class)
			.httpBasic((httpBasic)-> httpBasic.disable())
			.cors(cors -> cors.configurationSource(request -> {
				CorsConfiguration config = new CorsConfiguration();
				config.setAllowedOrigins(List.of("*")); // 허용된 출처 설정
				config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
				config.setAllowedHeaders(List.of("*"));
				return config;
			}))
			.build();
	}

	private static final String[] SWAGGER_PATTERNS = {
		"/swagger-ui/**",
		"/actuator/**",
		"/v3/api-docs/**",
	};

	private static final String[] STATIC_RESOURCES_PATTERNS = {
		"/img/**",
		"/css/**",
		"/js/**"
	};

	private static final String[] PERMIT_ALL_PATTERNS = {
		"/error",
		"/favicon.ico",
		"/index.html",
		"/",
		"/actuator/**"
	};

	private static final String[] PUBLIC_ENDPOINTS = {
		"/signup",
		"/login"
	};

	@Bean
	public AuthenticationManager authenticationManager(
		BCryptPasswordEncoder bCryptPasswordEncoder
	) {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		authProvider.setUserDetailsService(customUserDetailsService);
		authProvider.setPasswordEncoder(bCryptPasswordEncoder);
		return new ProviderManager(authProvider);
	}

	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}

	public TokenAuthenticationFilter tokenAuthenticationFilter(TokenProvider tokenProvider) {
		return new TokenAuthenticationFilter(tokenProvider);
	}

	public AuthenticationFilter authenticationFilter() {
		AuthenticationFilter authenticationFilter = new AuthenticationFilter(tokenProvider);
		authenticationFilter.setAuthenticationManager(authenticationManager(bCryptPasswordEncoder()));
		return authenticationFilter;
	}

}
