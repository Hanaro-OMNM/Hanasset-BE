package com.omnm.hanasset.chat.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatRoomDTO {

    private String chatroomId;
    private Long userId;
    private Long consultantId;
    private String chatroomTitle;
    private String chatroomStatus;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime reservedTime;
    private LocalDateTime finishedAt;
    private LocalDateTime createdAt;


    //Mapper로 구현
//    // ChatRoom 엔티티를 DTO로 변환하는 생성자
//    public ChatRoomDTO(ChatRoom chatRoom)  {
//        this.chatroomId = chatRoom.getChatroomId();
//        this.userId = chatRoom.getUserId();
//        this.consultantId = chatRoom.getConsultantId();
//        this.chatroomTitle = chatRoom.getChatroomTitle();
//        this.chatroomStatus = chatRoom.getChatroomStatus();
//        this.reservedTime = chatRoom.getReservedTime();
//        this.finishedAt = chatRoom.getFinishedAt();
//        this.createdAt = chatRoom.getCreatedAt();
//    }
}


