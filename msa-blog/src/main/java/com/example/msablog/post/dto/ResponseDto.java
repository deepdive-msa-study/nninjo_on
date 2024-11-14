package com.example.msablog.post.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Builder
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ResponseDto {
    private boolean success;
    private int code;
    private Object result;

    public static ResponseDto success() {
        return ResponseDto.builder()
                .success(true)
                .code(200)
                .result(null)
                .build();
    }

    public static ResponseDto success(Object data) {
        return ResponseDto.builder()
                .success(true)
                .code(200)
                .result(data)
                .build();
    }

    public static ResponseDto fail(int code, String message) {
        return ResponseDto.builder()
                .success(false)
                .code(code)
                .result(message)
                .build();
    }
}
