package com.nninjoon.common.commentservice.messagequeue;

import java.util.HashMap;
import java.util.Map;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nninjoon.common.commentservice.repository.CommentRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserConsumer {
	private final CommentRepository commentRepository;

	@Transactional
	@KafkaListener(topics = "user-events-topic", groupId = "comment-service-group")
	public void consume(String message) {
		log.info("Kafka Message: -> " + message);

		Map<Object, Object> map = parseKafkaMessage(message);
		Map<Object, Object> payload = validateKafkaMessage(map);

		commentRepository.anonymizePostsByUserId(payload.get("user_id").toString());
	}

	private Map<Object, Object> parseKafkaMessage(String kafkaMessage) {
		try {
			return new ObjectMapper().readValue(kafkaMessage, new TypeReference<HashMap<Object, Object>>() {});
		} catch (JsonProcessingException e) {
			log.error("Failed to parse Kafka message: {}", kafkaMessage, e);
			throw new RuntimeException("Invalid Kafka message format", e);
		}
	}

	private Map<Object, Object> validateKafkaMessage(Map<Object, Object> map) {
		if (!map.containsKey("payload")) {
			throw new IllegalArgumentException("Missing 'payload' field in Kafka message: " + map);
		}

		Map<Object, Object> payload = (Map<Object, Object>) map.get("payload");
		if (!payload.containsKey("user_id") || !payload.containsKey("name")) {
			throw new IllegalArgumentException("Missing required fields in payload: " + payload);
		}
		if (!(payload.get("user_id") instanceof String) || !(payload.get("name") instanceof String)) {
			throw new IllegalArgumentException("Invalid field types in payload: " + payload);
		}

		return payload;
	}
}