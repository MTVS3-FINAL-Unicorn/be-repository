package com.ohgiraffers.unicorn.survey.entity;

import jakarta.persistence.*;

@Entity
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long questionId;

    private Long meetingId;
    private String content;

    @Enumerated(EnumType.STRING)
    private QuestionType type;

    public Question(){};

    public Question(Long questionId, Long meetingId, String content, QuestionType type) {
        this.questionId = questionId;
        this.meetingId = meetingId;
        this.content = content;
        this.type = type;
    }

    public Long getQuestionId() {
        return questionId;
    }

    public Long getMeetingId() {
        return meetingId;
    }

    public String getContent() {
        return content;
    }

    public QuestionType getType() {
        return type;
    }

    public void setMeetingId(Long meetingId) {
        this.meetingId = meetingId;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setType(QuestionType type) {
        this.type = type;
    }
}
