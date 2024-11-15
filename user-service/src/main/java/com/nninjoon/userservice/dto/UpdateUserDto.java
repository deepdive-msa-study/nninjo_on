package com.nninjoon.userservice.dto;

import lombok.Builder;

@Builder
public record UpdateUserDto(
	String name,
	String email
) {}
