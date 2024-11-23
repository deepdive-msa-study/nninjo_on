package com.nninjoon.common.commentservice.model;

import java.time.format.DateTimeFormatter;

import com.nninjoon.common.commentservice.domain.Comment;

import lombok.Builder;

@Builder
public record CommentResponse(
	String userId,
	String content,
	String createdAt
) {
	public static CommentResponse from(Comment comment) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		return CommentResponse.builder()
			.userId(comment.getUserId())
			.content(comment.getContent())
			.createdAt(comment.getCreatedAt().format(formatter))
			.build();
	}
}
