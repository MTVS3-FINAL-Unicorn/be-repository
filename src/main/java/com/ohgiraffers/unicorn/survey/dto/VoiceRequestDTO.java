package com.ohgiraffers.unicorn.survey.dto;

import lombok.Data;

@Data
public class VoiceRequestDTO {

    private String surveyQuestion;
    private byte[] voiceResponse;
    private Long userId;
    private Long meetingId;
    private Long corpId;
    private Long questionId;

    public VoiceRequestDTO(Long questionId, Long indivId, byte[] audioData) {
        this.questionId = questionId;
        this.userId = indivId;
        this.voiceResponse = audioData;
    }
}
