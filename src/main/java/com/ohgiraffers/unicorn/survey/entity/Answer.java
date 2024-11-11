package com.ohgiraffers.unicorn.survey.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Answer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long answerId;

    private Long meetingId;
    private Long questionId;
    private Long indivId;
    private String content;

    public Answer() {}

    public Answer(Long meetingId, Long questionId, Long indivId, String content) {
        this.meetingId = meetingId;
        this.questionId = questionId;
        this.indivId = indivId;
        this.content = content;
    }

    public Long getAnswerId() {
        return answerId;
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

    public String getContent() {
        return content;
    }

    public void setAnswerId(Long answerId) {
        this.answerId = answerId;
    }

    public void setIndivId(Long indivId) {
        this.indivId = indivId;
    }

    public void setMeetingId(Long meetingId) {
        this.meetingId = meetingId;
    }

    public void setQuestionId(Long questionId) {
        this.questionId = questionId;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "Answer{" +
                "answerId=" + answerId +
                ", indivId=" + indivId +
                ", meetingId=" + meetingId +
                ", questionId=" + questionId +
                ", content='" + content + '\'' +
                '}';
    }
}
