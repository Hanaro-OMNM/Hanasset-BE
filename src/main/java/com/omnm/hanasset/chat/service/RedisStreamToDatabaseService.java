package com.omnm.hanasset.chat.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.omnm.hanasset.chat.dto.ChatMessageDTO;
import com.omnm.hanasset.chat.dto.ChatRoomDTO;
import com.omnm.hanasset.chat.entity.ChatMessage;
import com.omnm.hanasset.chat.repository.ChatMessageRepository;
import com.omnm.hanasset.chat.repository.ChatMessageRepositoryImpl;
import com.omnm.hanasset.chat.utils.ChatMapper;
import io.lettuce.core.RedisCommandInterruptedException;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.redis.connection.stream.*;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.ScanOptions;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.*;

@Log4j2
@RequiredArgsConstructor
@Service
public class RedisStreamToDatabaseService {

    private final StringRedisTemplate redisStreamTemplate;
    private final ObjectMapper objectMapper;
    private final ChatMessageRepository chatMessageRepository;
    private final ChatMessageRepositoryImpl chatMessageRepositoryImpl;

    private static final String STREAM_PREFIX = "stream_";
    private static final String GROUP_PREFIX = "group_";
    private static final String CONSUMER_PREFIX = "consumer_";
    private final ChatMapper chatMapper;

    // SCAN을 이용해 Redis 키를 비차단 방식으로 가져오는 메서드 추가
    private Set<String> scanKeys(String pattern) {
        Set<String> keys = new HashSet<>();
        RedisConnection connection = redisStreamTemplate.getConnectionFactory().getConnection();
        Cursor<byte[]> cursor = connection.scan(ScanOptions.scanOptions().match(pattern).count(100).build());

        try {
            while (cursor.hasNext()) {
                keys.add(new String(cursor.next(), StandardCharsets.UTF_8));
            }
        } catch (Exception e) {
            log.error("Error while scanning keys with pattern '{}': {}", pattern, e.getMessage());
        } finally {
            cursor.close();
        }
        return keys;
    }

    /**
     * Redis Stream에서 메시지 읽기 및 MySQL 저장
     */
    @Scheduled(fixedRate = 20000) // 10초마다 실행
    public void transferMessages() {
        Set<String> streamKeys = scanKeys(STREAM_PREFIX + "*"); // SCAN으로 키 검색

        if (streamKeys.isEmpty()) {
            log.info("No streams found to process.");
            return;
        }

        for (String streamKey : streamKeys) {
            String streamUUID = streamKey.replace(STREAM_PREFIX, ""); // stream_ 부분 제거하여 UUID만 추출
            String groupName = "group_test_" + streamUUID;  // 동적으로 생성된 그룹 이름
            String consumerName = "consumer_test_" + streamUUID;  // 동적으로 생성된 소비자 이름

            try {
                List<MapRecord<String, Object, Object>> records = redisStreamTemplate.opsForStream()
                        .read(Consumer.from(groupName, consumerName),
                                StreamReadOptions.empty().block(Duration.ofSeconds(2)),
                                StreamOffset.create(streamKey, ReadOffset.from(">")));

                if (records == null || records.isEmpty()) continue;

                // 레코드 리스트를 배치로 처리
                processRecords(streamKey, groupName, records);

            } catch (RedisCommandInterruptedException e) {
                log.warn("Redis command interrupted while reading stream '{}'. Server may be down: {}", streamKey, e.getMessage());
                Thread.currentThread().interrupt();
            } catch (Exception e) {
                log.error("Unexpected error while reading messages from stream '{}': {}", streamKey, e.getMessage(), e);
            }
        }
    }

    /**
     * Stream record를 처리하는 메서드
     */
    private void processRecords(String streamKey, String groupName, List<MapRecord<String, Object, Object>> records) throws JsonProcessingException {
        List<ChatMessage> chatMessages = new ArrayList<>();
        List<MapRecord<String, Object, Object>> failedRecords = new ArrayList<>(); // 실패한 레코드 저장

        for (MapRecord<String, Object, Object> record : records) {
            try {
                Map<Object, Object> rawData = record.getValue();
                log.info("Processing record: {}", record);

                for (Object key : rawData.keySet()) {
                    String json = rawData.get(key).toString(); // JSON 문자열로 가져오기
                    String jsonOnly = extractJsonContent(json); // 중괄호 부분만 추출

                    // DTO -> Entity 변환
                    ChatMessageDTO chatMessageDTO = objectMapper.readValue(jsonOnly, ChatMessageDTO.class);
                    log.info("ChatMessageDTO: {}", chatMessageDTO);

                    ChatMessage chatMessageEntity = chatMapper.toChatMessage(chatMessageDTO);
                    chatMessages.add(chatMessageEntity);
                }

                // Redis Stream에서 메시지 ack 처리
                redisStreamTemplate.opsForStream().acknowledge(streamKey, groupName, record.getId());
            } catch (JsonProcessingException e) {
                log.error("JSON processing error for record '{}': {}", record.getId(), e.getMessage());
                failedRecords.add(record); // 실패한 레코드 저장
            } catch (Exception e) {
                log.error("Error processing record from stream '{}': {}", streamKey, e.getMessage(), e);
                failedRecords.add(record); // 실패한 레코드 저장
            }
        }

        // 배치 저장
        if (!chatMessages.isEmpty()) {
            try {
                chatMessageRepositoryImpl.saveAllBulk(chatMessages); // 커스텀 벌크 저장 메서드 호출
                log.info("{} chat messages saved to database.", chatMessages.size());
            } catch (Exception e) {
                log.error("Error saving messages to database: {}", e.getMessage());
                failedRecords.addAll(records); // 저장 실패한 경우 전체를 다시 추가
            }
        }

//        // 실패한 레코드 재시도 로직
//        if (!failedRecords.isEmpty()) {
//            retryFailedRecords(failedRecords, streamKey, groupName);
//        }
    }

    /**
     * 실패한 레코드를 재시도하는 메서드
     */
    private void retryFailedRecords(List<MapRecord<String, Object, Object>> failedRecords, String streamKey, String groupName) {
        log.info("Retrying {} failed records...", failedRecords.size());

        for (MapRecord<String, Object, Object> record : failedRecords) {
            try {
                processRecords(streamKey, groupName, Collections.singletonList(record)); // 단일 레코드 재처리
            } catch (Exception e) {
                log.error("Error retrying record '{}': {}", record.getId(), e.getMessage());
            }
        }
    }

    /**
     * 서버 종료 대비 처리를 위한 Runtime 종료 후크 등록
     */
    @PostConstruct
    public void setupShutdownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            log.info("Shutdown detected. Cleaning up resources...");
            cleanUpResources();
        }));
    }

    /**
     * 서버 종료 시 자원 정리를 수행하는 메서드
     */
    @PreDestroy
    public void cleanUpResources() {
        try {
            if (redisStreamTemplate != null && redisStreamTemplate.getConnectionFactory() != null) {
                redisStreamTemplate.getConnectionFactory().getConnection().close();
            }
            log.info("Redis connection closed successfully.");
        } catch (Exception e) {
            log.error("Error during resource cleanup: {}", e.getMessage(), e);
        }
    }

    // 중괄호 {} 부분만 추출하는 함수
    private String extractJsonContent(String json) {
        int start = json.indexOf("{");
        int end = json.lastIndexOf("}");

        if (start != -1 && end != -1 && start < end) {
            return json.substring(start, end + 1);
        }
        return json;
    }

}
