package com.nninjoon.userservice.messagequeue.model;

import lombok.Builder;

@Builder
public record Payload(
	String user_id,
	String email,
	String name
) {
	public static Payload of(String userId, String email, String name) {
		return Payload.builder()
			.user_id(userId)
			.email(email)
			.name(name)
			.build();
	}
}
