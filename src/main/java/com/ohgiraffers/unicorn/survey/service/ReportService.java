package com.ohgiraffers.unicorn.survey.service;

import com.ohgiraffers.unicorn.meeting.repository.MeetingRepository;
import com.ohgiraffers.unicorn.survey.client.ReportClient;
import com.ohgiraffers.unicorn.survey.dto.EachAnalysisRequestDTO;
import com.ohgiraffers.unicorn.survey.dto.OverallAnalysisRequestDTO;
import com.ohgiraffers.unicorn.survey.dto.TextRequestDTO;
import com.ohgiraffers.unicorn.survey.dto.VoiceRequestDTO;
import com.ohgiraffers.unicorn.survey.entity.Answer;
import com.ohgiraffers.unicorn.survey.entity.Question;
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
    @Autowired
    private MeetingRepository meetingRepository;

    public void submitTextAnswerToAI(Answer answer) {
        Question question = questionRepository.findById(answer.getQuestionId()).orElse(null);
        Long corpId = meetingRepository.findById(question.getMeetingId()).get().getCorpId();

        TextRequestDTO request = new TextRequestDTO(question.getContent(), answer.getContent(), answer.getIndivId(), question.getMeetingId(), corpId, answer.getQuestionId());
        reportClient.submitTextAnswer(request);
    }

    public String submitVoiceAnswerToAI(Answer answer, String encodedAudio) {
        Question question = questionRepository.findById(answer.getQuestionId()).orElse(null);
        Long corpId = meetingRepository.findById(question.getMeetingId()).get().getCorpId();

        VoiceRequestDTO request = new VoiceRequestDTO(question.getContent(), encodedAudio, answer.getIndivId(), question.getMeetingId(), corpId, answer.getQuestionId());
        return reportClient.submitVoiceAnswer(request);
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
