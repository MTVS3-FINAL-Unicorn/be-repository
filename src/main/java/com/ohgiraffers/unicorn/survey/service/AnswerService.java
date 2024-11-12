package com.ohgiraffers.unicorn.survey.service;

import com.ohgiraffers.unicorn.survey.entity.Answer;
import com.ohgiraffers.unicorn.survey.repository.AnswerRepository;
import com.ohgiraffers.unicorn.survey.repository.QuestionRepository;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Base64;

@Service
public class AnswerService {

    @Autowired
    private AnswerRepository answerRepository;
    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private ReportService reportService;

    public Answer saveAnswer(Long questionId, Long indivId, String content) {
        Answer answer = new Answer();
        answer.setMeetingId(questionRepository.findById(questionId).get().getMeetingId());
        answer.setQuestionId(questionId);
        answer.setIndivId(indivId);
        answer.setContent(content);
        return answerRepository.save(answer);
    }

    public Answer handleVoiceResponse(Long questionId, Long indivId, byte[] audioData) throws JSONException {
        String encodedAudio = Base64.getEncoder().encodeToString(audioData);

        Answer answer = new Answer(questionId, indivId, encodedAudio);

        String aiResponse = reportService.submitVoiceAnswerToAI(answer, encodedAudio);

        JSONObject jsonResponse = new JSONObject(aiResponse);
        String content = jsonResponse.optString("data", "");

        answer = saveAnswer(questionId, indivId, content);

        return answer;
    }

    public Answer handlePreferenceResponse(Long questionId, Long indivId, String selectedOption) {
        Answer answer = saveAnswer(questionId, indivId, selectedOption);
        return answer;
    }
}
