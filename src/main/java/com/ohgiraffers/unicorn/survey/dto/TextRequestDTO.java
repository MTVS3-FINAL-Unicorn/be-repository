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

    public TextRequestDTO(Long questionId, Long indivId, String content) {
        this.questionId = questionId;
        this.textResponse = content;
        this.userId = indivId;
    }
}
