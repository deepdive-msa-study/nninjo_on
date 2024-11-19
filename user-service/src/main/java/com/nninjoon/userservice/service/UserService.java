package com.nninjoon.userservice.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nninjoon.userservice.domain.User;
import com.nninjoon.userservice.dto.UserProfileResponse;
import com.nninjoon.userservice.dto.RegisterDto;
import com.nninjoon.userservice.dto.UpdateUserDto;
import com.nninjoon.userservice.dto.UserPersistResponse;
import com.nninjoon.userservice.dto.UserProfileDto;
import com.nninjoon.userservice.jwt.TokenProvider;
import com.nninjoon.userservice.jwt.JwtUtil;
import com.nninjoon.userservice.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final JwtUtil jwtUtil;

	@Transactional
	public UserPersistResponse create(RegisterDto dto) {
		if (userRepository.findByEmail(dto.getEmail()) != null) {
			throw new IllegalArgumentException("Email already in use: " + dto.getEmail());
		}

		User user = User.create(
			dto.getName(),
			dto.getEmail(),
			passwordEncoder.encode(dto.getPassword())
		);

		userRepository.save(user);
		return UserPersistResponse.of(user);
	}

	// public JwtTokenDto login(RequestLogin request) {
	// 	Authentication authentication = authenticationManager.authenticate(
	// 		new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
	// 	);
	// 	SecurityContextHolder.getContext().setAuthentication(authentication);
	// 	JwtTokenDto jwtTokenDto = tokenProvider.generateToken(authentication);
	//
	// 	return jwtTokenDto;
	// }

	@Transactional(readOnly = true)
	public UserProfileResponse getMyProfile() {
		User user = jwtUtil.getCurrentUser();
		return UserProfileResponse.from(user);
	}

	@Transactional
	public UserProfileResponse update(UpdateUserDto dto) {
		User user = jwtUtil.getCurrentUser();

		user.updateName(dto.name());
		user.updateEmail(dto.email());

		return UserProfileResponse.from(user);
	}

	@Transactional
	public void deleteMe() {
		User user = jwtUtil.getCurrentUser();
		user.delete();
	}

	@Transactional(readOnly = true)
	public UserProfileDto getUserProfile(Long id) {
		User found = userRepository.findById(id).orElseThrow(
			() -> new RuntimeException("No Such User Found")
		);
		return new UserProfileDto(found.getName(), found.getEmail());
	}
}
