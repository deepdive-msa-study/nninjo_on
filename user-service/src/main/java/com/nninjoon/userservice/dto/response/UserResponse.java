package com.nninjoon.userservice.dto.response;


import com.nninjoon.userservice.domain.User;

import lombok.Builder;

@Builder
public record UserResponse(
	String email,
	String name
) {
	public static UserResponse from(User user) {
		return UserResponse.builder()
			.email(user.getEmail())
			.name(user.getName())
			.build();
	}
}