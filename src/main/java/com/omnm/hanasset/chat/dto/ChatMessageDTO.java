package com.omnm.hanasset.chat.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "채팅 메시지 DTO")
public class ChatMessageDTO {

    public enum MessageType {
        @Schema(description = "가입 메시지")
        JOIN,

        @Schema(description = "대화 메시지")
        TALK
    }

    @Schema(description = "메시지 타입")
    private MessageType messageType;

    @Schema(description = "채팅방 ID")
    private String chatroomId;

    @Schema(description = "보낸 사람 ID")
    private Long senderId;

    @Schema(description = "메시지 내용")
    private String content;

    @Schema(description = "접속자 정보")
    private String accessor;

    @Schema(description = "메시지 생성 시간")
    private String createdAt;
}
