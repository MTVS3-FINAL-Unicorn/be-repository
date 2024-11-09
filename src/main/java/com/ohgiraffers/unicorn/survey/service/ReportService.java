package com.ohgiraffers.unicorn.survey.service;

import com.ohgiraffers.unicorn.openfeign.client.ReportClient;
import com.ohgiraffers.unicorn.openfeign.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReportService {

    @Autowired
    private ReportClient reportClient;

    public String generateOverallReport(Long corpId, Long meetingId) {
        OverallRequestDTO request = new OverallRequestDTO();
        request.setCorpId(corpId);
        request.setMeetingId(meetingId);
        return reportClient.analyzeOverallResponses(request);
    }

    public String analyzeTopic(Long corpId, Long meetingId, Long topicId, Long questionId) {
        TopicRequestDTO request = new TopicRequestDTO();
        request.setCorpId(corpId);
        request.setMeetingId(meetingId);
        request.setTopicId(topicId);
        request.setQuestionId(questionId);
        return reportClient.analyzeTopic(request);
    }

    public String analyzeEmbedding(Long corpId, Long meetingId, Long questionId) {
        EmbeddingRequestDTO request = new EmbeddingRequestDTO();
        request.setCorpId(corpId);
        request.setMeetingId(meetingId);
        request.setQuestionId(questionId);
        return reportClient.analyzeEmbedding(request);
    }

    public String generateWordcloud(Long corpId, Long meetingId, Long questionId) {
        WordcloudRequestDTO request = new WordcloudRequestDTO();
        request.setCorpId(corpId);
        request.setMeetingId(meetingId);
        request.setQuestionId(questionId);
        return reportClient.generateWordcloud(request);
    }

    public String analyzeSentiment(Long corpId, Long meetingId, Long questionId) {
        SentimentRequestDTO request = new SentimentRequestDTO();
        request.setCorpId(corpId);
        request.setMeetingId(meetingId);
        request.setQuestionId(questionId);
        return reportClient.analyzeSentiment(request);
    }
}
