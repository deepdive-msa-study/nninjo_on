package com.nninjoon.postservice.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nninjoon.postservice.dto.PostResponse;
import com.nninjoon.postservice.service.PostService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/internal/posts")
public class PostFeignController {
	private final PostService postService;

	@GetMapping("/{userId}")
	public ResponseEntity<List<PostResponse>> getMyPosts(@PathVariable String userId) {
		return ResponseEntity.ok(postService.findAllByUserId(userId));
	}
}
