package com.omnm.hanasset.chat.entity;

import lombok.*;

import jakarta.persistence.*;
import java.time.LocalDateTime;

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
}