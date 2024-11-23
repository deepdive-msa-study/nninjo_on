package com.nninjoon.userservice.client.model.post;

import lombok.Builder;

@Builder
public record PostResponse(
	Long id,
	String title,
	String content,
	String userId
) {
}
