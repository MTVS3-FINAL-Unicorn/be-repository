package com.ohgiraffers.unicorn.survey.entity;

import jakarta.persistence.*;
import java.util.List;

@Entity
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long questionId;

    private Long meetingId;
    private String content;

    @Enumerated(EnumType.STRING)
    private QuestionType type;

    // Only applicable for PREFERENCE questions, stores URLs of image options
    @ElementCollection
    private List<String> options;

    public Question() {}

    public Question(Long questionId, Long meetingId, String content, QuestionType type, List<String> options) {
        this.questionId = questionId;
        this.meetingId = meetingId;
        this.content = content;
        this.type = type;
        this.options = options;
    }

    public Question(Long meetingId, String content, QuestionType type) {
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

    public List<String> getOptions() {
        return options;
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

    public void setOptions(List<String> options) {
        this.options = options;
    }
}
