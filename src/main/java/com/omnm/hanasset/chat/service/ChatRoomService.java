package com.omnm.hanasset.chat.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.omnm.hanasset.chat.entity.ChatMessage;
import com.omnm.hanasset.chat.entity.ChatRoom;
import com.omnm.hanasset.chat.redis.service.RedisStreamSubscriber;
import com.omnm.hanasset.chat.repository.ChatMessageRepository;
import com.omnm.hanasset.chat.repository.ChatRoomRepository;
import com.omnm.hanasset.chat.utils.ChatMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.stream.MapRecord;
import org.springframework.data.redis.connection.stream.ReadOffset;
import org.springframework.data.redis.connection.stream.StreamOffset;
import org.springframework.data.redis.connection.stream.StreamReadOptions;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import com.omnm.hanasset.chat.dto.ChatRoomDTO;


import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Slf4j
@Service
public class ChatRoomService {

    @Autowired
    private ChatMapper chatMapper;  // ChatMapper 주입

    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final RedisStreamSubscriber redisStreamSubscriber; // Redis Stream 구독
    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;
    private static final String CHATROOM_KEY_PREFIX = "chatroom:";

    public List<ChatRoom> findAll() {
        return chatRoomRepository.findAll();
    }



    public ChatRoomDTO createRoom(Long userId, Long consultantId,String chatroomTitle, LocalDateTime reservedTime) {

        // DTO -> Entity 변환
        ChatRoom chatRoomEntity = ChatRoom.builder()
                .chatroomId(UUID.randomUUID().toString()) // UUID 수동 생성
                .userId(userId) // userId 추가
                .consultantId(consultantId) // consultantId 추가
                .chatroomTitle(chatroomTitle)
                .chatroomStatus("waiting")
                .createdAt(LocalDateTime.now())
                .reservedTime(reservedTime)
                .build();

        log.info("Saving ChatRoom Entity: {}", chatRoomEntity); // 저장 직전 로그 출력

        // 저장소에 Entity 저장
        ChatRoom savedChatRoom = chatRoomRepository.save(chatRoomEntity);

        log.info("Saved ChatRoom Entity: {}", savedChatRoom); // 저장 후 로그 출력

        // ChatRoom -> ChatRoomDTO 변환 (ChatMapper 사용)
        ChatRoomDTO chatRoomDTO = chatMapper.toChatRoomDTO(savedChatRoom);

        // 채팅방 ID 기반 Stream Key 생성
        String streamKey = "stream_" + chatRoomDTO.getChatroomId();
        String groupName = "group_" + chatRoomDTO.getChatroomId();
        String groupTestName = "group_test_" + chatRoomDTO.getChatroomId();


        //Stream 생성
        try {
            // ChatRoomDTO를 JSON 문자열로 변환
            String json = objectMapper.writeValueAsString(chatRoomDTO);

            // Redis Stream에 JSON 메시지 저장
            redisTemplate.opsForStream().add(streamKey, Collections.singletonMap("chatRoom", json));
            // 1. 소비자 그룹 생성 (없으면 생성)
            createConsumerGroup(streamKey, groupName);
            createConsumerGroup(streamKey, groupTestName);

            // 로그 기록
            log.info("Chat room information published to stream '{}': {}", streamKey, json);
        } catch (Exception e) {
            // Redis 메시지 추가 실패 처리
            log.error("Error while publishing chat room information to stream '{}': {}", streamKey, e.getMessage(), e);
        }

        // 오늘 날짜일 경우 waiting room에 추가
        if (reservedTime.toLocalDate().equals(LocalDate.now())) {
            String redisKey = "consultant:" + consultantId + ":waiting_rooms";
            try {
                String json = objectMapper.writeValueAsString(chatRoomDTO);
                redisTemplate.opsForStream().add(redisKey, Collections.singletonMap("chatRoom", json));
            } catch (JsonProcessingException e) {
                log.error("Error syncing new room to Redis: {}", e.getMessage());
            }
        }
        return chatRoomDTO;
    }

    public void deleteRoom(String chatroomId) {
        // Redis Stream 키
        String streamKey = "stream_" + chatroomId;

        // 1. Redis에서 Stream 데이터 삭제
        try {
            Boolean isDeleted = redisTemplate.delete(streamKey); // 키 삭제
            if (Boolean.TRUE.equals(isDeleted)) {
                log.info("Redis Stream [{}] deleted successfully.", streamKey);
            } else {
                log.warn("Redis Stream [{}] not found or could not be deleted.", streamKey);
            }
        } catch (Exception e) {
            log.error("Error deleting Redis Stream [{}]: {}", streamKey, e.getMessage(), e);
        }

        // 2. DB에서 ChatRoom 삭제
        try {
            if (chatRoomRepository.existsById(chatroomId)) { // 삭제 대상 존재 여부 확인
                chatRoomRepository.deleteById(chatroomId);
                log.info("ChatRoom [{}] deleted successfully from DB.", chatroomId);
            } else {
                log.warn("ChatRoom [{}] not found in DB.", chatroomId);
            }
        } catch (Exception e) {
            log.error("Error deleting ChatRoom [{}] from DB: {}", chatroomId, e.getMessage(), e);
        }
    }

    public void enterChatRoom(String chatroomId) {
        // 채팅방 ID를 기반으로 메시지 소비
        redisStreamSubscriber.consumeMessages(chatroomId);
        log.info("Consuming messages for chatroom: {}", chatroomId);
    }

    // Consumer Group 생성 (이미 존재하면 무시)
    private void createConsumerGroup(String streamKey, String groupName) {
        try {
            // RedisTemplate을 사용하여 Consumer Group 생성
            redisTemplate.opsForStream().createGroup(streamKey, ReadOffset.latest(), groupName);
            log.info("Consumer Group created: {}", groupName);
        } catch (Exception e) {
            log.warn("Consumer Group already exists: {}", groupName);
        }
    }

    /**
     * Redis Stream에 대기방 추가
     */
    public void addWaitingRoomToStream(Long consultantId) {
        String streamKey = "consultant:" + consultantId + ":waiting_rooms";
        String groupName = "waiting_groups:" + consultantId;

        LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
        LocalDateTime endOfDay = LocalDate.now().atTime(LocalTime.MAX);
        List<ChatRoom> waitingRooms = chatRoomRepository.findWaitingRoomsByConsultantIdAndReservedDate(
                consultantId, startOfDay, endOfDay);

        List<ChatRoomDTO> chatRoomDTOS = waitingRooms.stream()
                .map(chatMapper::toChatRoomDTO)  // ChatMapper 사용
                .collect(Collectors.toList());

        try {

            createConsumerGroup(streamKey, groupName);
            // Serialize the chatRoomDTO to JSON
            String json = objectMapper.writeValueAsString(chatRoomDTOS);

            // Add the message to the stream
            redisTemplate.opsForStream().add(streamKey, Collections.singletonMap("chatRoom", json));

            // Set expiration (12 hours = 43200 seconds)
            redisTemplate.expire(streamKey, 12, TimeUnit.HOURS);

            log.info("Added waiting room to stream '{}': {}", streamKey, json);
        } catch (JsonProcessingException e) {
            log.error("Error serializing ChatRoomDTO: {}", e.getMessage(), e);
        }
    }

    public List<ChatRoomDTO> getWaitingRooms(Long consultantId) {
        String streamKey = "consultant:" + consultantId + ":waiting_rooms";
        List<ChatRoomDTO> chatRooms = new ArrayList<>();

        // 1. Redis에서 대기방 데이터 조회
        try {
            List<MapRecord<String, Object, Object>> messages = redisTemplate.opsForStream()
                    .read(StreamReadOptions.empty().block(Duration.ofMillis(500)),
                            StreamOffset.create(streamKey, ReadOffset.from("0")));

            if (messages != null && !messages.isEmpty()) {
                for (MapRecord<String, Object, Object> message : messages) {
                    Map<Object, Object> rawData = message.getValue();
                    for (Object key : rawData.keySet()) {
                        String json = rawData.get(key).toString();
                        ChatRoomDTO chatRoomDTO = objectMapper.readValue(json, ChatRoomDTO.class);
                        chatRooms.add(chatRoomDTO);
                    }
                }
            }
        } catch (Exception e) {
            log.error("Error fetching waiting rooms from Redis: {}", e.getMessage());
        }

        // 2. Redis에 데이터가 없으면 MySQL에서 조회
        if (chatRooms.isEmpty()) {
            LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
            LocalDateTime endOfDay = LocalDate.now().atTime(LocalTime.MAX);
            List<ChatRoom> waitingRooms = chatRoomRepository.findWaitingRoomsByConsultantIdAndReservedDate(
                    consultantId, startOfDay, endOfDay);

            chatRooms = waitingRooms.stream()
                    .map(chatMapper::toChatRoomDTO)
                    .collect(Collectors.toList());

            // 3. Redis에 동기화
            syncWaitingRoomsToRedis(streamKey, chatRooms);
        }

        return chatRooms;
    }

    private void syncWaitingRoomsToRedis(String streamKey, List<ChatRoomDTO> chatRooms) {
        for (ChatRoomDTO chatRoomDTO : chatRooms) {
            try {
                String json = objectMapper.writeValueAsString(chatRoomDTO);
                redisTemplate.opsForStream().add(streamKey, Collections.singletonMap("chatRoom", json));
            } catch (Exception e) {
                log.error("Error syncing waiting room to Redis: {}", e.getMessage());
            }
        }
    }

    public void removeRoomFromRedis(String chatroomId, Long consultantId) {
        String streamKey = "consultant:" + consultantId + ":waiting_rooms";
        try {
            // Redis Stream에서 해당 chatroomId와 일치하는 메시지 삭제
            List<MapRecord<String, Object, Object>> messages = redisTemplate.opsForStream()
                    .read(StreamReadOptions.empty(), StreamOffset.fromStart(streamKey));

            for (MapRecord<String, Object, Object> message : messages) {
                Map<Object, Object> rawData = message.getValue();
                if (rawData.containsKey("chatRoom")) {
                    String json = rawData.get("chatRoom").toString();
                    ChatRoomDTO chatRoomDTO = objectMapper.readValue(json, ChatRoomDTO.class);

                    // chatroomId가 일치하는 메시지 삭제
                    if (chatRoomDTO.getChatroomId().equals(chatroomId)) {
                        redisTemplate.opsForStream().delete(streamKey, message.getId());
                        log.info("Removed room [{}] from Redis waiting rooms.", chatroomId);
                        break;
                    }
                }
            }
        } catch (Exception e) {
            log.error("Error removing room [{}] from Redis: {}", chatroomId, e.getMessage());
        }
    }

    public ChatRoomDTO updateChatRoomStatus(String chatroomId, String currentState, String newState) {
        // 상태 업데이트
        int rowsUpdated = chatRoomRepository.updateStatusByChatroomId(chatroomId, currentState, newState);
        if (rowsUpdated == 0) {
            throw new RuntimeException("No ChatRoom found : " + chatroomId + " with status: " + currentState);
        }

        // 업데이트된 채팅방 조회
        ChatRoom updatedChatRoom = chatRoomRepository.findByChatroomId(chatroomId)
                .orElseThrow(() -> new RuntimeException("Failed to fetch updated ChatroomId for userId: " + chatroomId));


        log.info("ChatRoom [{}] status updated to '{}'.", updatedChatRoom.getChatroomId(), newState);

        // 2. Redis에서 상태 변경 반영 (waiting -> active일 경우 삭제)
        if ("waiting".equals(currentState) && "active".equals(newState)) {
            try {
                removeRoomFromRedis(updatedChatRoom.getChatroomId(), updatedChatRoom.getConsultantId());
            } catch (Exception e) {
                log.error("Error removing room [{}] from Redis: {}",updatedChatRoom.getChatroomId(), e.getMessage());
            }
        }


        // ChatRoom -> ChatRoomDTO 변환 (ChatMapper 사용)
        ChatRoomDTO updatedChatRoomDTO = chatMapper.toChatRoomDTO(updatedChatRoom);

        // 변환된 DTO 반환
        return updatedChatRoomDTO;
    }


    public String findRoomId(Long userId, String chatroomStatus) {
        String redisKey = CHATROOM_KEY_PREFIX + userId + ":" + chatroomStatus;

        // 1. Redis에서 Room ID 조회
        try {
            Object cachedValue = redisTemplate.opsForValue().get(redisKey);
            if (cachedValue != null) {
                log.info("Room ID found in Redis: {}", cachedValue);
                return cachedValue.toString();
            }
        } catch (Exception e) {
            log.error("Error while fetching Room ID from Redis: {}", e.getMessage());
        }

        // 2. Redis에 없을 경우 DB 조회
        String roomId = chatRoomRepository.findRoomIdByUserIdAndStatus(userId, chatroomStatus)
                .orElseThrow(() -> new RuntimeException("No room found for userId: " + userId + " with status: " + chatroomStatus));

        // 3. Redis에 저장
        try {
            redisTemplate.opsForValue().set(redisKey, roomId);
            log.info("Room ID saved to Redis: {}", redisKey);
        } catch (Exception e) {
            log.error("Error while saving Room ID to Redis: {}", e.getMessage());
        }

        return roomId;
    }

    public List<ChatRoom> getCompletedChatroomsByUserId(Long userId) {
        return chatRoomRepository.findCompletedChatroomsByUserId(userId);
    }

    // 2. Find messages for a specific chatroom
    public List<ChatMessage> getMessagesByChatroomId(String chatroomId) {
        return chatMessageRepository.findMessagesByChatroomId(chatroomId);
    }



}