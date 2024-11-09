package com.ohgiraffers.unicorn.survey.service;

import com.ohgiraffers.unicorn.survey.entity.Question;
import com.ohgiraffers.unicorn.survey.entity.QuestionType;
import com.ohgiraffers.unicorn.survey.repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class QuestionService {

    @Autowired
    private QuestionRepository questionRepository;

    public Question createQuestion(Long meetingId, String content, QuestionType type) {

        Question question = new Question();

        question.setMeetingId(meetingId);
        question.setContent(content);
        question.setType(type);

        return questionRepository.save(question);
    }
}
