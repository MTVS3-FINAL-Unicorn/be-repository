package com.ohgiraffers.unicorn.ad.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class AdResponseDTO {
    MultipartFile advertiseVideo;
    Long corpId;
}
