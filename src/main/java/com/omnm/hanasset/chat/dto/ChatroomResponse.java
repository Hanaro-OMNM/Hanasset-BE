package com.omnm.hanasset.chat.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;


@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatroomResponse {
    private String message;
    private Result result;



    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Result {
        private List<ChatRoomDTO> chatroomResponse;
    }
}

