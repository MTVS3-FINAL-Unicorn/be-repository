package com.ohgiraffers.unicorn.report.repository;

import com.ohgiraffers.unicorn.report.entity.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReportRepository extends JpaRepository<Report, Long> {
    @Query("SELECT r FROM Report r WHERE r.meetingId = :meetingId AND r.questionId IS NULL")
    List<Report> findReportsByMeetingIdAndNullQuestionId(@Param("meetingId") Long meetingId);

    List<Report> findByQuestionId(Long questionId);
}

