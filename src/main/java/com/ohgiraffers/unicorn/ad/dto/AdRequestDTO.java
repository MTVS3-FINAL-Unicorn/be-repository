package com.ohgiraffers.unicorn.ad.dto;

public class AdRequestDTO {
    private String prompt;
    private Long corpId;
    private String ratioType;
    private String imageFile;

    public AdRequestDTO(String description, Long corpId, String type, String fileUrl) {
        this.prompt = description;
        this.corpId = corpId;
        this.ratioType = type;
        this.imageFile = fileUrl;
    }
}
