package com.ohgiraffers.unicorn.report.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reportId;

    @Column
    private Long meetingId;

    @Column(nullable = true)
    private Long questionId;

    @Enumerated(EnumType.STRING)
    @Column
    private AnalysisType analysisType;

    @Column(columnDefinition = "LONGTEXT")
    private String analysisResult;

    public Report(Long meetingId, Long questionId, AnalysisType analysisType, String analysisResult) {
        this.meetingId = meetingId;
        this.questionId = questionId;
        this.analysisType = analysisType;
        this.analysisResult = analysisResult;
    }
}
