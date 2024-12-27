package com.omnm.hanasset.chat.repository;

import com.omnm.hanasset.chat.entity.ChatMessage;

import java.util.List;

public interface ChatMessageRepositoryCustom {
    void saveAllBulk(List<ChatMessage> messages);
}