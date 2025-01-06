package com.omnm.hanasset.chat.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.omnm.hanasset.chat.dto.ChatMessageDTO;
import com.omnm.hanasset.chat.dto.ChatRoomDTO;
import com.omnm.hanasset.chat.dto.ReservationInfoDTO;
import com.omnm.hanasset.chat.entity.ChatMessage;
import com.omnm.hanasset.chat.entity.ChatRoom;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Collections;
import java.util.List;

@Mapper(componentModel = "spring")
public interface ChatMapper {

    // ChatMessage -> ChatMessageDTO
    @Mapping(source = "messageType", target = "messageType")
    @Mapping(source = "chatroom.chatroomId", target = "chatroomId")
    @Mapping(source = "senderId", target = "senderId")
    @Mapping(source = "content", target = "content")
    @Mapping(source = "accessor", target = "accessor")
    @Mapping(source = "createdAt", target = "createdAt", dateFormat = "yyyy-MM-dd HH:mm:ss")
    ChatMessageDTO toChatMessageDTO(ChatMessage message);

    // ChatRoom -> ChatRoomDTO
    @Mapping(source = "chatroomId", target = "chatroomId")
    @Mapping(source = "user.userId", target = "userId")
    @Mapping(source = "consultant.consultantId", target = "consultantId")
    @Mapping(source = "chatroomTitle", target = "chatroomTitle")
    @Mapping(source = "chatroomStatus", target = "chatroomStatus")
    @Mapping(source = "reservedTime", target = "reservedTime", dateFormat = "yyyy-MM-dd HH:mm:ss")
    @Mapping(source = "finishedAt", target = "finishedAt", dateFormat = "yyyy-MM-dd HH:mm:ss")
    @Mapping(source = "createdAt", target = "createdAt", dateFormat = "yyyy-MM-dd HH:mm:ss")
    @Mapping(source = "reservationInfo", target = "reservationInfo")
    ChatRoomDTO toChatRoomDTO(ChatRoom chatRoom);

    // ChatMessageDTO -> ChatMessage
    @Mapping(source = "messageType", target = "messageType")
    @Mapping(source = "chatroomId", target = "chatroom")
    @Mapping(source = "senderId", target = "senderId")
    @Mapping(source = "content", target = "content")
    @Mapping(source = "accessor", target = "accessor")
    @Mapping(source = "createdAt", target = "createdAt")
    ChatMessage toChatMessage(ChatMessageDTO chatMessageDTO);

    default String mapMessageType(ChatMessageDTO.MessageType messageType) {
        return messageType == null ? null : messageType.name();
    }

    default ChatRoom mapChatroom(String chatroomId) {
        if (chatroomId == null) return null;
        return ChatRoom.builder().chatroomId(chatroomId).build();
    }

    default LocalDateTime mapCreatedAt(String createdAt) {
        if (createdAt == null) return null;
        try {
            return OffsetDateTime.parse(createdAt, DateTimeFormatter.ISO_DATE_TIME).toLocalDateTime();
        } catch (DateTimeParseException e) {
            return LocalDateTime.parse(createdAt, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        }
    }


    // Custom mappers for JSON <-> Object conversion
    default List<ReservationInfoDTO> mapStringToReservationInfo(String reservationInfoJson) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(reservationInfoJson, objectMapper.getTypeFactory()
                    .constructCollectionType(List.class, ReservationInfoDTO.class));
        } catch (IOException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    default String mapReservationInfoToString(List<ReservationInfoDTO> reservationInfo) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(reservationInfo);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}