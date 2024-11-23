package com.nninjoon.userservice.client.post.model;

import lombok.Builder;

@Builder
public record PostResponse(
	Long id,
	String title,
	String content,
	String userId
) {
}
