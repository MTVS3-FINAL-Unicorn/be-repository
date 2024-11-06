package com.ohgiraffers.unicorn.ad.controller;

import com.ohgiraffers.unicorn.ad.entity.Ad;
import com.ohgiraffers.unicorn.ad.service.AdService;
import com.ohgiraffers.unicorn.utils.ApiUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static com.ohgiraffers.unicorn.utils.SecurityUtils.getCurrentUserId;

@RestController
@RequestMapping("/api/v1/ad")
public class AdController {

    @Autowired
    private AdService adService;

    // 하드코딩된 corpId (로그인 미구현 상태)
    private final Long corpId = 1L;

    @PostMapping
    public ResponseEntity<?> createOrUpdateAd(
            @RequestParam("file") MultipartFile file,
            @RequestParam("type") String type,
            @RequestParam("description") String description) {
        try {

            String fileUrl = adService.uploadFileToS3(file, "ad");
            adService.createOrUpdateAd(getCurrentUserId(), fileUrl, type, description);

            return ResponseEntity.ok().body(ApiUtils.success(null));
        } catch (IOException e) {
            return ResponseEntity.ok().body(ApiUtils.error(e.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<Ad> getAd() {
        Ad ad = adService.getAds(corpId, 1);
        return ResponseEntity.ok(ad);
    }
}
