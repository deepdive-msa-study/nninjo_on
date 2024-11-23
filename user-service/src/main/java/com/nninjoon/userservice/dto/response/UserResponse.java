package com.nninjoon.userservice.dto.response;


import java.util.List;

import com.nninjoon.userservice.domain.User;
import com.nninjoon.userservice.dto.post.PostResponse;

import lombok.Builder;

@Builder
public record UserResponse(
	String email,
	String name,

	List<PostResponse> posts
) {
	public static UserResponse from(User user) {
		return UserResponse.builder()
			.email(user.getEmail())
			.name(user.getName())
			.build();
	}

	public static UserResponse of(String email, String name, List<PostResponse> posts) {
		return UserResponse.builder()
			.email(email)
			.name(name)
			.posts(posts)
			.build();
	}
}