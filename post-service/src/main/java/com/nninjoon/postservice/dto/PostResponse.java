package com.nninjoon.postservice.dto;

import com.nninjoon.postservice.entity.Post;

import lombok.Builder;

@Builder
public record PostResponse(
    Long id,
    String title,
    String content,
    String userId
) {
   public static PostResponse from(Post post) {
       return PostResponse.builder()
           .id(post.getId())
           .title(post.getTitle())
           .content(post.getContent())
           .userId(post.getUserId())
           .build();
   }
}
