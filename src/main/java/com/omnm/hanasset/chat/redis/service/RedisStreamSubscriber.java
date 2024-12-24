package com.omnm.hanasset.chat.redis.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.omnm.hanasset.chat.dto.ChatMessageDTO;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.redis.connection.stream.*;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Log4j2
@Service
public class RedisStreamSubscriber {

    private final RedisTemplate<String, Object> redisStreamTemplate;
    private final ObjectMapper objectMapper;

    public RedisStreamSubscriber(RedisTemplate<String, Object> redisStreamTemplate, ObjectMapper objectMapper) {
        this.redisStreamTemplate = redisStreamTemplate;
        this.objectMapper = objectMapper;
    }

    public List<ChatMessageDTO> consumeMessages(String chatroomId) {
        String streamKey = "stream_" + chatroomId; // 스트림 키
        String groupName = "group_" + chatroomId; // 그룹 이름
        String consumerName = "consumer_" + UUID.randomUUID(); // 랜덤한 소비자 이름 생성

        List<ChatMessageDTO> messageList = new ArrayList<>();

        try {
            // 1. 컨슈머 그룹 생성 (이미 존재하면 예외를 무시)
            try {
                redisStreamTemplate.opsForStream().createGroup(streamKey, ReadOffset.latest(), groupName);
                log.info("Consumer Group created: {}", groupName);
            } catch (Exception e) {
                log.warn("Consumer Group already exists: {}", groupName);
            }

            // 2. Redis 스트림에서 메시지 읽기
            List<MapRecord<String, Object, Object>> messages = redisStreamTemplate.opsForStream()
                    .read(Consumer.from(groupName, consumerName),
                            StreamReadOptions.empty().block(Duration.ofSeconds(2)),
                            StreamOffset.create(streamKey, ReadOffset.lastConsumed()));

            // 3. 메시지 처리
            if (messages != null && !messages.isEmpty()) {
                for (MapRecord<String, Object, Object> message : messages) {
                    try {
                        // 메시지 값을 JSON 문자열로 변환 (MapRecord에서 가져온 값은 Object)
                        Map<Object, Object> rawData = message.getValue(); // Map으로 가져오기
                        for (Object key : rawData.keySet()) {
                            String json = rawData.get(key).toString(); // JSON 문자열로 변환
                            ChatMessageDTO chatMessage = objectMapper.readValue(json, ChatMessageDTO.class); // JSON -> DTO로 변환
//                            log.info("ChatMessage JSON: {}", json);
//                            log.info("ChatMessage DTO: {}", chatMessage);
                            // 메시지 리스트에 추가
                            messageList.add(chatMessage);// 변환된 DTO 출력
                        }

                        // 메시지 처리 후 ACK
                        redisStreamTemplate.opsForStream().acknowledge(streamKey, groupName, message.getId());
                    } catch (Exception e) {
                        log.error("Error processing message from stream '{}': {}", streamKey, e.getMessage(), e);
                    }
                }
                log.info("Consumed {} messages from stream '{}'", messages.size(), streamKey);
            } else {
                log.info("No messages found in stream '{}'", streamKey);
            }
        } catch (Exception e) {
            // 메시지 소비 중 예외 처리
            log.error("Error consuming messages from stream '{}': {}", streamKey, e.getMessage(), e);
        }

        return messageList;
    }

    /**
     * Stream 방식으로 Redis 메시지 읽기 (ACK 없이 데이터 유지)
     */
    public List<ChatMessageDTO> reLoadMessages(String chatroomId) {
        String streamKey = "stream_" + chatroomId; // Stream 키
        List<ChatMessageDTO> messageList = new ArrayList<>();

        try {
            // Redis Stream에서 모든 데이터를 읽음
            List<MapRecord<String, Object, Object>> messages = redisStreamTemplate.opsForStream()
                    .read(StreamReadOptions.empty(), StreamOffset.fromStart(streamKey));

            if (messages != null && !messages.isEmpty()) {
                for (MapRecord<String, Object, Object> message : messages) {
                    try {
                        Map<Object, Object> rawData = message.getValue(); // Map으로 데이터 가져오기
                        for (Object key : rawData.keySet()) {
                            String json = rawData.get(key).toString(); // JSON 문자열로 변환
                            ChatMessageDTO chatMessage = objectMapper.readValue(json, ChatMessageDTO.class);
                            messageList.add(chatMessage); // 메시지 리스트에 추가
                        }
                    } catch (Exception e) {
                        log.error("Error processing message from stream '{}': {}", streamKey, e.getMessage(), e);
                    }
                }
                log.info("Read {} messages from stream '{}'", messages.size(), streamKey);
            } else {
                log.info("No messages found in stream '{}'", streamKey);
            }
        } catch (Exception e) {
            log.error("Error reading messages from stream '{}': {}", streamKey, e.getMessage(), e);
        }

        return messageList;
    }
}