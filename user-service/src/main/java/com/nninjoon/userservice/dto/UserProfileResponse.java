package com.nninjoon.userservice.dto;


import com.nninjoon.userservice.domain.User;

import lombok.Builder;
import lombok.Getter;

@Builder
public record UserProfileResponse(
	String email,
	String name
) {
	public static UserProfileResponse from(User user) {
		return UserProfileResponse.builder()
			.email(user.getEmail())
			.name(user.getName())
			.build();
	}
}