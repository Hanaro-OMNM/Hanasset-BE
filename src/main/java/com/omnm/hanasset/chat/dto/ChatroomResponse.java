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
public class ChatroomResponse {
    private Integer count;
    private List<ChatRoomDTO> chatrooms;

}

