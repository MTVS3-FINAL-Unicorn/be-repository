package com.ohgiraffers.unicorn.openfeign.client;

import com.ohgiraffers.unicorn.openfeign.dto.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "aiClient", url = "http://metaai2.iptime.org:8888")
public interface ReportClient {

    @PostMapping("/analyze-overall-responses")
    String analyzeOverallResponses(@RequestBody OverallRequestDTO overallRequest);

    @PostMapping("/analyze-topic")
    String analyzeTopic(@RequestBody TopicRequestDTO topicRequest);

    @PostMapping("/analyze-embedding")
    String analyzeEmbedding(@RequestBody EmbeddingRequestDTO embeddingRequest);

    @PostMapping("/generate-wordcloud")
    String generateWordcloud(@RequestBody WordcloudRequestDTO wordcloudRequest);

    @PostMapping("/analyze-sentiment")
    String analyzeSentiment(@RequestBody SentimentRequestDTO sentimentRequest);
}

