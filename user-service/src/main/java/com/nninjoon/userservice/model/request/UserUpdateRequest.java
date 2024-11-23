package com.nninjoon.userservice.model.request;

import lombok.Builder;

@Builder
public record UserUpdateRequest(
	String name,
	String email
) {}
