package com.nninjoon.userservice.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record UserCreateRequest(
	@NotBlank
	String name,

	@Email
	@NotBlank
	String email,

	@NotBlank
	String password
) {}
