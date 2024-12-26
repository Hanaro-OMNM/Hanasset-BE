package com.omnm.hanasset.consultant.repository;

import com.omnm.hanasset.consultant.entity.Consultant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ConsultantRepository extends JpaRepository<Consultant, Long> {
    Optional<Consultant> findByconsultantLoginId(String loginId);
}