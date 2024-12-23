package com.omnm.hanasset.chat.repository;

import com.omnm.hanasset.chat.entity.Consultant;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ConsultantRepository extends JpaRepository<Consultant, Long> {
}