package com.nninjoon.postservice.client;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.nninjoon.postservice.client.model.CommentResponse;

@FeignClient("comment-service")
public interface CommentServiceClient {
	@GetMapping("/internal/comments/posts/{postId}")
	List<CommentResponse> getComments(@PathVariable Long postId);
}
