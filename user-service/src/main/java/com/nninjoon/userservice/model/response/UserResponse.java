package com.nninjoon.userservice.model.response;


import java.util.List;

import com.nninjoon.userservice.client.comment.model.CommentResponse;
import com.nninjoon.userservice.domain.User;
import com.nninjoon.userservice.client.post.model.PostResponse;

import lombok.Builder;

@Builder
public record UserResponse(
	String userId,
	String email,
	String name,

	List<PostResponse> posts,
	List<CommentResponse> comments
) {
	public static UserResponse from(User user) {
		return UserResponse.builder()
			.userId(user.getUserId())
			.email(user.getEmail())
			.name(user.getName())
			.build();
	}

	public static UserResponse of(String email, String name, List<PostResponse> posts, List<CommentResponse> comments) {
		return UserResponse.builder()
			.email(email)
			.name(name)
			.posts(posts)
			.comments(comments)
			.build();
	}
}