package com.nninjoon.common.commentservice.controller;

import static org.springframework.http.HttpStatus.CREATED;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nninjoon.common.commentservice.model.request.CommentRequest;
import com.nninjoon.common.commentservice.model.response.CommentPersistResponse;
import com.nninjoon.common.commentservice.service.CommentService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comments")
public class CommentApiController {

	private final CommentService commentService;

	@PostMapping("/{postId}")
	public ResponseEntity<CommentPersistResponse> create(@PathVariable("postId") Long postId, @RequestHeader("X-User-Id") String userId,
		@RequestBody CommentRequest request) {
		CommentPersistResponse response = commentService.create(postId, request, userId);
		return ResponseEntity.status(CREATED).body(response);
	}

	@PatchMapping("/{commentId}")
	public ResponseEntity<Void> update(@PathVariable("commentId") Long commentId, @RequestBody CommentRequest request,@RequestHeader("X-User-Id") String userId){
		commentService.update(commentId, request, userId);
		return ResponseEntity.noContent().build();
	}

	@DeleteMapping("/{commentId}")
	public ResponseEntity<Void> delete(@PathVariable("commentId") Long commentId, @RequestHeader("X-User-Id") String userId){
		commentService.delete(commentId, userId);
		return ResponseEntity.noContent().build();
	}
}
