package com.nninjoon.userservice.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import com.nninjoon.userservice.jwt.JwtAuthenticationFilter;
import com.nninjoon.userservice.jwt.JwtTokenProvider;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@AllArgsConstructor
@EnableWebSecurity
public class SecurityConfig {

	private final JwtTokenProvider jwtTokenProvider;

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
						.requestMatchers("/user/register", "/user/login").permitAll()
						.anyRequest().authenticated()
				// .anyRequest().authenticated()
			)
			.addFilterAfter(jwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class)
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

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
		return authenticationConfiguration.getAuthenticationManager();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public JwtAuthenticationFilter jwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider) {
		log.info("Init JwtAuthentication Filter");
		return new JwtAuthenticationFilter(jwtTokenProvider);
	}
}
