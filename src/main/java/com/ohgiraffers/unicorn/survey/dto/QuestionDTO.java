package com.ohgiraffers.unicorn.survey.dto;

import com.ohgiraffers.unicorn.survey.entity.QuestionType;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class QuestionDTO {

    private Long questionId;

    private Long meetingId;
    private String content;

    private QuestionType type;

}
