package com.ohgiraffers.unicorn.report.repository;

import com.ohgiraffers.unicorn.report.entity.Report;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReportRepository extends JpaRepository<Report, Long> {
    List<Report> findByMeetingId(Long meetingId);
}

