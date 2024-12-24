package com.omnm.hanasset.user.repository;

import com.omnm.hanasset.user.entity.Property;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PropertyRepository extends JpaRepository<Property, Long> {
    Optional<Property> findByUser_UserId(Long userId);
}
