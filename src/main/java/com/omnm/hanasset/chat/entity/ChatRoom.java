package com.omnm.hanasset.chat.entity;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.omnm.hanasset.chat.dto.ReservationInfoDTO;
import com.omnm.hanasset.consultant.entity.Consultant;
import com.omnm.hanasset.user.entity.User;
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
    @Column(name = "chatroom_id", nullable = false, length = 255)
    private String chatroomId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "consultant_id", nullable = false)
    private Consultant consultant;

    @Column(name = "chatroom_title", length = 255)
    private String chatroomTitle;

    @Column(name = "chatroom_status", length = 255)
    private String chatroomStatus;

    @Column(name = "reserved_time")
    private LocalDateTime reservedTime;

    @Column(name = "finished_at")
    private LocalDateTime finishedAt;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "reservation_info", columnDefinition = "TEXT")
    private String reservationInfo;

}