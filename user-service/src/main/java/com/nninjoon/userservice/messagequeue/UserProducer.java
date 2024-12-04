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
import com.nninjoon.userservice.messagequeue.model.Type;
import com.nninjoon.userservice.model.response.UserResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserProducer {
	private final KafkaTemplate<String, String> kafkaTemplate;

	List<Field> fields = Arrays.asList(new Field("string", true, "user_id"),
		Field.of("string", true, "email"),
		Field.of("string", true, "name"));


	public void sendMessage(Type type, UserResponse response) {
		Payload payload = Payload.of(response.userId(), response.email(), response.name());
		Schema schema = Schema.of(type, fields, false, "users");

		KafkaUserDto kafkaUserDto = KafkaUserDto.of(schema, payload);

		ObjectMapper objectMapper = new ObjectMapper();

		String message = "";

		try {
			message = objectMapper.writeValueAsString(kafkaUserDto);
		} catch (JsonProcessingException e) {
			log.error(e.getMessage());
		}

		kafkaTemplate.send("user-events-topic", message);
		log.info("User Producer sent data from the User microservice: " + kafkaUserDto);

	}
}
