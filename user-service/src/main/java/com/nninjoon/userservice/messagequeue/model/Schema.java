package com.nninjoon.userservice.messagequeue.model;

import java.util.List;

import lombok.Builder;

@Builder
public record Schema(
	Type type,
	List<Field> fields,
	boolean optional,
	String name
) {
	public static Schema of(Type type, List<Field> fields, boolean optional, String name) {
		return Schema.builder()
			.type(type)
			.fields(fields)
			.optional(optional)
			.name(name)
			.build();
	}
}
