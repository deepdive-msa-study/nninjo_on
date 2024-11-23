package com.nninjoon.postservice.controller;

import static org.springframework.http.HttpStatus.CREATED;

import com.nninjoon.postservice.dto.PostDetailResponse;
import com.nninjoon.postservice.dto.PostPersistResponse;
import com.nninjoon.postservice.dto.PostUploadRequest;
import com.nninjoon.postservice.service.PostService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts")
public class PostApiController {
	private final PostService postService;

	@PostMapping
	public ResponseEntity<PostPersistResponse> writePost(@RequestBody PostUploadRequest dto, @RequestHeader("X-User-Id") String userId) {
		return ResponseEntity.status(CREATED).body(postService.savePost(dto, userId));
	}

	@GetMapping("/{postId}")
	public ResponseEntity<PostDetailResponse> updatePost(@PathVariable Long postId, @RequestHeader("X-User-Id") String userId) {
		return ResponseEntity.ok(postService.findById(postId, userId));
	}

	@PatchMapping("/{postId}")
	public ResponseEntity<Void> updatePost(@PathVariable Long postId,
		@RequestBody PostUploadRequest request, @RequestHeader("X-User-Id") String userId) {
		postService.updatePost(request, postId, userId);
		return ResponseEntity.noContent().build();
	}

	@DeleteMapping("/{postId}")
	public ResponseEntity<Void> deletePost(@PathVariable Long postId, @RequestHeader("X-User-Id") String userId) {
		postService.deletePost(postId, userId);
		return ResponseEntity.noContent().build();
	}
}

