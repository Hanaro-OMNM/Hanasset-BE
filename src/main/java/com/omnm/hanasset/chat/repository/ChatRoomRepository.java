package com.omnm.hanasset.chat.repository;

import com.omnm.hanasset.chat.entity.ChatRoom;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


public interface ChatRoomRepository extends JpaRepository<ChatRoom, String> {

    // Assuming ChatRoomRepository is a JPA repository
    @Query("SELECT c FROM ChatRoom c WHERE c.consultantId = :consultantId AND c.chatroomStatus = 'waiting' AND c.reservedTime >= :startOfDay AND c.reservedTime < :endOfDay")
    List<ChatRoom> findWaitingRoomsByConsultantIdAndReservedDate(
            @Param("consultantId") Long consultantId,
            @Param("startOfDay") LocalDateTime startOfDay,
            @Param("endOfDay") LocalDateTime endOfDay
    );

    @Query("SELECT c.chatroomId FROM ChatRoom c WHERE c.userId = :userId AND c.chatroomStatus = :chatroomStatus")
    Optional<String> findRoomIdByUserIdAndStatus(@Param("userId") Long userId, @Param("chatroomStatus") String chatroomStatus);

    // 1. 상태를 업데이트하는 쿼리
    @Modifying
    @Transactional
    @Query("UPDATE ChatRoom c SET c.chatroomStatus = :newState " +
            "WHERE c.chatroomId = :chatroomId AND c.chatroomStatus = :currentState")
    int updateStatusByChatroomId(@Param("chatroomId") String chatroomId,
                                 @Param("currentState") String currentState,
                                 @Param("newState") String newState);

    @Modifying
    @Transactional
    @Query("UPDATE ChatRoom c SET c.chatroomStatus = :newState, " +
            "c.finishedAt = CURRENT_TIMESTAMP " +
            "WHERE c.chatroomId = :chatroomId AND c.chatroomStatus = :currentState")
    int updateStatusAndFinishedAt(
            @Param("chatroomId") String chatroomId,
            @Param("currentState") String currentState,
            @Param("newState") String newState
    );

    // 2. 업데이트된 채팅방 조회
    @Query("SELECT c FROM ChatRoom c WHERE c.chatroomId = :chatroomId AND c.chatroomStatus = :newState")
    Optional<ChatRoom> findUpdatedRoomByChatroomIdAndState(@Param("chatroomId") Long chatroomId,
                                                           @Param("newState") String newState);


    @Query("SELECT c FROM ChatRoom c WHERE c.chatroomId = :chatroomId")
    Optional<ChatRoom> findByChatroomId(@Param("chatroomId") String chatroomId);

    @Query("SELECT cr FROM ChatRoom cr WHERE cr.userId = :userId AND cr.chatroomStatus = 'completed'")
    List<ChatRoom> findCompletedChatroomsByUserId(@Param("userId") Long userId);




}