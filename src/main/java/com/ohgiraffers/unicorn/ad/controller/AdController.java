package com.ohgiraffers.unicorn.ad.controller;

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

    // 하드코딩된 corpId (로그인 미구현 상태)
    private final Long corpId = 123L;

    @PostMapping
    public ResponseEntity<Ad> createAd(
            @RequestParam("file") MultipartFile file,
            @RequestParam("type") String type,
            @RequestParam("description") String description) {
        try {
            String fileUrl = adService.uploadFileToS3(file, "ad");

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
