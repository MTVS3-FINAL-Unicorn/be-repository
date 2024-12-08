package com.ohgiraffers.unicorn.report.client;

import com.ohgiraffers.unicorn.survey.dto.EachAnalysisRequestDTO;
import com.ohgiraffers.unicorn.survey.dto.OverallAnalysisRequestDTO;
import com.ohgiraffers.unicorn.survey.dto.TextRequestDTO;
import com.ohgiraffers.unicorn.survey.dto.VoiceRequestDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "reportClient", url = "http://metaai2.iptime.org:8888")
public interface ReportClient {

    @PostMapping("/submit-text")
    String submitTextAnswer(@RequestBody TextRequestDTO request);

    @PostMapping("/submit-voice")
    String submitVoiceAnswer(@RequestBody VoiceRequestDTO request);

    @PostMapping("/analyze-all")
    String analyzeOverallResponses(@RequestBody OverallAnalysisRequestDTO request);

    @GetMapping("/meeting-script")
    String generateScript(@RequestBody OverallAnalysisRequestDTO request);

    @PostMapping("/analyze-topic")
    String analyzeTopic(@RequestBody EachAnalysisRequestDTO request);

    @PostMapping("/analyze-embedding")
    String analyzeEmbedding(@RequestBody EachAnalysisRequestDTO request);

    @PostMapping("/generate-wordcloud")
    String generateWordcloud(@RequestBody EachAnalysisRequestDTO request);

    @PostMapping("/analyze-sentiment")
    String analyzeSentiment(@RequestBody EachAnalysisRequestDTO request);
}

