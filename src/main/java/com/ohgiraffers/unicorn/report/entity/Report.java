package com.ohgiraffers.unicorn.report.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "report")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reportId;

    private Long meetingId;
    private String analysisType;
    private String analysisResult;

    public Report(Long meetingId, String analysisType, String analysisResult) {
        this.meetingId = meetingId;
        this.analysisType = analysisType;
        this.analysisResult = analysisResult;
    }
}
