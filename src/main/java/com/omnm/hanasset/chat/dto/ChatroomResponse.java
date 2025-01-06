package com.omnm.hanasset.chat.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "채팅방 응답 DTO")
public class ChatroomResponse {

    @Schema(description = "채팅방 수")
    private Integer count;

    @Schema(description = "채팅방 목록")
    private List<ChatRoomDTO> chatrooms;
}
