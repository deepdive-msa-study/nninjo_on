package com.nninjoon.userservice.jwt;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.nninjoon.userservice.domain.User;
import com.nninjoon.userservice.repository.UserRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class JwtUtil {
	private final UserRepository userRepository;

	public JwtUtil(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	public Long getCurrentUserId() {
		User userDetails = getCurrentCustomUserDetails();
		return userDetails.getId();
	}

	public String getCurrentUserEmail() {
		Authentication authentication = getAuthentication();
		return authentication.getName();
	}

	public User getCurrentUser() {
		String email = getCurrentUserEmail();
		User user = userRepository.findByEmail(email);
		if (user == null) {
			log.error("User not found");
			new UsernameNotFoundException("User not found");
		}
		return user;
	}

	private Authentication getAuthentication() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null || authentication.getPrincipal() == null) {
			log.error("No authentication information");
			throw new RuntimeException("No authentication information.");
		}
		return authentication;
	}

	private User getCurrentCustomUserDetails() {
		return (User) getAuthentication().getPrincipal();
	}
}

