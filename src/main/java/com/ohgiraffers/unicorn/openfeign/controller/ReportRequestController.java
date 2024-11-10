package com.ohgiraffers.unicorn.openfeign.controller;

import com.ohgiraffers.unicorn.openfeign.dto.*;
import com.ohgiraffers.unicorn.survey.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/reports")
public class ReportRequestController {

    @Autowired
    private ReportService reportService;

    @PostMapping("/generate-overall")
    public ResponseEntity<String> generateOverallReport(@RequestBody OverallRequestDTO request) {
        String result = reportService.generateOverallReport(request.getCorpId(), request.getMeetingId());
        return ResponseEntity.ok(result);
    }

    @PostMapping("/analyze-topic")
    public ResponseEntity<String> analyzeTopic(@RequestBody TopicRequestDTO request) {
        String result = reportService.analyzeTopic(request.getCorpId(), request.getMeetingId(), request.getTopicId(), request.getQuestionId());
        return ResponseEntity.ok(result);
    }

    @PostMapping("/analyze-embedding")
    public ResponseEntity<String> analyzeEmbedding(@RequestBody EmbeddingRequestDTO request) {
        String result = reportService.analyzeEmbedding(request.getCorpId(), request.getMeetingId(), request.getQuestionId());
        return ResponseEntity.ok(result);
    }

    @PostMapping("/generate-wordcloud")
    public ResponseEntity<String> generateWordcloud(@RequestBody WordcloudRequestDTO request) {
        String result = reportService.generateWordcloud(request.getCorpId(), request.getMeetingId(), request.getQuestionId());
        return ResponseEntity.ok(result);
    }

    @PostMapping("/analyze-sentiment")
    public ResponseEntity<String> analyzeSentiment(@RequestBody SentimentRequestDTO request) {
        String result = reportService.analyzeSentiment(request.getCorpId(), request.getMeetingId(), request.getQuestionId());
        return ResponseEntity.ok(result);
    }
}
