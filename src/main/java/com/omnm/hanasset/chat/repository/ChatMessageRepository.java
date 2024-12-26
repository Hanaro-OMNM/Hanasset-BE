package com.omnm.hanasset.chat.repository;

import com.omnm.hanasset.chat.entity.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    @Query("SELECT cm FROM ChatMessage cm WHERE cm.chatroom.chatroomId = :chatroomId")
    List<ChatMessage> findMessagesByChatroomId(@Param("chatroomId") String chatroomId);

}