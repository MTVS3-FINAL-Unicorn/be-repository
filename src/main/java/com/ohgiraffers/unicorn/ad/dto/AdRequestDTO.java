package com.ohgiraffers.unicorn.ad.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdRequestDTO {
    private String prompt;
    private Long corpId;
    private String ratioType;
    private MultipartFile image;
}
