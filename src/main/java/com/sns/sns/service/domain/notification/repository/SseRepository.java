package com.sns.sns.service.domain.notification.repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
@RequiredArgsConstructor
public class SseRepository {
	private Map<String, SseEmitter> sseRepository = new HashMap<>();

	public void saveEmitter(String userLoginId, SseEmitter sseEmitter) {
		final String key = getKey(userLoginId);
		sseRepository.put(key, sseEmitter);
		log.info("set emitter");
	}

	public Optional<SseEmitter> get(String userLoginId) {
		log.info("get emitter");
		return Optional.ofNullable(sseRepository.get(getKey(userLoginId)));
	}

	private String getKey(String userLoginId) {
		log.info("emitter key name is userLoginId::" + userLoginId);
		return "userLoginId::" + userLoginId;
	}

	public void delete(String userLoginId) {
		sseRepository.remove(getKey(userLoginId));
	}

}
