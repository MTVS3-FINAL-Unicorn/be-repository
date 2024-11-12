package com.ohgiraffers.unicorn.survey.dto;

import lombok.Data;

@Data
public class VoiceRequestDTO {

    private String surveyQuestion;
    private String voiceResponse;
    private Long userId;
    private Long meetingId;
    private Long corpId;
    private Long questionId;

    public VoiceRequestDTO(String content, String encodedAudio, Long indivId, Long meetingId, Long corpId, Long questionId) {
        this.surveyQuestion = content;
        this.voiceResponse = encodedAudio;
        this.userId = indivId;
        this.meetingId = meetingId;
        this.corpId = corpId;
        this.questionId = questionId;
    }
}
