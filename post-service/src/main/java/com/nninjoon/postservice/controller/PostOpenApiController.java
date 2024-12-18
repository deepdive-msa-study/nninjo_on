package com.nninjoon.postservice.controller;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nninjoon.postservice.service.PostService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/open-api/posts")
public class PostOpenApiController {
	private final PostService postService;

	@GetMapping
	public ResponseEntity<Object> findAll(@RequestParam(value = "page", defaultValue = "1") int page,
		@RequestParam(value = "size", defaultValue = "10") int size) {
		Pageable pageable = PageRequest.of(page - 1, size);
		return ResponseEntity.ok(postService.findAll(pageable));
	}

	@GetMapping("/{postId}")
	public ResponseEntity<Object> findById(@PathVariable Long postId) {
		return ResponseEntity.ok(postService.findById(postId));
	}
}
