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
    private Long chatMessageId;
    private String chatroomId;
    private Long senderId;
    private String accessor; // 'guest' | 'consultant'
    private String content;
    private LocalDateTime createdAt;
    private String messageType; // 'talk' | 'join'
}