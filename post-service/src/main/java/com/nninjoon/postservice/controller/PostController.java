package com.nninjoon.postservice.controller;

import com.nninjoon.postservice.dto.CommentUploadRequestDto;
import com.nninjoon.postservice.dto.PostUploadRequestDto;
import com.nninjoon.postservice.dto.ResponseDto;
import com.nninjoon.postservice.service.CommentService;
import com.nninjoon.postservice.service.PostService;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class PostController {
	private final PostService postService;
	private final CommentService commentService;

	@GetMapping("/list")
	public ResponseDto findAll(@RequestParam(value = "page", defaultValue = "1") int page,
		@RequestParam(value = "size", defaultValue = "10") int size) {
		Pageable pageable = PageRequest.of(page - 1, size);
		return ResponseDto.success(postService.findAll(pageable));
	}

	@GetMapping("/post")
	public ResponseDto findById(@PathVariable Long postId) {
		return ResponseDto.success(postService.findById(postId));
	}

	@PostMapping("/post")
	public ResponseDto writePost(@RequestBody PostUploadRequestDto dto, @RequestHeader("X-User-Id") String userId) {
		return ResponseDto.success(postService.savePost(dto, userId));
	}


	@GetMapping("/post/{postId}")
	public ResponseDto updatePost(@PathVariable Long postId, @RequestHeader("X-User-Id") String userId) {
		return ResponseDto.success(postService.findById(postId));
	}

	@PatchMapping("/post/{postId}")
	public ResponseDto updatePost(@PathVariable Long postId,
		@RequestBody PostUploadRequestDto dto) {
		return ResponseDto.success(postService.updatePost(dto, postId));
	}

	@DeleteMapping("/post/{postId}")
	public ResponseDto deletePost(@PathVariable Long postId, @RequestHeader) {
		postService.deletePost(postId);
		return ResponseDto.success();
	}

	// 댓글 작성
	@PostMapping("{userId}/comment/{postId}")
	public ResponseDto writeComment(@PathVariable Long postId,
		@RequestBody CommentUploadRequestDto commentDto) {
		return ResponseDto.success(commentService.writeComment(postId, commentDto));
	}


	// 댓글 수정
	@PatchMapping("/{userId}/comment/{commentId}}")
	public ResponseDto updateComment(@PathVariable Long userId,
		@PathVariable Long commentId,
		@RequestBody CommentUploadRequestDto commentDto) {
		return ResponseDto.success(commentService.update(commentId, commentDto));
	}


	// 댓글 삭제
	@DeleteMapping("/{userId}/comment/{commentId}}")
	public ResponseDto deleteComment(@PathVariable Long userId,
		@PathVariable Long commentId) {
		commentService.delete(commentId);
		return ResponseDto.success();
	}
}

