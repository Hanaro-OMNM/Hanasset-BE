package com.omnm.hanasset.chat.controller;

import com.omnm.hanasset.chat.dto.ChatMessageResponse;
import com.omnm.hanasset.chat.dto.ChatRoomDTO;
import com.omnm.hanasset.chat.dto.ChatroomResponse;
import com.omnm.hanasset.chat.dto.WaitingRoomResponse;
import com.omnm.hanasset.chat.entity.ChatRoom;
import com.omnm.hanasset.chat.repository.ChatRoomRepository;
import com.omnm.hanasset.chat.service.ChatRoomService;
import com.omnm.hanasset.chat.utils.ChatMapper;
import com.omnm.hanasset.global.common.ApiResponseEntity;
import com.omnm.hanasset.global.exception.code.ErrorCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.*;


import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Tag(name = "채팅룸 관리", description = "채팅룸 관련 API 목록")
@RequestMapping("/chat")
@RequiredArgsConstructor
@RestController
public class ChatRoomController {
    private final ChatRoomService chatRoomService;
    private final ChatRoomRepository chatRoomRepository;
    private final RedisTemplate<String, Object> redisStreamTemplate;
    private final ChatMapper chatMapper;

    @Operation(summary = "모든 채팅방 조회", description = "전체 채팅방 목록을 조회합니다.")
    @ApiResponse(responseCode = "200", description = "채팅방 목록 조회 성공")
    @GetMapping("/list")
    public ApiResponseEntity<ChatroomResponse> getAllChatrooms() {
        ChatroomResponse response = chatRoomService.findAll();
        return ApiResponseEntity.ok("채팅방 목록 조회 성공", response);
    }

    @Operation(summary = "채팅방 생성", description = "새로운 채팅방을 생성합니다.")
    @ApiResponse(responseCode = "201", description = "채팅방 생성 성공")
    @PostMapping("/create")
    public ApiResponseEntity<ChatroomResponse> createChatroom(@RequestBody ChatRoomDTO request) {
        ChatroomResponse response = chatRoomService.createRoom(request.getUserId(),request.getConsultantId(),request.getChatroomTitle(), request.getReservedTime());

        return ApiResponseEntity.ok("채팅방 생성 성공", response);
    }

    @Operation(summary = "대기방 조회", description = "특정 상담사의 대기방 목록을 조회합니다.")
    @ApiResponse(responseCode = "200", description = "대기방 조회 성공")
    @GetMapping("/waiting/{consultantId}")
    public ApiResponseEntity<WaitingRoomResponse> getWaitingRooms(@PathVariable Long consultantId) {
        WaitingRoomResponse response = chatRoomService.getWaitingRooms(consultantId);
        return ApiResponseEntity.ok("대기방 조회 성공", response);
    }

    // 상담사 대기 목록에 채팅방 추가 API
    @Operation(summary = "대기방 생성", description = "특정 상담사의 대기방 목록을 생성합니다.")
    @ApiResponse(responseCode = "201", description = "대기방 생성 성공")
    @PostMapping("/add-waiting/{consultantId}")
    public ApiResponseEntity<Object> addWaitingRooms(@PathVariable Long consultantId) {
        try {
            // 대기 목록에 채팅방 추가
            chatRoomService.addWaitingRoomToStream(consultantId);
            return ApiResponseEntity.ok("Chat rooms added to waiting rooms successfully.", null);
        } catch (Exception e) {
            // 에러 발생 시
            return ApiResponseEntity.fail(ErrorCode.INTERNAL_SERVER_ERROR)
                    .withMessage("Error occurred while adding chat rooms to waiting rooms.");
        }
    }

    @Operation(summary = "예약된 상담 조회", description = "특정 유저의 예약된 상담을 조회합니다.")
    @ApiResponse(responseCode = "200", description = "예약 상담 조회 성공")
    @GetMapping("/findRoom")
    public ApiResponseEntity<Object> findRoomDetails(@RequestParam Long userId, @RequestParam String chatroomStatus) {
        try {
            // DB에서 직접 조회
            ChatRoom chatRoom = chatRoomRepository.findRoomIdByUserIdAndStatus(userId, chatroomStatus)
                    .map(roomId -> chatRoomRepository.findByChatroomId(roomId)
                            .orElseThrow(() -> new RuntimeException("Chat room not found in DB: " + roomId)))
                    .orElseThrow(() -> new RuntimeException("No room found for userId: " + userId + " with status: " + chatroomStatus));

            ChatRoomDTO chatRoomDTO = chatMapper.toChatRoomDTO(chatRoom);

            return ApiResponseEntity.ok("Room found successfully.", chatRoomDTO);
        } catch (RuntimeException e) {
            return ApiResponseEntity.fail(ErrorCode.NOT_FOUND)
                    .withMessage("Room not found for userId: " + userId + " and status: " + chatroomStatus);
        }
    }

    @Operation(summary = "채팅방 삭제", description = "특정 채팅방을 삭제합니다.")
    @ApiResponse(responseCode = "200", description = "채팅방 삭제 성공")
    @DeleteMapping("/delete/{chatroomId}")
    public ApiResponseEntity<Void> deleteChatroom(@PathVariable String chatroomId) {
        chatRoomService.deleteRoom(chatroomId);
        return ApiResponseEntity.ok("채팅방 삭제 성공", null);
    }

    @Operation(summary = "채팅방 상태 업데이트", description = "특정 채팅방의 상태를 업데이트합니다.")
    @ApiResponse(responseCode = "200", description = "채팅방 상태 업데이트 성공")
    @PutMapping("/update-status")
    public ApiResponseEntity<Object> updateChatRoomStatus(@RequestBody Map<String, String> request) {
        try {
            String chatroomId = request.get("chatroomId");
            String currentState = request.get("state");

            // Determine the next state
            String newState = determineNextState(currentState);

            if (newState == null) {
                return ApiResponseEntity.fail(ErrorCode.BAD_REQUEST)
                        .withMessage("Invalid state: " + currentState);
            }

            // Call appropriate service method based on state transition
            ChatroomResponse response;
            if ("active".equals(currentState) && "completed".equals(newState)) {
                response = chatRoomService.updateActiveToCompleted(chatroomId, currentState, newState);
            } else {
                response = chatRoomService.updateChatRoomStatus(chatroomId, currentState, newState);
            }

            return ApiResponseEntity.ok("채팅방 상태 업데이트 성공", response);

        } catch (RuntimeException e) {
            return ApiResponseEntity.fail(ErrorCode.BAD_REQUEST)
                    .withMessage("Runtime error: " + e.getMessage());
        } catch (Exception e) {
            return ApiResponseEntity.fail(ErrorCode.INTERNAL_SERVER_ERROR)
                    .withMessage("Unexpected error: " + e.getMessage());
        }
    }




    // 다음 상태를 결정하는 유틸리티 메서드
    private String determineNextState(String currentState) {
        if ("waiting".equalsIgnoreCase(currentState)) {
            return "active";
        } else if ("active".equalsIgnoreCase(currentState)) {
            return "completed";
        }
        return null;
    }

    // Redis에 상태를 저장하는 유틸리티 메서드
    private boolean setChatRoomStatusInRedis(String chatroomId, String status) {
        final String key = "stream_" + chatroomId;
        final ValueOperations<String, Object> valueOperations = redisStreamTemplate.opsForValue();

        valueOperations.set(key, status);
        return redisStreamTemplate.expire(key, 1, TimeUnit.HOURS);
    }




    @Operation(summary = "완료된 채팅방 조회", description = "특정 사용자의 완료된 채팅방 목록을 조회합니다.")
    @ApiResponse(responseCode = "200", description = "완료된 채팅방 조회 성공")
    @GetMapping("/completed-chatrooms")
    public ApiResponseEntity<ChatroomResponse> getCompletedChatroomsByUserId(@RequestParam Long userId) {
        ChatroomResponse response = chatRoomService.getCompletedChatroomsByUserId(userId);
        return ApiResponseEntity.ok("완료된 채팅방 조회 성공", response);
    }


    @Operation(summary = "특정 채팅방 메시지 조회", description = "특정 채팅방의 메시지 목록을 조회합니다.")
    @ApiResponse(responseCode = "200", description = "메시지 조회 성공")
    // API 2: Get messages for a specific chatroom
    @GetMapping("/{chatroomId}/messages")
    public ApiResponseEntity<ChatMessageResponse> getMessagesByChatroomId(@PathVariable String chatroomId) {
        ChatMessageResponse response = chatRoomService.getMessagesByChatroomId(chatroomId);
        return ApiResponseEntity.ok("메시지 조회 성공", response);
    }
}