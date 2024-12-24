package com.omnm.hanasset.chat.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.omnm.hanasset.chat.dto.ChatMessageDTO;
import com.omnm.hanasset.chat.dto.ChatMessageResponse;
import com.omnm.hanasset.chat.dto.ChatroomResponse;
import com.omnm.hanasset.chat.entity.ChatMessage;
import com.omnm.hanasset.chat.entity.ChatRoom;
import com.omnm.hanasset.chat.redis.service.RedisStreamSubscriber;
import com.omnm.hanasset.chat.repository.ChatMessageRepository;
import com.omnm.hanasset.chat.repository.ChatRoomRepository;
import com.omnm.hanasset.chat.utils.ChatMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
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
@Log4j2
@Service
public class ChatRoomService {

    @Autowired
    private ChatMapper chatMapper;  // ChatMapper 주입

    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final RedisStreamSubscriber redisStreamSubscriber; // Redis Stream 구독
    private final RedisTemplate<String, Object> redisStreamTemplate; // 수정된 RedisTemplate
    private final ObjectMapper objectMapper;
    private static final String CHATROOM_KEY_PREFIX = "chatroom:";

    public ChatroomResponse findAll() {
        List<ChatRoom> allChatrooms = chatRoomRepository.findAll();

        List<ChatRoomDTO> chatRoomDTOList = allChatrooms.stream()
                .map(chatMapper::toChatRoomDTO)
                .collect(Collectors.toList());

        return new ChatroomResponse(chatRoomDTOList.size(), chatRoomDTOList);
    }




    public ChatroomResponse createRoom(Long userId, Long consultantId, String chatroomTitle, LocalDateTime reservedTime) {

        ChatRoom chatRoomEntity = ChatRoom.builder()
                .chatroomId(UUID.randomUUID().toString())
                .userId(userId)
                .consultantId(consultantId)
                .chatroomTitle(chatroomTitle)
                .chatroomStatus("waiting")
                .createdAt(LocalDateTime.now())
                .reservedTime(reservedTime)
                .build();

        log.info("Saving ChatRoom Entity: {}", chatRoomEntity);
        ChatRoom savedChatRoom = chatRoomRepository.save(chatRoomEntity);

        log.info("Saved ChatRoom Entity: {}", savedChatRoom);

        ChatRoomDTO chatRoomDTO = chatMapper.toChatRoomDTO(savedChatRoom);
        String streamKey = "stream_" + chatRoomDTO.getChatroomId();
        String groupName = "group_" + chatRoomDTO.getChatroomId();

        try {
            String json = objectMapper.writeValueAsString(chatRoomDTO);
            redisStreamTemplate.opsForStream().add(streamKey, Collections.singletonMap("chatRoom", json));
            createConsumerGroup(streamKey, groupName);
            log.info("Chat room information published to stream '{}': {}", streamKey, json);
        } catch (Exception e) {
            log.error("Error while publishing chat room information to stream '{}': {}", streamKey, e.getMessage(), e);
        }

        // Waiting room 처리
        if (reservedTime.toLocalDate().equals(LocalDate.now())) {
            String redisKey = "consultant:" + consultantId + ":waiting_rooms";
            try {
                String json = objectMapper.writeValueAsString(chatRoomDTO);
                redisStreamTemplate.opsForStream().add(redisKey, Collections.singletonMap("chatRoom", json));
            } catch (JsonProcessingException e) {
                log.error("Error syncing new room to Redis: {}", e.getMessage());
            }
        }

        return new ChatroomResponse(1, Collections.singletonList(chatRoomDTO));
    }

    public void deleteRoom(String chatroomId) {
        // Redis Stream 키
        String streamKey = "stream_" + chatroomId;

        // 1. Redis에서 Stream 데이터 삭제
        try {
            Boolean isDeleted = redisStreamTemplate.delete(streamKey); // 키 삭제
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
            // redisStreamTemplate을 사용하여 Consumer Group 생성
            redisStreamTemplate.opsForStream().createGroup(streamKey, ReadOffset.latest(), groupName);
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
            redisStreamTemplate.opsForStream().add(streamKey, Collections.singletonMap("chatRoom", json));

            // Set expiration (12 hours = 43200 seconds)
            redisStreamTemplate.expire(streamKey, 12, TimeUnit.HOURS);

            log.info("Added waiting room to stream '{}': {}", streamKey, json);
        } catch (JsonProcessingException e) {
            log.error("Error serializing ChatRoomDTO: {}", e.getMessage(), e);
        }
    }

    // 대기방 조회
    public ChatroomResponse getWaitingRooms(Long consultantId) {
        String streamKey = "consultant:" + consultantId + ":waiting_rooms";
        List<ChatRoomDTO> chatRooms = new ArrayList<>();

        try {
            List<MapRecord<String, Object, Object>> messages = redisStreamTemplate.opsForStream()
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

        if (chatRooms.isEmpty()) {
            LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
            LocalDateTime endOfDay = LocalDate.now().atTime(LocalTime.MAX);
            List<ChatRoom> waitingRooms = chatRoomRepository.findWaitingRoomsByConsultantIdAndReservedDate(consultantId, startOfDay, endOfDay);
            chatRooms = waitingRooms.stream()
                    .map(chatMapper::toChatRoomDTO)
                    .collect(Collectors.toList());
            syncWaitingRoomsToRedis(streamKey, chatRooms);
        }

        return new ChatroomResponse(chatRooms.size(), chatRooms);
    }

    private void syncWaitingRoomsToRedis(String streamKey, List<ChatRoomDTO> chatRooms) {
        for (ChatRoomDTO chatRoomDTO : chatRooms) {
            try {
                String json = objectMapper.writeValueAsString(chatRoomDTO);
                redisStreamTemplate.opsForStream().add(streamKey, Collections.singletonMap("chatRoom", json));
            } catch (Exception e) {
                log.error("Error syncing waiting room to Redis: {}", e.getMessage());
            }
        }
    }

    private void removeRoomFromRedis(String chatroomId, Long consultantId) {
        String streamKey = "consultant:" + consultantId + ":waiting_rooms";
        try {
            // Redis Stream에서 해당 chatroomId와 일치하는 메시지 삭제
            List<MapRecord<String, Object, Object>> messages = redisStreamTemplate.opsForStream()
                    .read(StreamReadOptions.empty(), StreamOffset.fromStart(streamKey));

            for (MapRecord<String, Object, Object> message : messages) {
                Map<Object, Object> rawData = message.getValue();
                if (rawData.containsKey("chatRoom")) {
                    String json = rawData.get("chatRoom").toString();
                    ChatRoomDTO chatRoomDTO = objectMapper.readValue(json, ChatRoomDTO.class);

                    // chatroomId가 일치하는 메시지 삭제
                    if (chatRoomDTO.getChatroomId().equals(chatroomId)) {
                        redisStreamTemplate.opsForStream().delete(streamKey, message.getId());
                        log.info("Removed room [{}] from Redis waiting rooms.", chatroomId);
                        break;
                    }
                }
            }
        } catch (Exception e) {
            log.error("Error removing room [{}] from Redis: {}", chatroomId, e.getMessage());
        }
    }

    public ChatroomResponse updateChatRoomStatus(String chatroomId, String currentState, String newState) {
        int rowsUpdated = chatRoomRepository.updateStatusByChatroomId(chatroomId, currentState, newState);
        if (rowsUpdated == 0) {
            throw new RuntimeException("No ChatRoom found : " + chatroomId + " with status: " + currentState);
        }

        ChatRoom updatedChatRoom = chatRoomRepository.findByChatroomId(chatroomId)
                .orElseThrow(() -> new RuntimeException("Failed to fetch updated ChatroomId for userId: " + chatroomId));

        log.info("ChatRoom [{}] status updated to '{}'.", updatedChatRoom.getChatroomId(), newState);

        if ("waiting".equals(currentState) && "active".equals(newState)) {
            try {
                removeRoomFromRedis(updatedChatRoom.getChatroomId(), updatedChatRoom.getConsultantId());
            } catch (Exception e) {
                log.error("Error removing room [{}] from Redis: {}", updatedChatRoom.getChatroomId(), e.getMessage());
            }
        }

        // ChatRoomDTO로 변환 후 ChatroomResponse에 포함
        ChatRoomDTO chatRoomDTO = chatMapper.toChatRoomDTO(updatedChatRoom);
        return new ChatroomResponse(1, Collections.singletonList(chatRoomDTO));


    }public ChatroomResponse updateActiveToCompleted(String chatroomId, String currentState, String newState) {
        if (!"active".equals(currentState) || !"completed".equals(newState)) {
            throw new IllegalArgumentException("Invalid state transition: " + currentState + " to " + newState);
        }

        int rowsUpdated = chatRoomRepository.updateStatusAndFinishedAt(chatroomId, currentState, newState);
        if (rowsUpdated == 0) {
            throw new RuntimeException("No ChatRoom found with ID: " + chatroomId + " and status: " + currentState);
        }

        // Fetch updated ChatRoom
        ChatRoom updatedChatRoom = chatRoomRepository.findByChatroomId(chatroomId)
                .orElseThrow(() -> new RuntimeException("Failed to fetch updated ChatroomId for userId: " + chatroomId));

        log.info("ChatRoom [{}] status updated from 'active' to 'completed'. FinishedAt set to current time.", chatroomId);

        // Convert to DTO and return response
        ChatRoomDTO chatRoomDTO = chatMapper.toChatRoomDTO(updatedChatRoom);
        return new ChatroomResponse(1, Collections.singletonList(chatRoomDTO));
    }




    public ChatRoomDTO findRoom(Long userId, String chatroomStatus) {
        String redisKey = CHATROOM_KEY_PREFIX + userId + ":" + chatroomStatus;

        // 1. Redis에서 ChatRoomDTO 조회
        try {
            Object cachedValue = redisStreamTemplate.opsForValue().get(redisKey);
            if (cachedValue != null) {
                log.info("Room details found in Redis: {}", cachedValue);
                return objectMapper.readValue(cachedValue.toString(), ChatRoomDTO.class);
            }
        } catch (Exception e) {
            log.error("Error while fetching Room details from Redis: {}", e.getMessage());
        }

        // 2. Redis에 없을 경우 DB 조회
        ChatRoom chatRoom = chatRoomRepository.findRoomIdByUserIdAndStatus(userId, chatroomStatus)
                .map(roomId -> chatRoomRepository.findByChatroomId(roomId)
                        .orElseThrow(() -> new RuntimeException("Chat room not found in DB: " + roomId)))
                .orElseThrow(() -> new RuntimeException("No room found for userId: " + userId + " with status: " + chatroomStatus));

        // 3. ChatRoomDTO 변환 및 Redis에 저장
        ChatRoomDTO chatRoomDTO = chatMapper.toChatRoomDTO(chatRoom);
        try {

            String json = objectMapper.writeValueAsString(chatRoomDTO);
            redisStreamTemplate.opsForValue().set(redisKey, json);
            log.info("Room details saved to Redis: {}", redisKey);

        } catch (Exception e) {
            log.error("Error while saving Room details to Redis: {}", e.getMessage());
        }

        return chatRoomDTO;
    }

    public ChatroomResponse getCompletedChatroomsByUserId(Long userId) {
        List<ChatRoom> completedChatrooms = chatRoomRepository.findCompletedChatroomsByUserId(userId);

        List<ChatRoomDTO> chatRoomDTOList = completedChatrooms.stream()
                .map(chatMapper::toChatRoomDTO)
                .collect(Collectors.toList());

        return new ChatroomResponse(chatRoomDTOList.size(), chatRoomDTOList);
    }


    public ChatMessageResponse getMessagesByChatroomId(String chatroomId) {
        List<ChatMessage> chatMessages = chatMessageRepository.findMessagesByChatroomId(chatroomId);
        List<ChatMessageDTO> chatMessageDTOList = chatMessages.stream()
                .map(chatMapper::toChatMessageDTO)
                .collect(Collectors.toList());
        return new ChatMessageResponse(chatMessageDTOList.size(), chatMessageDTOList);
    }



}