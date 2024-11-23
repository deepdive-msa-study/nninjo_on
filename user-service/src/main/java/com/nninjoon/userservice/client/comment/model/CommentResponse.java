package com.nninjoon.userservice.client.comment.model;

public record CommentResponse(
	String userId,
	String content,
	String createdAt
) {}

