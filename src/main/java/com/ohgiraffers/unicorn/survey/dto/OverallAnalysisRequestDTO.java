package com.ohgiraffers.unicorn.survey.dto;

import lombok.Data;

@Data
public class OverallAnalysisRequestDTO {
    private Long corpId;
    private Long meetingId;

    public OverallAnalysisRequestDTO(Long corpId, Long meetingId) {
        this.corpId = corpId;
        this.meetingId = meetingId;
    }
}
