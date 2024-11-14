package com.example.msablog.user.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class LoginDto {
    private String email;
    private String password;
}
