package com.omnm.hanasset.chat.entity;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.omnm.hanasset.chat.dto.ReservationInfoDTO;
import lombok.*;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@ToString
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "chatroom")
public class ChatRoom {
    @Id
    @Column(name="chatroom_id")
    private String chatroomId;

    @Column(name="user_id")
    private Long userId;

    @Column(name="consultant_id")
    private Long consultantId;

    @Column(name="chatroom_title")
    private String chatroomTitle;

    @Column(name="chatroom_status")
    private String chatroomStatus; // 'waiting', 'active', 'done', 'canceled'

    @Column(name="reserved_time")
    private LocalDateTime reservedTime;

    @Column(name="finished_at")
    private LocalDateTime finishedAt;

    @Column(name="created_at")
    private LocalDateTime createdAt;

    @Lob
    @Column(name = "reservation_info")
    private String reservationInfo; // JSON 문자열로 저장

    // 예약 정보를 객체로 변환하는 메서드
    public List<ReservationInfoDTO> getReservationInfoAsList() {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(reservationInfo, new TypeReference<List<ReservationInfoDTO>>() {});
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList(); // 오류가 발생하면 빈 리스트 반환
        }
    }

    // 예약 정보를 객체로 설정하는 메서드
    public void setReservationInfoAsList(List<ReservationInfoDTO> reservationInfoList) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            this.reservationInfo = objectMapper.writeValueAsString(reservationInfoList); // JSON 문자열로 변환
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}