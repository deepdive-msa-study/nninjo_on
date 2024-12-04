package com.nninjoon.userservice.messagequeue;

import java.util.Arrays;
import java.util.List;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nninjoon.userservice.messagequeue.model.Field;
import com.nninjoon.userservice.messagequeue.model.KafkaUserDto;
import com.nninjoon.userservice.messagequeue.model.Payload;
import com.nninjoon.userservice.messagequeue.model.Schema;
import com.nninjoon.userservice.model.response.UserResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserProducer {
	private static final String TOPIC = "user-deleted-topic";
	private final KafkaTemplate<String, String> kafkaTemplate;

	List<Field> fields = Arrays.asList(new Field("string", true, "order_id"),
		Field.of("string", true, "user_id"),
		Field.of("string", true, "product_id"),
		Field.of("int32", true, "qty"),
		Field.of("int32", true, "unit_price"),
		Field.of("int32", true, "total_price"));

	Schema schema = Schema.of("struct", fields, false, "users");

	public void sendMessage(String topic, UserResponse response) {
		Payload payload = Payload.of(response.userId(), response.email(), response.name());
		KafkaUserDto kafkaUserDto = KafkaUserDto.of(schema, payload);

		ObjectMapper objectMapper = new ObjectMapper();

		String message = "";

		try {
			message = objectMapper.writeValueAsString(kafkaUserDto);
		} catch (JsonProcessingException e) {
			log.error(e.getMessage());
		}

		kafkaTemplate.send(topic, message);
		log.info("User Producer sent data from the User microservice: " + kafkaUserDto);

	}
}
