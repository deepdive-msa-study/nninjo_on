package com.nninjoon.userservice.model.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record UserLoginRequest(
	@NotBlank
	String email,

	@NotBlank
	String password
) {}
