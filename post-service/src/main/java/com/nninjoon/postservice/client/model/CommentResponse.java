package com.nninjoon.postservice.client.model;

public record CommentResponse(
	String userId,
	String content,
	String createdAt
) {}
