package com.ohgiraffers.unicorn.meeting.repository;

import com.ohgiraffers.unicorn.meeting.entity.Meeting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MeetingRepository extends JpaRepository<Meeting, Long> {
}
