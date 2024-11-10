package com.ohgiraffers.unicorn.openfeign.dto;

import lombok.Data;

@Data
public class SentimentRequestDTO {
    private Long corpId;
    private Long meetingId;
    private Long questionId;
}
