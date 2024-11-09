package com.ohgiraffers.unicorn.survey.controller;

import com.ohgiraffers.unicorn.survey.dto.QuestionDTO;
import com.ohgiraffers.unicorn.survey.entity.Question;
import com.ohgiraffers.unicorn.survey.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/survey")
public class SurveyController {

    @Autowired
    private QuestionService questionService;

    @PostMapping("/question")
    public ResponseEntity<Question> createQuestion(@RequestBody QuestionDTO questionRequest) {
        Question question = questionService.createQuestion(
                questionRequest.getMeetingId(),
                questionRequest.getContent(),
                questionRequest.getType()
        );
        return ResponseEntity.ok(question);
    }

    @GetMapping("/question/{questionId}")
    public ResponseEntity<Question> getQuestion(@PathVariable("questionId") Long questionId) {
        Question question = questionService.getQuestion(questionId);
        return ResponseEntity.ok(question);
    }

}