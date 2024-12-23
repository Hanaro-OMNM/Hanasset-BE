package com.omnm.hanasset.chat.redis.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import com.omnm.hanasset.chat.dto.ChatMessageDTO;

import java.util.Collections;

@Log4j2
@Service
public class RedisStreamPublisher {

    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;

    public RedisStreamPublisher(RedisTemplate<String, Object> redisTemplate, ObjectMapper objectMapper) {
        this.redisTemplate = redisTemplate;
        this.objectMapper = objectMapper;
    }

    public void publishMessage(String chatroomId, ChatMessageDTO message) {
        String streamKey = "stream_" + chatroomId; // 채팅방 ID 기반 Stream Key

        try {
            // ChatMessageDTO를 JSON 문자열로 변환
            String json = objectMapper.writeValueAsString(message);

            // Redis Stream에 JSON 메시지 추가
            redisTemplate.opsForStream().add(streamKey, Collections.singletonMap("chatMessage", json));

            // 로그 기록
            log.info("Message published to stream '{}': {}", streamKey, json);
        } catch (Exception e) {
            // Redis 메시지 추가 실패 처리
            log.error("Error while publishing message to stream '{}': {}", streamKey, e.getMessage(), e);
        }
    }



}
