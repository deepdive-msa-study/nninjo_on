package com.nninjoon.postservice.dto;

import java.time.LocalDateTime;

import com.nninjoon.postservice.entity.Comment;

import lombok.Builder;

@Builder
public record CommentResponse(
    String content,
    LocalDateTime createAt,
    String userId
) {
    public static CommentResponse from(Comment comment) {
        return CommentResponse.builder()
            .content(comment.getContent())
            .createAt(comment.getCreatedAt())
            .userId(comment.getUserId())
            .build();
    }
}
