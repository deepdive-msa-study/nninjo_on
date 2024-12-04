package com.nninjoon.userservice.messagequeue.model;

import lombok.Builder;

@Builder
public record Field(
	String type,
	boolean optional,
	String field
) {
	public static Field of(String type, boolean optional, String field) {
		return Field.builder()
			.type(type)
			.field(field)
			.optional(optional)
			.build();
	}
}
