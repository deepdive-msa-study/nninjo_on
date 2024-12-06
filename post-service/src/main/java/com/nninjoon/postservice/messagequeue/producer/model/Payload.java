package com.nninjoon.postservice.messagequeue.producer.model;

import lombok.Builder;

@Builder
public record Payload(
	Long post_id
) {
	public static Payload of(Long post_id) {
		return Payload.builder()
			.post_id(post_id)
			.build();
	}
}
