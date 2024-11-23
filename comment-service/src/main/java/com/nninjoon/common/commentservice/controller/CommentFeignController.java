package com.nninjoon.common.commentservice.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nninjoon.common.commentservice.model.CommentResponse;
import com.nninjoon.common.commentservice.service.CommentService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/internal/comments")
public class CommentFeignController {

	private final CommentService commentService;

	@GetMapping("/posts/{postId}")
	public ResponseEntity<List<CommentResponse>> getComments(@PathVariable Long postId) {
		List<CommentResponse> comments = commentService.getComments(postId);
		return ResponseEntity.ok(comments);
	}

	@GetMapping("/users/{userId}")
	public ResponseEntity<List<CommentResponse>> getComments(@PathVariable String userId) {
		List<CommentResponse> comments = commentService.getCommentsByUserId(userId);
		return ResponseEntity.ok(comments);
	}
}
