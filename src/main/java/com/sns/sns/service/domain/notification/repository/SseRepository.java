package com.sns.sns.service.domain.notification.repository;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Repository
@RequiredArgsConstructor
public class SseRepository {

    private Map<String, SseEmitter> sseRepository = new HashMap<>();

    public SseEmitter saveEmitter(Long userId, SseEmitter sseEmitter){
        final String key = getKey(userId);
        sseRepository.put(key, sseEmitter);
        log.info("set emitter");
        return sseEmitter;
    }

    public Optional<SseEmitter> get(Long userId){
        log.info("get emitter");
        return Optional.ofNullable(sseRepository.get(getKey(userId)));
    }
    private String getKey(Long userId){
        log.info("emitter key name is userId::"+userId);
        return "userId::" + userId;

    }
    public void delete(Long userId){
        sseRepository.remove(getKey(userId));
    }

}
