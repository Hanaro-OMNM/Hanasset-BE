package com.omnm.hanasset.chat.repository;

import com.omnm.hanasset.chat.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}