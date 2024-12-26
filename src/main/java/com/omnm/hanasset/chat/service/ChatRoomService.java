package com.omnm.hanasset.chat.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.omnm.hanasset.bookmark.repository.BookmarkRealEstateRepository;
import com.omnm.hanasset.chat.dto.*;
import com.omnm.hanasset.chat.entity.ChatMessage;
import com.omnm.hanasset.chat.entity.ChatRoom;
import com.omnm.hanasset.chat.entity.ConsultingItem;
import com.omnm.hanasset.chat.redis.RedisStreamSubscriber;
import com.omnm.hanasset.chat.repository.ChatMessageRepository;
import com.omnm.hanasset.chat.repository.ChatRoomRepository;
import com.omnm.hanasset.chat.repository.ConsultingItemRepository;
import com.omnm.hanasset.chat.utils.ChatMapper;
import com.omnm.hanasset.consultant.entity.Consultant;
import com.omnm.hanasset.consultant.repository.ConsultantRepository;
import com.omnm.hanasset.global.exception.CustomException;
import com.omnm.hanasset.global.exception.code.ErrorCode;
import com.omnm.hanasset.user.entity.User;
import com.omnm.hanasset.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.stream.MapRecord;
import org.springframework.data.redis.connection.stream.ReadOffset;
import org.springframework.data.redis.connection.stream.StreamOffset;
import org.springframework.data.redis.connection.stream.StreamReadOptions;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;


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
    private final ConsultingItemRepository consultingItemRepository;
    private final UserRepository userRepository;
    private final ConsultantRepository consultantRepository;
    private final BookmarkRealEstateRepository bookmarkRealEstateRepository;

    public ChatroomResponse findAll() {
        List<ChatRoom> allChatrooms = chatRoomRepository.findAll();

        List<ChatRoomDTO> chatRoomDTOList = allChatrooms.stream()
                .map(chatMapper::toChatRoomDTO)
                .collect(Collectors.toList());

        return new ChatroomResponse(chatRoomDTOList.size(), chatRoomDTOList);
    }

    public ChatroomResponse createRoom(Long userId, Long consultantId, String chatroomTitle, LocalDateTime reservedTime,  List<ReservationInfoDTO> reservationInfo) {
        String reservationInfoJson = convertReservationInfoToJson(reservationInfo);

        // Find the User and Consultant entities by their IDs
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userId));

        Consultant consultant = consultantRepository.findById(consultantId)
                .orElseThrow(() -> new IllegalArgumentException("Consultant not found with ID: " + consultantId));

        // Build the ChatRoom entity
        ChatRoom chatRoomEntity = ChatRoom.builder()
                .chatroomId(UUID.randomUUID().toString())
                .user(user)
                .consultant(consultant)
                .chatroomTitle(chatroomTitle)
                .chatroomStatus("waiting")
                .createdAt(LocalDateTime.now())
                .reservedTime(reservedTime)
                .reservationInfo(reservationInfoJson)
                .build();

        ChatRoom savedChatRoom = chatRoomRepository.save(chatRoomEntity);

        if (reservationInfo != null && !reservationInfo.isEmpty()) {
            for (ReservationInfoDTO item : reservationInfo) {
                Long realEstateId = item.getRealEstateId();
                Long housingComplexId = item.getHousingComplexId();

                log.info("Saving ConsultingItem: realEstateId={}, housingComplexId={}", realEstateId, housingComplexId);

                ConsultingItem consultingItem = ConsultingItem.builder()
                        .chatroom(savedChatRoom) // ChatRoom 객체를 직접 설정
                        .realEstate(bookmarkRealEstateRepository.findById(realEstateId)
                                .orElseThrow(() -> new IllegalArgumentException("RealEstate not found with ID: " + realEstateId)))
                        .build();
                consultingItemRepository.save(consultingItem);
            }
        }

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

        enterChatRoom(chatRoomDTO.getChatroomId());
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

    private String convertReservationInfoToJson(List<ReservationInfoDTO> reservationInfo) {
        try {
            return objectMapper.writeValueAsString(reservationInfo);
        } catch (Exception e) {
            e.printStackTrace();
            return "[]";
        }
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

    // Redis Stream에 대기방 추가
    public void addWaitingRoomToStream(Long consultantId) {
        String streamKey = "consultant:" + consultantId + ":waiting_rooms";
        String groupName = "waiting_groups:" + consultantId;

        LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
        LocalDateTime endOfDay = LocalDate.now().atTime(LocalTime.MAX);
        List<ChatRoom> waitingRooms = chatRoomRepository.findWaitingRoomsByConsultantIdAndReservedDate(
                consultantId, startOfDay, endOfDay);

        List<WaitingRoomDTO> waitingRoomDTOS = waitingRooms.stream()
                .map(chatRoom -> {
                    // Fetch user information
                    User user = userRepository.findById(chatRoom.getUser().getUserId())
                            .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

                    // Build WaitingRoomDTO
                    return WaitingRoomDTO.builder()
                            .userName(user.getName())
                            .chatroom(chatMapper.toChatRoomDTO(chatRoom))
                            .build();
                })
                .collect(Collectors.toList());

        createConsumerGroup(streamKey, groupName);
        syncWaitingRoomsToRedis(streamKey, waitingRoomDTOS);

        // Set expiration (12 hours = 43200 seconds)
        redisStreamTemplate.expire(streamKey, 12, TimeUnit.HOURS);
    }

    private void syncWaitingRoomsToRedis(String streamKey, List<WaitingRoomDTO> waitingRooms) {
        for (WaitingRoomDTO waitingRoomDTO : waitingRooms) {
            try {
                String json = objectMapper.writeValueAsString(waitingRoomDTO);
                redisStreamTemplate.opsForStream().add(streamKey, Collections.singletonMap("waitingRoom", json));
            } catch (Exception e) {
                log.error("Error syncing waiting room to Redis: {}", e.getMessage());
            }
        }
    }

    // 대기방 조회
    public WaitingRoomResponse getWaitingRooms(Long consultantId) {
        String streamKey = "consultant:" + consultantId + ":waiting_rooms";
        List<WaitingRoomDTO> waitingRooms = new ArrayList<>();

        try {
            List<MapRecord<String, Object, Object>> messages = redisStreamTemplate.opsForStream()
                    .read(StreamReadOptions.empty().block(Duration.ofMillis(500)),
                            StreamOffset.create(streamKey, ReadOffset.from("0")));
            if (messages != null && !messages.isEmpty()) {
                for (MapRecord<String, Object, Object> message : messages) {
                    Map<Object, Object> rawData = message.getValue();
                    for (Object key : rawData.keySet()) {
                        String json = rawData.get(key).toString();
                        WaitingRoomDTO waitingRoomDTO = objectMapper.readValue(json, WaitingRoomDTO.class);
                        waitingRooms.add(waitingRoomDTO);
                    }
                }
            }
        } catch (Exception e) {
            log.error("Error fetching waiting rooms from Redis: {}", e.getMessage());
        }

        if (waitingRooms.isEmpty()) {
            LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
            LocalDateTime endOfDay = LocalDate.now().atTime(LocalTime.MAX);
            List<ChatRoom> foundWaitingRooms = chatRoomRepository.findWaitingRoomsByConsultantIdAndReservedDate(consultantId, startOfDay, endOfDay);
            waitingRooms = foundWaitingRooms.stream()
                    .map(chatRoom -> {
                        User user = userRepository.findById(chatRoom.getUser().getUserId())
                                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
                        return WaitingRoomDTO.builder()
                                .userName(user.getName())
                                .chatroom(chatMapper.toChatRoomDTO(chatRoom))
                                .build();
                    })
                    .collect(Collectors.toList());
            syncWaitingRoomsToRedis(streamKey, waitingRooms);
        }

        return new WaitingRoomResponse(waitingRooms.size(), waitingRooms);
    }

    public void removeRoomFromRedis(String chatroomId, Long consultantId) {
        String streamKey = "consultant:" + consultantId + ":waiting_rooms";
        try {
            // Redis Stream에서 해당 chatroomId와 일치하는 메시지 삭제
            List<MapRecord<String, Object, Object>> messages = redisStreamTemplate.opsForStream()
                    .read(StreamReadOptions.empty(), StreamOffset.fromStart(streamKey));

            for (MapRecord<String, Object, Object> message : messages) {
                Map<Object, Object> rawData = message.getValue();
                if (rawData.containsKey("waitingRoom")) {
                    String json = rawData.get("waitingRoom").toString();
                    WaitingRoomDTO waitingRoomDTO = objectMapper.readValue(json, WaitingRoomDTO.class);

                    // chatroomId가 일치하는 메시지 삭제
                    if (waitingRoomDTO.getChatroom().getChatroomId().equals(chatroomId)) {
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

    private ChatRoom fetchAndValidateChatRoom(String chatroomId, String expectedState, String newState) {
        ChatRoom chatRoom = chatRoomRepository.findByChatroomId(chatroomId)
                .orElseThrow(() -> new RuntimeException("No ChatRoom found: " + chatroomId));

        if (expectedState != null && !expectedState.equals(chatRoom.getChatroomStatus())) {
            if (newState.equals(chatRoom.getChatroomStatus())) {
                log.info("ChatRoom [{}] is already in the desired state '{}'. Skipping update.", chatroomId, newState);
                return chatRoom;
            }
            throw new RuntimeException("No ChatRoom found with the specified status: " + expectedState + " for chatroomId: " + chatroomId);
        }

        return chatRoom;
    }

    public ChatroomResponse updateChatRoomStatus(String chatroomId, String currentState, String newState) {
        int rowsUpdated = chatRoomRepository.updateStatusByChatroomId(chatroomId, currentState, newState);

        if (rowsUpdated == 0) {
            ChatRoom existingChatRoom = fetchAndValidateChatRoom(chatroomId, currentState, newState);
            ChatRoomDTO chatRoomDTO = chatMapper.toChatRoomDTO(existingChatRoom);
            removeRoomFromRedis(chatRoomDTO.getChatroomId(), chatRoomDTO.getConsultantId());
            return new ChatroomResponse(1, Collections.singletonList(chatRoomDTO));
        }

        ChatRoom updatedChatRoom = chatRoomRepository.findByChatroomId(chatroomId)
                .orElseThrow(() -> new RuntimeException("Failed to fetch updated ChatroomId for userId: " + chatroomId));


        ChatRoomDTO chatRoomDTO = chatMapper.toChatRoomDTO(updatedChatRoom);
        removeRoomFromRedis(chatRoomDTO.getChatroomId(), chatRoomDTO.getConsultantId());
        return new ChatroomResponse(1, Collections.singletonList(chatRoomDTO));
    }

    public ChatroomResponse updateActiveToCompleted(String chatroomId, String currentState, String newState) {
        if (!"active".equals(currentState) || !"completed".equals(newState)) {
            throw new IllegalArgumentException("Invalid state transition: " + currentState + " to " + newState);
        }

        int rowsUpdated = chatRoomRepository.updateStatusAndFinishedAt(chatroomId, currentState, newState);

        if (rowsUpdated == 0) {
            ChatRoom existingChatRoom = fetchAndValidateChatRoom(chatroomId, currentState, newState);
            ChatRoomDTO chatRoomDTO = chatMapper.toChatRoomDTO(existingChatRoom);
            return new ChatroomResponse(1, Collections.singletonList(chatRoomDTO));
        }

        ChatRoom updatedChatRoom = chatRoomRepository.findByChatroomId(chatroomId)
                .orElseThrow(() -> new RuntimeException("Failed to fetch updated ChatroomId for userId: " + chatroomId));


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