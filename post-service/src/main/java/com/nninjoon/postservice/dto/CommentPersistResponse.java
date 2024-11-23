package com.nninjoon.postservice.dto;

import com.nninjoon.postservice.entity.Comment;

import lombok.Builder;

@Builder
public record CommentPersistResponse(
	Long id
) {
	public static CommentPersistResponse from(Comment comment) {
		return CommentPersistResponse.builder()
			.id(comment.getId())
			.build();
	}
}
