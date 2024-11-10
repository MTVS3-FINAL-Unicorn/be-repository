package com.ohgiraffers.unicorn.survey.controller;

import com.ohgiraffers.unicorn.survey.dto.AnswerDTO;
import com.ohgiraffers.unicorn.survey.dto.PreferenceAnswerDTO;
import com.ohgiraffers.unicorn.survey.dto.QuestionDTO;
import com.ohgiraffers.unicorn.survey.entity.Answer;
import com.ohgiraffers.unicorn.survey.entity.Question;
import com.ohgiraffers.unicorn.survey.service.AnswerService;
import com.ohgiraffers.unicorn.survey.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

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

    @PostMapping(value = "/question/preference", consumes = "multipart/form-data")
    public ResponseEntity<Question> createPreferenceQuestion(
            @ModelAttribute QuestionDTO questionRequest,
            @RequestParam(name = "images", required = false) List<MultipartFile> images) throws IOException {

        Question question = questionService.createPreferenceQuestion(
                questionRequest.getMeetingId(),
                questionRequest.getContent(),
                questionRequest.getType(),
                images);

        return ResponseEntity.ok(question);
    }

    @GetMapping("/question/{questionId}")
    public ResponseEntity<Question> getQuestion(@PathVariable("questionId") Long questionId) {
        Question question = questionService.getQuestion(questionId);
        return ResponseEntity.ok(question);
    }

    @PostMapping("/answers/text")
    public ResponseEntity<Answer> saveTextAnswer(
            @RequestBody AnswerDTO answerRequest) {
        Long indivId = getCurrentUserId();
        Answer answer = answerService.saveAnswer(
                answerRequest.getQuestionId(),
                indivId,
                answerRequest.getContent());
        return ResponseEntity.ok(answer);
    }

    @PostMapping(value = "/answers/voice", consumes = "multipart/form-data")
    public ResponseEntity<?> saveVoiceAnswer(
            @RequestParam("questionId") Long questionId,
            @RequestParam("audioFile") MultipartFile audioFile) throws IOException {
        Long indivId = getCurrentUserId();
        byte[] audioData = audioFile.getBytes();
        Answer answer = answerService.handleVoiceResponse(questionId, indivId, audioData);
        return ResponseEntity.ok(answer);
    }

    @PostMapping("/answers/preference")
    public ResponseEntity<?> savePreferenceAnswer(
            @RequestBody PreferenceAnswerDTO preferenceAnswerDTO) {
        Long indivId = getCurrentUserId();
        Answer answer = answerService.handlePreferenceResponse(preferenceAnswerDTO.getQuestionId(), indivId, preferenceAnswerDTO.getSelectedOption());
        return ResponseEntity.ok(answer);
    }

}