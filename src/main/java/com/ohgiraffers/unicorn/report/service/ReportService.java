package com.ohgiraffers.unicorn.report.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ohgiraffers.unicorn.meeting.repository.MeetingRepository;
import com.ohgiraffers.unicorn.report.entity.Report;
import com.ohgiraffers.unicorn.report.entity.AnalysisType;
import com.ohgiraffers.unicorn.report.repository.ReportRepository;
import com.ohgiraffers.unicorn.report.client.ReportClient;
import com.ohgiraffers.unicorn.survey.dto.EachAnalysisRequestDTO;
import com.ohgiraffers.unicorn.survey.dto.OverallAnalysisRequestDTO;
import com.ohgiraffers.unicorn.survey.dto.TextRequestDTO;
import com.ohgiraffers.unicorn.survey.dto.VoiceRequestDTO;
import com.ohgiraffers.unicorn.survey.entity.Answer;
import com.ohgiraffers.unicorn.survey.entity.Question;
import com.ohgiraffers.unicorn.survey.repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReportService {

    @Autowired
    private ReportClient reportClient;
    @Autowired
    private ReportRepository reportRepository;
    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private MeetingRepository meetingRepository;
    @Autowired
    private ObjectMapper objectMapper;

    public List<Report> getReportsByMeetingId(Long meetingId) {
        return reportRepository.findByMeetingId(meetingId);
    }

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

        // AI 서버로부터 전체 분석 결과 JSON을 받아옵니다.
        String overallAnalysisJson = reportClient.analyzeOverallResponses(request);

        return processOverallReport(meetingId, overallAnalysisJson);
    }

    public String processOverallReport(Long meetingId, String overallAnalysisJson) {
        try {
            // 전체 분석 JSON 파싱
            JsonNode root = objectMapper.readTree(overallAnalysisJson);

            // 1. topic_result 저장
            JsonNode topicResult = root.path("topic_result");
            saveWholeReport(meetingId, AnalysisType.topicAnalysis, topicResult.toString());

            // 2. wordcloud 파일 이름 저장
            String wordcloudFilename = root.path("wordcloud_filename").asText();
            saveWholeReport(meetingId, AnalysisType.wordcloud, wordcloudFilename);

            // 3. sentiment_result 저장
            JsonNode sentimentResult = root.path("sentiment_result");
            saveWholeReport(meetingId, AnalysisType.sentimentAnalysis, sentimentResult.toString());

            return "모든 분석이 성공적으로 저장되었습니다.";

        } catch (IOException e) {
            e.printStackTrace();
            return "분석 저장 중 오류가 발생했습니다.";
        }
    }

    private void saveWholeReport(Long meetingId, AnalysisType analysisType, String analysisResult) {
        Report report = new Report(meetingId, null, analysisType, analysisResult);
        reportRepository.save(report);
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

    public String analyzeTopic(Long meetingId, Long questionId, List<Answer> answers) {
        EachAnalysisRequestDTO request = buildEachAnalysisRequestDTO(answers);
        String response = reportClient.analyzeTopic(request);

        try {
            JsonNode root = objectMapper.readTree(response);
            JsonNode topicResult = root.path("topic_result");
            saveReportByQuestion(meetingId, questionId, AnalysisType.topicAnalysis, topicResult.toString());
        } catch (IOException e) {
            e.printStackTrace();
            return "토픽 분석 결과 저장 중 오류가 발생했습니다.";
        }

        return "토픽 분석이 성공적으로 저장되었습니다.";
    }

    public String analyzeEmbedding(Long meetingId, Long questionId, List<Answer> answers) {
        EachAnalysisRequestDTO request = buildEachAnalysisRequestDTO(answers);
        String response = reportClient.analyzeEmbedding(request);

        try {
            JsonNode root = objectMapper.readTree(response);
            String tensorboardUrl = root.path("tensorboard_url").asText();
            saveReportByQuestion(meetingId, questionId, AnalysisType.embeddingAnalysis, tensorboardUrl);
        } catch (IOException e) {
            e.printStackTrace();
            return "임베딩 분석 결과 저장 중 오류가 발생했습니다.";
        }

        return "임베딩 분석이 성공적으로 저장되었습니다.";
    }

    public String generateWordcloud(Long meetingId, Long questionId, List<Answer> answers) {
        EachAnalysisRequestDTO request = buildEachAnalysisRequestDTO(answers);
        String response = reportClient.generateWordcloud(request);

        try {
            JsonNode root = objectMapper.readTree(response);
            String wordcloudFilename = root.path("wordcloud_filename").asText();
            saveReportByQuestion(meetingId, questionId, AnalysisType.wordcloud, wordcloudFilename);
        } catch (IOException e) {
            e.printStackTrace();
            return "워드클라우드 생성 결과 저장 중 오류가 발생했습니다.";
        }

        return "워드클라우드가 성공적으로 저장되었습니다.";
    }

    public String analyzeSentiment(Long meetingId, Long questionId, List<Answer> answers) {
        EachAnalysisRequestDTO request = buildEachAnalysisRequestDTO(answers);
        request.setMostCommonK(10);
        String response = reportClient.analyzeSentiment(request);

        try {
            JsonNode root = objectMapper.readTree(response);
            JsonNode sentimentResult = root.path("sentiment_result");
            saveReportByQuestion(meetingId, questionId, AnalysisType.sentimentAnalysis, sentimentResult.toString());
        } catch (IOException e) {
            e.printStackTrace();
            return "감정 분석 결과 저장 중 오류가 발생했습니다.";
        }

        return "감정 분석이 성공적으로 저장되었습니다.";
    }

    public Report saveReportByQuestion(Long meetingId, Long questionId, AnalysisType analysisType, String analysisResult) {
        Report report = new Report(meetingId, questionId, analysisType, analysisResult);
        return reportRepository.save(report);
    }
}
