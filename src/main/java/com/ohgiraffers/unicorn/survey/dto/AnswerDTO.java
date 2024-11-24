package com.ohgiraffers.unicorn.survey.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class AnswerDTO {

    private Long questionId;
    private Long indivId;
    private String content;

}
