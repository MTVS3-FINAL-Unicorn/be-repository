package com.ohgiraffers.unicorn.survey.controller;

import com.ohgiraffers.unicorn.survey.dto.QuestionDTO;
import com.ohgiraffers.unicorn.survey.entity.Answer;
import com.ohgiraffers.unicorn.survey.entity.Question;
import com.ohgiraffers.unicorn.survey.service.AnswerService;
import com.ohgiraffers.unicorn.survey.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.ohgiraffers.unicorn.utils.SecurityUtils.getCurrentUserId;

@RestController
@RequestMapping("/api/v1/survey")
public class SurveyController {

    @Autowired
    private QuestionService questionService;

    @Autowired
    private AnswerService answerService;

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

    @PostMapping("/answers/text")
    public ResponseEntity<Answer> saveTextAnswer(
            @RequestParam Long questionId,
            @RequestParam String content) {
        Long indivId = getCurrentUserId();
        Answer answer = answerService.saveAnswer(questionId, indivId, content);
        return ResponseEntity.ok(answer);
    }
}