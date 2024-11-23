package com.nninjoon.userservice.controller;

import static org.springframework.http.HttpStatus.CREATED;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nninjoon.userservice.dto.request.UserCreateRequest;
import com.nninjoon.userservice.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/open-api")
public class UserOpenApiController {
	private final UserService userService;

	@PostMapping("/signup")
	public ResponseEntity<Void> create(@RequestBody UserCreateRequest request) {
		userService.create(request);
		return ResponseEntity.status(CREATED).build();
	}
}
