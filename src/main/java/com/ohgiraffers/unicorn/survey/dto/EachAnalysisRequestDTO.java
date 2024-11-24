package com.ohgiraffers.unicorn.survey.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

@Data
public class EachAnalysisRequestDTO {
    private List<Response> responses;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer mostCommonK;

    public EachAnalysisRequestDTO(List<Response> responses) {
        this.responses = responses;
    }

    public void setMostCommonK(Integer mostCommonK) {
        this.mostCommonK = mostCommonK;
    }

    @Data
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
    }
}
