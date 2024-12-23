package com.omnm.hanasset.chat.controller;

import com.omnm.hanasset.chat.dto.ChatMessageDTO;
import com.omnm.hanasset.chat.entity.ChatMessage;
import com.omnm.hanasset.chat.redis.service.RedisStreamPublisher;
import com.omnm.hanasset.chat.redis.service.RedisStreamSubscriber;
import com.omnm.hanasset.chat.utils.ChatMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController

@RequiredArgsConstructor
public class ChatController {

    @Autowired
    private ChatMapper chatMapper;  // ChatMapper 주입

    private final RedisStreamPublisher publisher;
    private final RedisStreamSubscriber subscriber;
    private final SimpMessagingTemplate messagingTemplate;

    /**
     * 메시지 발행: 채팅방 ID를 기반으로 Stream에 메시지 추가
     */
//    @PostMapping("/publish")
//    public String publishMessage(@RequestParam String chatroomId, @RequestBody ChatMessageDTO message) {
//        publisher.publishMessage(chatroomId, message);
//        return "Message published to chatroom: " + chatroomId;
//    }

    @MessageMapping("/chat.sendMessage/{roomId}")
    @SendTo("/topic/rooms/{roomId}")
    public ChatMessage sendMessage(@DestinationVariable String roomId, ChatMessage message) {
        // ChatMessage를 ChatMessageDTO로 변환
        ChatMessageDTO chatMessageDTO = chatMapper.toChatMessageDTO(message);
        publisher.publishMessage(roomId, chatMessageDTO);
        // 메시지 처리 로직
        return message;
    }

    /**
     * 메시지 구독: 채팅방 ID를 기반으로 메시지 소비
     */
//    @GetMapping("/consume")
//    public List<ChatMessageDTO> consumeMessages(@RequestParam String chatroomId) {
//        List<ChatMessageDTO> consumedMessages = subscriber.consumeMessages(chatroomId);
//        return consumedMessages; // 메시지 목록 반환
//    }

    /**
     * WebSocket 연결 시 Redis에서 메시지 기록을 불러와 클라이언트로 전송
     */
    @MessageMapping("/chat.history/{chatroomId}")
    public void reEnterChat(@DestinationVariable String chatroomId) {
        // Redis에서 메시지 읽기 (ACK 없이 유지)
        List<ChatMessageDTO> messages = subscriber.reLoadMessages(chatroomId);

        // WebSocket으로 클라이언트에게 메시지 전송
        messagingTemplate.convertAndSend("/topic/rooms/" + chatroomId, messages);
    }
}