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
import java.util.List;

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
    @Mapping(source = "reservationInfo", target = "reservationInfo")
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

    default List<ReservationInfoDTO> mapStringToReservationInfo(String reservationInfoJson) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(reservationInfoJson, objectMapper.getTypeFactory().constructCollectionType(List.class, ReservationInfoDTO.class));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
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