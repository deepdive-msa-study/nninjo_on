package com.nninjoon.postservice.messagequeue.producer.model;

import lombok.Builder;

@Builder
public record KafkaPostDto(
	Schema schema,
	Payload payload
) {
	public static KafkaPostDto of(Schema schema, Payload payload) {
		return KafkaPostDto.builder()
			.schema(schema)
			.payload(payload)
			.build();
	}
}
