package com.ohgiraffers.unicorn.ad.controller;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.ohgiraffers.unicorn.ad.dto.AdResponseDTO;
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

    @PostMapping
    public ResponseEntity<String> handleAdVideoUpload(
            @RequestBody AdResponseDTO adResponseDTO) {
        try {
            String s3Url = uploadFileToS3(adResponseDTO);
            return ResponseEntity.ok(s3Url);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to upload image to S3: " + e.getMessage());
        }
    }

    public String uploadFileToS3(AdResponseDTO adResponseDTO) throws IOException {
        String folderName = "ad/" + adResponseDTO.getCorpId();
        String fileName = folderName + "/" + adResponseDTO.getAdvertiseVideo().getOriginalFilename();
        String fileUrl = "https://" + bucket + ".s3.ap-northeast-2.amazonaws.com/" + fileName;

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(adResponseDTO.getAdvertiseVideo().getContentType());
        metadata.setContentLength(adResponseDTO.getAdvertiseVideo().getSize());

        amazonS3Client.putObject(bucket, fileName, adResponseDTO.getAdvertiseVideo().getInputStream(), metadata);
        return fileUrl;
    }

}
