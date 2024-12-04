package com.nninjoon.userservice.messagequeue.model;

import lombok.Builder;

@Builder
public record KafkaUserDto(
	Schema schema,
	Payload payload
) {
	public static KafkaUserDto of(Schema schema, Payload payload) {
		return KafkaUserDto.builder()
			.schema(schema)
			.payload(payload)
			.build();
	}
}
