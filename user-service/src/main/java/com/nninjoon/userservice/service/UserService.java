package com.nninjoon.userservice.service;

import java.util.UUID;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nninjoon.userservice.domain.User;
import com.nninjoon.userservice.dto.response.UserResponse;
import com.nninjoon.userservice.dto.request.UserCreateRequest;
import com.nninjoon.userservice.dto.request.UserUpdateRequest;
import com.nninjoon.userservice.dto.response.UserPersistResponse;
import com.nninjoon.userservice.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	@Transactional
	public UserPersistResponse create(UserCreateRequest request) {
		if (userRepository.findByEmail(request.email()) != null) {
			throw new IllegalArgumentException("Email already in use: " + request.email());
		}

		String userId = UUID.randomUUID().toString();

		User user = User.create(
			request.name(),
			userId,
			request.email(),
			passwordEncoder.encode(request.password())
		);

		userRepository.save(user);
		return UserPersistResponse.of(user);
	}

	@Transactional(readOnly = true)
	public UserResponse getMyProfile(String userId) {
		User user = getUserByUserId(userId);
		return UserResponse.from(user);
	}

	@Transactional
	public UserResponse update(String userId, UserUpdateRequest request) {
		User user = getUserByUserId(userId);

		user.updateName(request.name());
		user.updateEmail(request.email());

		return UserResponse.from(user);
	}

	@Transactional
	public void deleteMe(String userId) {
		User user = getUserByUserId(userId);
		user.delete();
	}

	private User getUserByUserId(String userId) {
		return userRepository.findByUserId(userId)
			.orElseThrow(() -> new RuntimeException("No Such User Found"));
	}
}
