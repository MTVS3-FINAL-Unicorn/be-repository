package com.ohgiraffers.unicorn.report.repository;
import com.ohgiraffers.unicorn.report.entity.ReportSummary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReportSummaryRepository extends JpaRepository<ReportSummary, Long> {
}
