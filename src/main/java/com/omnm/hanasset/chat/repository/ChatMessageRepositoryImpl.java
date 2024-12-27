package com.omnm.hanasset.chat.repository;

import com.omnm.hanasset.chat.entity.ChatMessage;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Repository
public class ChatMessageRepositoryImpl implements ChatMessageRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public void saveAllBulk(List<ChatMessage> messages) {
        for (int i = 0; i < messages.size(); i++) {
            entityManager.persist(messages.get(i));

            if (i % 50 == 0) {
                entityManager.flush();
                entityManager.clear();
            }
        }
        entityManager.flush();
        entityManager.clear();
    }
}
