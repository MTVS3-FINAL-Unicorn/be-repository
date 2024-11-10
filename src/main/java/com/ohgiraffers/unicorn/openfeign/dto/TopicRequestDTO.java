package com.ohgiraffers.unicorn.openfeign.dto;

import lombok.Data;

@Data
public class TopicRequestDTO {
    private Long corpId;
    private Long meetingId;
    private Long topicId;
    private Long questionId;
}
