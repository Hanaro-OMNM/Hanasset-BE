package com.omnm.hanasset.chat.repository;

import com.omnm.hanasset.chat.entity.ConsultingItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConsultingItemRepository extends JpaRepository<ConsultingItem, Long> {
    List<ConsultingItem> findAllByChatroom_ChatroomId(@Param("chatroomId") String chatroomId);
}