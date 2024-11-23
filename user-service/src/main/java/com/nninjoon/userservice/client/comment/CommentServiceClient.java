package com.nninjoon.userservice.client.comment;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.nninjoon.userservice.client.comment.model.CommentResponse;

@FeignClient("comment-service")
public interface CommentServiceClient {
	@GetMapping("/internal/comments/users/{userId}")
	List<CommentResponse> getComments(@PathVariable String userId);
}
