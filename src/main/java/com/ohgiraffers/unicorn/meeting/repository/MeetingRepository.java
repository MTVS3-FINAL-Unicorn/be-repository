package com.ohgiraffers.unicorn.meeting.repository;

import com.ohgiraffers.unicorn.meeting.entity.Meeting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MeetingRepository extends JpaRepository<Meeting, Long> {
    @Query("SELECT m FROM Meeting m JOIN m.participantStatus p WHERE p.userId = :userId")
    List<Meeting> findAllByParticipantUserId(@Param("userId") Long userId);

    List<Meeting> findByCorpId(Long corpId);

    List<Meeting> findByCategoryId(Long categoryId);
}
