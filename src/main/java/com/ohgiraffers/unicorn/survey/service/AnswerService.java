package com.ohgiraffers.unicorn.survey.service;

import com.ohgiraffers.unicorn.survey.entity.Answer;
import com.ohgiraffers.unicorn.survey.repository.AnswerRepository;
import com.ohgiraffers.unicorn.survey.repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AnswerService {

    @Autowired
    private AnswerRepository answerRepository;
    @Autowired
    private QuestionRepository questionRepository;

    public Answer saveAnswer(Long questionId, Long indivId, String content) {
        Answer answer = new Answer();
        answer.setMeetingId(questionRepository.findById(questionId).get().getMeetingId());
        answer.setQuestionId(questionId);
        answer.setIndivId(indivId);
        answer.setContent(content);
        return answerRepository.save(answer);
    }

    public Answer handleVoiceResponse(Long questionId, Long indivId, byte[] audioData) {
        String textContent = convertAudioToText(audioData);
        Answer answer = saveAnswer(questionId, indivId, textContent);
        return answer;
    }

    private String convertAudioToText(byte[] audioData) {
        return "Converted text from audio";
    }

    public Answer handlePreferenceResponse(Long questionId, Long indivId, String selectedOption) {
        Answer answer = saveAnswer(questionId, indivId, selectedOption);
        return answer;
    }
}
