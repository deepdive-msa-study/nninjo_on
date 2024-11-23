package com.nninjoon.postservice.controller;

import java.util.List;

import com.nninjoon.postservice.dto.CommentUploadRequest;
import com.nninjoon.postservice.dto.PostResponse;
import com.nninjoon.postservice.dto.PostUploadRequest;
import com.nninjoon.postservice.dto.ResponseDto;
import com.nninjoon.postservice.service.CommentService;
import com.nninjoon.postservice.service.PostService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class PostApiController {
	private final PostService postService;
	private final CommentService commentService;

	@PostMapping("/post")
	public ResponseDto writePost(@RequestBody PostUploadRequest dto, @RequestHeader("X-User-Id") String userId) {
		return ResponseDto.success(postService.savePost(dto, userId));
	}

	@GetMapping("/post/update/{postId}")
	public ResponseDto updatePost(@PathVariable Long postId, @RequestHeader("X-User-Id") String userId) {
		return ResponseDto.success(postService.findById(postId, userId));
	}

	@PatchMapping("/post/{postId}")
	public ResponseDto updatePost(@PathVariable Long postId,
		@RequestBody PostUploadRequest request, @RequestHeader("X-User-Id") String userId) {
		return ResponseDto.success(postService.updatePost(request, postId, userId));
	}

	@DeleteMapping("/post/{postId}")
	public ResponseDto deletePost(@PathVariable Long postId, @RequestHeader("X-User-Id") String userId) {
		postService.deletePost(postId, userId);
		return ResponseDto.success();
	}

	// 댓글 작성
	@PostMapping("/comment/{postId}")
	public ResponseDto writeComment(@RequestHeader("X-User-Id") String userId,
		@PathVariable Long postId,
		@RequestBody CommentUploadRequest request) {
		return ResponseDto.success(commentService.writeComment(postId, request, userId));
	}


	// 댓글 수정
	@PatchMapping("/comment/{commentId}")
	public ResponseDto updateComment(@RequestHeader("X-User-Id") String userId,
		@PathVariable Long commentId,
		@RequestBody CommentUploadRequest commentDto) {
		commentService.update(commentId, commentDto, userId);
		return ResponseDto.success(ResponseEntity.noContent().build());
	}


	// 댓글 삭제
	@DeleteMapping("/comment/{commentId}")
	public ResponseDto deleteComment(@RequestHeader("X-User-Id") String userId,
		@PathVariable Long commentId) {
		commentService.delete(commentId, userId);
		return ResponseDto.success();
	}
}

