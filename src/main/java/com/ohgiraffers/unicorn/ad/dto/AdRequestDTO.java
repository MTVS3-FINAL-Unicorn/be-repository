package com.ohgiraffers.unicorn.ad.dto;

import lombok.Data;

@Data
public class AdRequestDTO {
    private String image;
    private String prompt;
    private Long corpId;
    private String ratioType;

    public AdRequestDTO(String description, Long corpId, String type, String fileUrl) {
        this.prompt = description;
        this.corpId = corpId;
        this.ratioType = type;
        this.image = fileUrl;
    }
}
