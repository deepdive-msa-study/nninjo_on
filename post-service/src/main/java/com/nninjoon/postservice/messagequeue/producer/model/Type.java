package com.nninjoon.postservice.messagequeue.producer.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Type {
	POST_DELETED("삭제");

	private final String description;
}
