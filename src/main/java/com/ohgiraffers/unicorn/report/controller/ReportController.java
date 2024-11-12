package com.ohgiraffers.unicorn.report.controller;

import com.ohgiraffers.unicorn.report.entity.Report;
import com.ohgiraffers.unicorn.report.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/reports")
public class ReportController {

    @Autowired
    private ReportService reportService;

    @GetMapping("/{meetingId}")
    public ResponseEntity<List<Report>> getReportsByMeetingId(@PathVariable Long meetingId) {
        List<Report> reports = reportService.getReportsByMeetingId(meetingId);
        return ResponseEntity.ok(reports);
    }
}

