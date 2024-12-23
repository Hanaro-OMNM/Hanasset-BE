package com.omnm.hanasset.chat.utils;

import com.omnm.hanasset.chat.dto.ChatMessageDTO;
import com.omnm.hanasset.chat.dto.ChatRoomDTO;
import com.omnm.hanasset.chat.entity.ChatMessage;
import com.omnm.hanasset.chat.entity.ChatRoom;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ChatMapper {

    // ChatMessage 엔티티 -> ChatMessageDTO로 변환
    @Mapping(source = "messageType", target = "messageType")
    @Mapping(source = "chatroomId", target = "chatroomId")
    @Mapping(source = "senderId", target = "senderId")
    @Mapping(source = "content", target = "content")
    @Mapping(source = "accessor", target = "accessor")
    @Mapping(source = "createdAt", target = "createdAt", dateFormat = "yyyy-MM-dd HH:mm:ss")
    ChatMessageDTO toChatMessageDTO(ChatMessage message);

    // ChatRoom 엔티티 -> ChatRoomDTO로 변환
    @Mapping(source = "chatroomId", target = "chatroomId")
    @Mapping(source = "userId", target = "userId")
    @Mapping(source = "consultantId", target = "consultantId")
    @Mapping(source = "chatroomTitle", target = "chatroomTitle")
    @Mapping(source = "chatroomStatus", target = "chatroomStatus")
    @Mapping(source = "reservedTime", target = "reservedTime", dateFormat = "yyyy-MM-dd HH:mm:ss")
    @Mapping(source = "finishedAt", target = "finishedAt", dateFormat = "yyyy-MM-dd HH:mm:ss")
    @Mapping(source = "createdAt", target = "createdAt", dateFormat = "yyyy-MM-dd HH:mm:ss")
    ChatRoomDTO toChatRoomDTO(ChatRoom chatRoom);

    // ChatMessageDTO -> ChatMessage 엔티티로 변환
    @Mapping(source = "messageType", target = "messageType")
    @Mapping(source = "chatroomId", target = "chatroomId")
    @Mapping(source = "senderId", target = "senderId")
    @Mapping(source = "content", target = "content")
    @Mapping(source = "accessor", target = "accessor")
    @Mapping(source = "createdAt", target = "createdAt", dateFormat = "yyyy-MM-dd HH:mm:ss")
    ChatMessage toChatMessage(ChatMessageDTO chatMessageDTO);

    // ChatRoomDTO -> ChatRoom 엔티티로 변환
    @Mapping(source = "chatroomId", target = "chatroomId")
    @Mapping(source = "userId", target = "userId")
    @Mapping(source = "consultantId", target = "consultantId")
    @Mapping(source = "chatroomTitle", target = "chatroomTitle")
    @Mapping(source = "chatroomStatus", target = "chatroomStatus")
    @Mapping(source = "reservedTime", target = "reservedTime", dateFormat = "yyyy-MM-dd HH:mm:ss")
    @Mapping(source = "finishedAt", target = "finishedAt", dateFormat = "yyyy-MM-dd HH:mm:ss")
    @Mapping(source = "createdAt", target = "createdAt", dateFormat = "yyyy-MM-dd HH:mm:ss")
    ChatRoom toChatRoom(ChatRoomDTO chatRoomDTO);

    // 추가적으로 필요할 경우, 변환 로직 구현
}
