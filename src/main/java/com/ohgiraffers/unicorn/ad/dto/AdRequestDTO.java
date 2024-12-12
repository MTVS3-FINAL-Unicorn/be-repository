package com.ohgiraffers.unicorn.ad.dto;

import lombok.Data;

@Data
public class AdRequestDTO {
    private String image;
    private String prompt;
    private Long corpId;

    public AdRequestDTO(String description, Long corpId, String fileUrl) {
        this.prompt = description;
        this.corpId = corpId;
        this.image = fileUrl;
    }
}
