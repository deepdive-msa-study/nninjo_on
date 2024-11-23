package com.nninjoon.common.commentservice.model.response;

import com.nninjoon.common.commentservice.domain.Comment;

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
