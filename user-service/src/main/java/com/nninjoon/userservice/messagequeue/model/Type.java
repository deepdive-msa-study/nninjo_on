package com.nninjoon.userservice.messagequeue.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Type {
	USER_DELETED("탈퇴"),
	USER_UPDATED("수정");

	private final String description;
}
