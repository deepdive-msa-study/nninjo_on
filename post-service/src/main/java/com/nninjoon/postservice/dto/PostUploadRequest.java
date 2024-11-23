package com.nninjoon.postservice.dto;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record PostUploadRequest(
    @NotBlank
    String title,

    @NotBlank
    String content,

    @NotBlank
    List<String> hashtags
) {
}
