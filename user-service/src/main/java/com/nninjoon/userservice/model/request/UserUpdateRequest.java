package com.nninjoon.userservice.dto.request;

import lombok.Builder;

@Builder
public record UserUpdateRequest(
	String name,
	String email
) {}
