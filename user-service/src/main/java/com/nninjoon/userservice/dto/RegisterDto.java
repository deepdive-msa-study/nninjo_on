package com.nninjoon.userservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RegisterDto {
	private String name;
	private String email;
	private String password;
}
