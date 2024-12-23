package com.omnm.hanasset.chat.dto;

import com.omnm.hanasset.chat.entity.ChatMessage;
import lombok.*;


@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatMessageDTO {


    public enum MessageType {
        JOIN, TALK
    }

    private MessageType messageType;
    private String chatroomId; // 수정됨: roomId -> chatroomId
    private Long senderId;   // 수정됨: sender -> senderId
    private String content;  // 수정됨: message -> content
    private String accessor; // 'guest' | 'consultant'
    private String createdAt; // datetime
}