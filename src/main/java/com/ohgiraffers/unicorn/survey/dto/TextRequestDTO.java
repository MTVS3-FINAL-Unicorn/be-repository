package com.ohgiraffers.unicorn.survey.dto;

import lombok.Data;

@Data
public class TextRequestDTO {

    private String surveyQuestion;
    private String textResponse;
    private Long userId;
    private Long meetingId;
    private Long corpId;
    private Long questionId;

    public TextRequestDTO(String content, String content1, Long indivId, Long meetingId, Long corpId, Long questionId) {
        this.surveyQuestion = content;
        this.textResponse = content1;
        this.userId = indivId;
        this.meetingId = meetingId;
        this.corpId = corpId;
        this.questionId = questionId;
    }
}
