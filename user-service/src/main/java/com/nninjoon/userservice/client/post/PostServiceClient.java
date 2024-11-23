package com.nninjoon.userservice.client.post;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.nninjoon.userservice.client.post.model.PostResponse;

@FeignClient("post-service")
public interface PostServiceClient {
	@GetMapping("/internal/posts/{userId}")
	List<PostResponse> getPosts(@PathVariable String userId);
}
