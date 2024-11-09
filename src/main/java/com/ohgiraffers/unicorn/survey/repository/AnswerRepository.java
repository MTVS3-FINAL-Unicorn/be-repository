package com.ohgiraffers.unicorn.survey.repository;

import com.ohgiraffers.unicorn.survey.entity.Answer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AnswerRepository extends JpaRepository<Answer, Long> {
    List<Answer> findByQuestionId(Long questionId);
}

