package com.omnm.hanasset.chat.entity;

import jakarta.persistence.*;
import lombok.*;


import java.time.LocalDateTime;


@ToString
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "chat_message")
public class ChatMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chat_message_id", nullable = false)
    private Long chatMessageId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chatroom_id", nullable = false)
    private ChatRoom chatroom;

    @Column(name = "sender_id", nullable = false)
    private Long senderId;

    @Column(name = "accessor", length = 225)
    private String accessor;

    @Column(name = "content", columnDefinition = "TEXT")
    private String content;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "message_type", length = 225)
    private String messageType;
}