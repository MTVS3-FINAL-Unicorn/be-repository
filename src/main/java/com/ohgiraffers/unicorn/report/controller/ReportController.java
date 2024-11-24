package com.ohgiraffers.unicorn.report.controller;

import com.ohgiraffers.unicorn.report.entity.Report;
import com.ohgiraffers.unicorn.report.service.ReportService;
import com.ohgiraffers.unicorn.survey.entity.Answer;
import com.ohgiraffers.unicorn.survey.entity.Question;
import com.ohgiraffers.unicorn.survey.repository.AnswerRepository;
import com.ohgiraffers.unicorn.survey.repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.ohgiraffers.unicorn._core.utils.SecurityUtils.getCurrentUserId;

@RestController
@RequestMapping("/api/v1/report")
public class ReportController {

    @Autowired
    private ReportService reportService;

    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @GetMapping("/get-questions/{meetingId}")
    public ResponseEntity<List<Question>> getQuestionsByMeetingId(@PathVariable("meetingId") Long meetingId) {
        List<Question> questions = questionRepository.findByMeetingId(meetingId);
        return ResponseEntity.ok(questions);
    }

    @GetMapping("/{meetingId}")
    public ResponseEntity<List<Report>> getReportsByMeetingId(@PathVariable("meetingId") Long meetingId) {
        List<Report> reports = reportService.getReportsByMeetingId(meetingId);
        return ResponseEntity.ok(reports);
    }

    @GetMapping("/question/{questionId}")
    public ResponseEntity<List<Report>> getReportsByQuestionId(@PathVariable("questionId") Long questionId) {
        List<Report> reports = reportService.getReportsByQuestionId(questionId);
        return ResponseEntity.ok(reports);
    }

    @GetMapping("/answer/preference/{questionId}")
    public ResponseEntity<List<Answer>> getPreferenceAnswersByQuestionId(@PathVariable("questionId") Long questionId) {
        List<Answer> answers = answerRepository.findByQuestionId(questionId);
        return ResponseEntity.ok(answers);
    }

    @PostMapping("/analyze/whole/{meetingId}")
    public ResponseEntity<?> completeSurvey(@PathVariable("meetingId") Long meetingId) {
        String overallReport = reportService.generateOverallReport(getCurrentUserId(), meetingId);
        return ResponseEntity.ok(overallReport);
    }

    @PostMapping("/generate-script/{meetingId}")
    public ResponseEntity<?> generateScript(@PathVariable("meetingId") Long meetingId) {
        String meetingScript = reportService.generateScript(meetingId, getCurrentUserId());
        return ResponseEntity.ok(meetingScript);
    }

    @PostMapping("/analyze/{meetingId}/{questionId}")
    public ResponseEntity<?> analyzeQuestionResponses(
            @PathVariable("meetingId") Long meetingId,
            @PathVariable("questionId") Long questionId) {

        List<Answer> answers = answerRepository.findByQuestionId(questionId);

        // 특정 문항에 대한 개별 분석 요청 (토픽, 임베딩 벡터, 워드클라우드, 감정 분석)
        String topicAnalysis = reportService.analyzeTopic(meetingId, questionId, answers);
        String embeddingAnalysis = reportService.analyzeEmbedding(meetingId, questionId, answers);
        String wordcloud = reportService.generateWordcloud(meetingId, questionId, answers);
        String sentimentAnalysis = reportService.analyzeSentiment(meetingId, questionId, answers);

        return ResponseEntity.ok(
                "Topic: " + topicAnalysis
                        + ", \n Embedding: " + embeddingAnalysis +
                        ", \n Wordcloud: " + wordcloud +
                        ", \n Sentiment: " + sentimentAnalysis
        );
    }

}

