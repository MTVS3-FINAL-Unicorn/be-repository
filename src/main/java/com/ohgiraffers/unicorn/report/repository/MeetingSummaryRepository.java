package com.ohgiraffers.unicorn.report.repository;
import com.ohgiraffers.unicorn.report.entity.MeetingSummary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MeetingSummaryRepository extends JpaRepository<MeetingSummary, Long> {
}
