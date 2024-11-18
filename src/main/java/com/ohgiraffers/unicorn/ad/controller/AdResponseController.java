package com.ohgiraffers.unicorn.ad.controller;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.ohgiraffers.unicorn.ad.dto.AdResponseDTO;
import com.ohgiraffers.unicorn.ad.entity.Ad;
import com.ohgiraffers.unicorn.ad.repository.AdRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/ad/generated")
public class AdResponseController {

    @Autowired
    private AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;
    @Autowired
    private AdRepository adRepository;

    @PostMapping
    public ResponseEntity<String> handleAdVideoUpload(
            @RequestParam("corpId") Long corpId,
            @RequestParam("advertiseVideo") MultipartFile advertiseVideo) {
        try {
            String s3Url = uploadFileToS3(corpId, advertiseVideo);
            return ResponseEntity.ok(s3Url);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to upload video to S3: " + e.getMessage());
        }
    }

    public String uploadFileToS3(Long corpId, MultipartFile advertiseVideo) throws IOException {
        String folderName = "ad/" + corpId;
        String fileName = folderName + "/video_" + advertiseVideo.getOriginalFilename();
        String fileUrl = "https://" + bucket + ".s3.ap-northeast-2.amazonaws.com/" + fileName;

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(advertiseVideo.getContentType());
        metadata.setContentLength(advertiseVideo.getSize());

        // S3에 파일 업로드
        amazonS3Client.putObject(bucket, fileName, advertiseVideo.getInputStream(), metadata);

        // 광고 데이터 업데이트
        adRepository.findByCorpId(corpId).ifPresent(ad -> {
            ad.setAdVideoUrl(fileUrl); // adVideoUrl 업데이트
            adRepository.save(ad); // 변경 사항 저장
        });

        return fileUrl;
    }
}

