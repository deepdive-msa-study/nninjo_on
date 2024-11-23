package com.nninjoon.postservice.dto;

import com.nninjoon.postservice.entity.Post;

import lombok.Builder;

@Builder
public record PostPersistResponse(
	Long id
) {
	public static PostPersistResponse from(Post post) {
		return PostPersistResponse.builder()
			.id(post.getId())
			.build();
	}
}
