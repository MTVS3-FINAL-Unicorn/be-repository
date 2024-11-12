package com.ohgiraffers.unicorn.report.service;

import com.ohgiraffers.unicorn.report.entity.Report;
import com.ohgiraffers.unicorn.report.repository.ReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReportService {

    @Autowired
    private ReportRepository reportRepository;

    public Report saveReport(Long meetingId, String analysisType, String analysisResult) {
        Report report = new Report(meetingId, analysisType, analysisResult);
        return reportRepository.save(report);
    }

    public List<Report> getReportsByMeetingId(Long meetingId) {
        return reportRepository.findByMeetingId(meetingId);
    }

}