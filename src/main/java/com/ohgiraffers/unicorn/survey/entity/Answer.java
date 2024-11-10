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

    private Long questionId;
    private Long indivId;
    private String content;

    public Answer() {}

    public Answer(Long questionId, Long indivId, String content) {
        this.questionId = questionId;
        this.indivId = indivId;
        this.content = content;
    }

    public Long getAnswerId() {
        return answerId;
    }

    public Long getQuestionId() {
        return questionId;
    }

    public Long getIndivId() {
        return indivId;
    }

    public String getContent() {
        return content;
    }

    public void setQuestionId(Long questionId) {
        this.questionId = questionId;
    }

    public void setIndivId(Long indivId) {
        this.indivId = indivId;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "Answer{" +
                "answerId=" + answerId +
                ", questionId=" + questionId +
                ", indivId=" + indivId +
                ", content='" + content + '\'' +
                '}';
    }
}
