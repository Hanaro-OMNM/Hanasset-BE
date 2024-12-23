package com.omnm.hanasset.chat.dto;


import lombok.*;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatMessageResponse {
    private Integer count;
    private List<ChatMessageDTO> chatMessages;

}
