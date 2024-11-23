package com.nninjoon.userservice.client.model;

public record CommentResponse(
	String userId,
	String content,
	String createdAt
) {}

