package com.nninjoon.postservice.messagequeue.producer;

import java.util.Arrays;
import java.util.List;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nninjoon.postservice.messagequeue.producer.model.Field;
import com.nninjoon.postservice.messagequeue.producer.model.KafkaPostDto;
import com.nninjoon.postservice.messagequeue.producer.model.Payload;
import com.nninjoon.postservice.messagequeue.producer.model.Schema;
import com.nninjoon.postservice.messagequeue.producer.model.Type;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class PostProducer {
	private final KafkaTemplate<String, String> kafkaTemplate;

	List<Field> fields = List.of(new Field("string", true, "post_id"));


	public void sendMessage(Type type, Long postId) {
		log.info("Preparing to send Kafka message for post: {}", postId);
		Payload payload = Payload.of(postId);
		Schema schema = Schema.of(type, fields, false, "posts");

		KafkaPostDto kafkaPostDto = KafkaPostDto.of(schema, payload);

		ObjectMapper objectMapper = new ObjectMapper();

		String message = "";

		try {
			message = objectMapper.writeValueAsString(kafkaPostDto);
		} catch (JsonProcessingException e) {
			log.error(e.getMessage());
		}

		kafkaTemplate.send("post-events-topic", message);
		log.info("Post Producer sent data from the Post microservice: {}", kafkaPostDto);
	}
}
