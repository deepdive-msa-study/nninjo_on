package com.nninjoon.postservice.dto;

import java.util.List;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
@Builder
public class PostDetailResponseDto {
    private Long id;
    private String title;
    private String content;
    private String author;
    private String createdAt;
    private List<String> hashtags;
    private List<CommentResponseDto> comments;
}
