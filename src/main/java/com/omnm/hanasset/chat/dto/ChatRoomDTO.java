package com.omnm.hanasset.chat.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "채팅방 DTO")
public class ChatRoomDTO {

    @Schema(description = "채팅방 ID")
    private String chatroomId;

    @Schema(description = "사용자 ID")
    private Long userId;

    @Schema(description = "상담사 ID")
    private Long consultantId;

    @Schema(description = "채팅방 제목")
    private String chatroomTitle;

    @Schema(description = "채팅방 상태")
    private String chatroomStatus;

    @Schema(description = "예약 시간", example = "2024-12-23 10:30:00")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime reservedTime;

    @Schema(description = "상담 종료 시간", example = "2024-12-23 11:00:00")
    private LocalDateTime finishedAt;

    @Schema(description = "채팅방 생성 시간", example = "2024-12-23 09:00:00")
    private LocalDateTime createdAt;
}
