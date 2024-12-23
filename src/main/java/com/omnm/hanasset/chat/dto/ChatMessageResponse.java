package com.omnm.hanasset.chat.dto;


import lombok.*;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatMessageResponse {
    private String message;
    private Result result;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Result {
        private List<ChatMessageDTO> chatMessageResponse;
    }
}
