package com.ohgiraffers.unicorn.survey.service;

import com.ohgiraffers.unicorn.survey.client.ReportClient;
import com.ohgiraffers.unicorn.survey.dto.EachAnalysisRequestDTO;
import com.ohgiraffers.unicorn.survey.dto.OverallAnalysisRequestDTO;
import com.ohgiraffers.unicorn.survey.dto.TextRequestDTO;
import com.ohgiraffers.unicorn.survey.dto.VoiceRequestDTO;
import com.ohgiraffers.unicorn.survey.entity.Answer;
import com.ohgiraffers.unicorn.survey.repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReportService {

    @Autowired
    private ReportClient reportClient;

    @Autowired
    private QuestionRepository questionRepository;

    public void submitTextAnswerToAI(Answer answer) {
        TextRequestDTO request = new TextRequestDTO(answer.getQuestionId(), answer.getIndivId(),answer.getContent());
        reportClient.submitTextAnswer(request);
    }

    public void submitVoiceAnswerToAI(Answer answer, byte[] audioData) {
        VoiceRequestDTO request = new VoiceRequestDTO(answer.getQuestionId(), answer.getIndivId(), audioData);
        reportClient.submitVoiceAnswer(request);
    }

    public String generateOverallReport(Long corpId, Long meetingId) {
        OverallAnalysisRequestDTO request = new OverallAnalysisRequestDTO(corpId, meetingId);
        System.out.println("Request DTO: " + request);
        return reportClient.analyzeOverallResponses(request);
    }

    private EachAnalysisRequestDTO buildEachAnalysisRequestDTO(List<Answer> answers) {
        List<EachAnalysisRequestDTO.Response> responses = answers.stream()
                .map(answer -> new EachAnalysisRequestDTO.Response(
                        answer.getIndivId(),
                        answer.getMeetingId(),
                        answer.getQuestionId(),
                        answer.getContent()))
                .collect(Collectors.toList());
        return new EachAnalysisRequestDTO(responses);
    }

    public String analyzeTopic(List<Answer> answers) {
        EachAnalysisRequestDTO request = buildEachAnalysisRequestDTO(answers);
        return reportClient.analyzeTopic(request);
    }

    public String analyzeEmbedding(List<Answer> answers) {
        EachAnalysisRequestDTO request = buildEachAnalysisRequestDTO(answers);
        return reportClient.analyzeEmbedding(request);
    }

    public String generateWordcloud(List<Answer> answers) {
        EachAnalysisRequestDTO request = buildEachAnalysisRequestDTO(answers);
        return reportClient.generateWordcloud(request);
    }

    public String analyzeSentiment(List<Answer> answers) {
        EachAnalysisRequestDTO request = buildEachAnalysisRequestDTO(answers);
        request.setMostCommonK(10);
        return reportClient.analyzeSentiment(request);
    }
}
