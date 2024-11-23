package com.nninjoon.common.commentservice.model.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record CommentRequest(
	@NotBlank
	String content
) {
}
