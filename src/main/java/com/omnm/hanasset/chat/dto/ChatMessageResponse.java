package com.omnm.hanasset.chat.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "채팅 메시지 응답 DTO")
public class ChatMessageResponse {

    @Schema(description = "총 메시지 수")
    private Integer count;

    @Schema(description = "채팅 메시지 목록")
    private List<ChatMessageDTO> chatMessages;
}
