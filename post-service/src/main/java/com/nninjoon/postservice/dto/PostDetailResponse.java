package com.nninjoon.postservice.dto;

import java.util.List;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.nninjoon.postservice.client.model.CommentResponse;
import com.nninjoon.postservice.entity.Post;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
public record PostDetailResponse(
    String title,
    String content,
    String author,
    String createdAt,
    List<CommentResponse> comments
) {
    public static PostDetailResponse of(String title, String content, String author, String createdAt, List<CommentResponse> comments) {
        return PostDetailResponse.builder()
            .title(title)
            .content(content)
            .author(author)
            .createdAt(createdAt)
            .comments(comments)
            .build();
    }
}
