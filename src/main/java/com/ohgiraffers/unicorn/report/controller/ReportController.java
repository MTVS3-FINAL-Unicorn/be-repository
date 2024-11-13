package com.ohgiraffers.unicorn.report.controller;

import com.ohgiraffers.unicorn.report.entity.Report;
import com.ohgiraffers.unicorn.report.service.ReportService;
import com.ohgiraffers.unicorn.survey.entity.Answer;
import com.ohgiraffers.unicorn.survey.repository.AnswerRepository;
import com.ohgiraffers.unicorn.survey.repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.ohgiraffers.unicorn.utils.SecurityUtils.getCurrentUserId;

@RestController
@RequestMapping("/api/v1/report")
public class ReportController {

    @Autowired
    private ReportService reportService;

    @Autowired
    private AnswerRepository answerRepository;
    @Autowired
    private QuestionRepository questionRepository;

    @GetMapping("/{meetingId}")
    public ResponseEntity<List<Report>> getReportsByMeetingId(@PathVariable Long meetingId) {
        List<Report> reports = reportService.getReportsByMeetingId(meetingId);
        return ResponseEntity.ok(reports);
    }

    @GetMapping("/{questionId}")
    public ResponseEntity<List<Report>> getReportsByQuestionId(@PathVariable Long questionId) {
        List<Report> reports = reportService.getReportsByMeetingId(questionId);
        return ResponseEntity.ok(reports);
    }

    @PostMapping("/analyze/whole/{meetingId}")
    public ResponseEntity<?> completeSurvey(@PathVariable("meetingId") Long meetingId) {
        // 전체 설문이 완료된 경우 AI 서버에 분석 요청
        String overallReport = reportService.generateOverallReport(getCurrentUserId() ,meetingId);
        return ResponseEntity.ok(overallReport);
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

