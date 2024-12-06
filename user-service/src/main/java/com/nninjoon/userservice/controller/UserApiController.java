package com.nninjoon.userservice.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nninjoon.userservice.messagequeue.UserProducer;
import com.nninjoon.userservice.model.response.UserResponse;
import com.nninjoon.userservice.model.request.UserUpdateRequest;
import com.nninjoon.userservice.service.UserService;

import io.micrometer.core.annotation.Timed;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class UserApiController {
	private final UserService userService;
	private final UserProducer userProducer;

	@GetMapping("/mypage")
	@Timed(value = "users.mypage", longTask = true)
	public ResponseEntity<UserResponse> getMyProfile(@RequestHeader("X-User-Id") String userId) {
		return ResponseEntity.ok(userService.getMyProfile(userId));
	}

	@PatchMapping("/mypage")
	public ResponseEntity<Void> updateMe(@RequestHeader("X-User-Id") String userId, @RequestBody UserUpdateRequest request) {
		userService.update(userId, request);
		return ResponseEntity.noContent().build();
	}

	@PatchMapping("/mypage/delete")
	public ResponseEntity<Void> deleteMe(@RequestHeader("X-User-Id") String userId) {
		userService.deleteMe(userId);
		return ResponseEntity.noContent().build();
	}

}

