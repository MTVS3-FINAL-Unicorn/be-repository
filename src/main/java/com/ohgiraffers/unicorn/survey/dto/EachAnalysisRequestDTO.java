package com.ohgiraffers.unicorn.survey.dto;

import lombok.Data;

import java.util.List;

@Data
public class EachAnalysisRequestDTO {
    private List<Response> responses;

    public EachAnalysisRequestDTO(List<Response> responses) {
        this.responses = responses;
    }

    public List<Response> getResponses() {
        return responses;
    }

    public static class Response {
        private Long indivId;
        private Long meetingId;
        private Long questionId;
        private String answer;

        public Response(Long indivId, Long meetingId, Long questionId, String answer) {
            this.indivId = indivId;
            this.meetingId = meetingId;
            this.questionId = questionId;
            this.answer = answer;
        }

        public Long getIndivId() {
            return indivId;
        }

        public Long getMeetingId() {
            return meetingId;
        }

        public Long getQuestionId() {
            return questionId;
        }

        public String getAnswer() {
            return answer;
        }
    }
}
