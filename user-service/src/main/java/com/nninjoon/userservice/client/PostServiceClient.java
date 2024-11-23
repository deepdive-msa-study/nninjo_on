package com.nninjoon.userservice.client;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import com.nninjoon.userservice.dto.post.PostResponse;

@FeignClient("post-service")
public interface PostServiceClient {
	@GetMapping("/api/post/me")
	List<PostResponse> getPosts(@RequestHeader("X-User-Id") String userId);
}
