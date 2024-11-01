package com.ohgiraffers.unicorn.ad.controller;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.ohgiraffers.unicorn.ad.entity.Ad;
import com.ohgiraffers.unicorn.ad.service.AdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/ad")
public class AdController {

    @Autowired
    private AdService adService;

    @Autowired
    private AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    // 하드코딩된 corpId (로그인 미구현 상태)
    private final Long corpId = 123L;

    @PostMapping
    public ResponseEntity<Ad> createAd(
            @RequestParam("file") MultipartFile file,
            @RequestParam("type") String type,
            @RequestParam("description") String description) {
        try {
            String fileName = file.getOriginalFilename();
            String fileUrl = "https://" + bucket + ".s3.amazonaws.com/" + fileName;

            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType(type);
            metadata.setContentLength(file.getSize());

            amazonS3Client.putObject(bucket, fileName, file.getInputStream(), metadata);

            Ad ad = adService.createAd(corpId, fileUrl, type, description);
            return ResponseEntity.ok(ad);
        } catch (IOException e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @GetMapping
    public ResponseEntity<Ad> getAd() {
        Ad ad = adService.getAds(corpId, 1);
        return ResponseEntity.ok(ad);
    }
}
