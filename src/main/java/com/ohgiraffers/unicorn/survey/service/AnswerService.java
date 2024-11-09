package com.ohgiraffers.unicorn.survey.service;

import com.ohgiraffers.unicorn.survey.entity.Answer;
import com.ohgiraffers.unicorn.survey.repository.AnswerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AnswerService {

    @Autowired
    private AnswerRepository answerRepository;

    public Answer saveAnswer(Long questionId, Long indivId, String content) {
        Answer answer = new Answer();
        answer.setQuestionId(questionId);
        answer.setIndivId(indivId);
        answer.setContent(content);
        return answerRepository.save(answer);
    }

    public void handleVoiceResponse(Long questionId, Long userId, byte[] audioData) {
        String textContent = convertAudioToText(audioData);
        saveAnswer(questionId, userId, textContent);
    }

    private String convertAudioToText(byte[] audioData) {
        return "Converted text from audio";
    }
}
