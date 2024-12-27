package com.omnm.hanasset.chat.repository;
import com.omnm.hanasset.chat.entity.ConsultingItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConsultingItemRepository extends JpaRepository<ConsultingItem, Long> {
}