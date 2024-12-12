package com.ohgiraffers.unicorn.ad.dto;

import lombok.Data;

@Data
public class AdRequestDTO {
    private String image;
    private String prompt;
    private Long adId;

    public AdRequestDTO(String description, Long adId, String fileUrl) {
        this.prompt = description;
        this.adId = adId;
        this.image = fileUrl;
    }
}
