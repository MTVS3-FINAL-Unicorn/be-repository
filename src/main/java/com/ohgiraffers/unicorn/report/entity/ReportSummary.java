package com.ohgiraffers.unicorn.report.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class ReportSummary {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long meetingId;

    private Long corpId;

    @Column(columnDefinition = "TEXT")
    private String summary;

    public ReportSummary(Long meetingId, Long corpId, String summary) {
        this.meetingId = meetingId;
        this.corpId = corpId;
        this.summary = summary;
    }
}

