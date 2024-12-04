package com.nninjoon.postservice.messagequeue;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nninjoon.postservice.entity.Post;
import com.nninjoon.postservice.repository.PostRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserConsumer {
	private final PostRepository postRepository;

	@Transactional
	@KafkaListener(topics = "user-events-topic", groupId = "post-service-group")
	public void consume(String message) {
		log.info("Kafka Message: -> " + message);

		Map<Object, Object> map = parseKafkaMessage(message);
		validateKafkaMessage(map);

		postRepository.anonymizePostsByUserId(map.get("userId").toString());
	}

	private Map<Object, Object> parseKafkaMessage(String kafkaMessage) {
		try {
			return new ObjectMapper().readValue(kafkaMessage, new TypeReference<HashMap<Object, Object>>() {});
		} catch (JsonProcessingException e) {
			log.error("Failed to parse Kafka message: {}", kafkaMessage, e);
			throw new RuntimeException("Invalid Kafka message format", e);
		}
	}

	private void validateKafkaMessage(Map<Object, Object> map) {
		if (!map.containsKey("userId") || !map.containsKey("name")) {
			throw new IllegalArgumentException("Missing required fields in Kafka message: " + map);
		}
		if (!(map.get("userId") instanceof String) || !(map.get("name") instanceof String)) {
			throw new IllegalArgumentException("Invalid field types in Kafka message: " + map);
		}
	}

}
