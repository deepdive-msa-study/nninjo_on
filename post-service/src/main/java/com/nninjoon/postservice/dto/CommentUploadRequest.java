package com.nninjoon.postservice.dto;

import lombok.Builder;

@Builder
public record CommentUploadRequest(
    String content
) {}
