package com.omnm.hanasset.chat.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "채팅방 DTO")
public class WaitingRoomDTO {
    @Schema(description = "유저 정보 ")
    private String userName;

    @Schema(description = "채팅방 목록")
    private ChatRoomDTO chatroom;
}
