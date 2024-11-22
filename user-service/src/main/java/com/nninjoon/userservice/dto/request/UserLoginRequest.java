package com.nninjoon.userservice.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record UserLoginRequest(
	@NotBlank
	String email,

	@NotBlank
	String password
) {}
