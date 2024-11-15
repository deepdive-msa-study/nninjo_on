package com.nninjoon.userservice.controller;

import static org.springframework.http.HttpStatus.CREATED;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nninjoon.userservice.dto.JwtTokenDto;
import com.nninjoon.userservice.dto.LoginDto;
import com.nninjoon.userservice.dto.UserProfileResponse;
import com.nninjoon.userservice.dto.RegisterDto;
import com.nninjoon.userservice.dto.UpdateUserDto;
import com.nninjoon.userservice.dto.UserProfileDto;
import com.nninjoon.userservice.service.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class UserController {
	private final UserService userService;

	@PostMapping("/register")
	public ResponseEntity<Void> create(@RequestBody RegisterDto request) {
		userService.create(request);
		return ResponseEntity.status(CREATED).build();
	}

	@PostMapping("/login")
	public ResponseEntity<JwtTokenDto> login(@RequestBody LoginDto request) {
		return ResponseEntity.ok(userService.login(request));
	}

	@GetMapping("/mypage")
	public ResponseEntity<UserProfileResponse> getMyProfile() {
		return ResponseEntity.ok(userService.getMyProfile());
	}

	@PostMapping("/mypage/update")
	public ResponseEntity<Void> updateMe(@RequestBody UpdateUserDto request) {
		userService.update(request);
		return ResponseEntity.noContent().build();
	}

	@PatchMapping("/mypage/delete")
	public ResponseEntity<Void> deleteMe() {
		userService.deleteMe();
		return ResponseEntity.noContent().build();
	}

	@GetMapping("/mypage/{id}")
	public ResponseEntity<UserProfileDto> getUser(@PathVariable Long id) {
		UserProfileDto response = userService.getUserProfile(id);
		return ResponseEntity.ok(response);
	}

}

