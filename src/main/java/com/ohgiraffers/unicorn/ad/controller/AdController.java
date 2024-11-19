package com.ohgiraffers.unicorn.ad.controller;

import com.ohgiraffers.unicorn.ad.entity.Ad;
import com.ohgiraffers.unicorn.ad.service.AdService;
import com.ohgiraffers.unicorn.utils.ApiUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static com.ohgiraffers.unicorn.utils.SecurityUtils.getCurrentUserId;

@RestController
@RequestMapping("/api/v1/ad")
public class AdController {

    @Autowired
    private AdService adService;

    @PostMapping
    public ResponseEntity<?> createOrUpdateAd(
            @RequestParam("file") MultipartFile file,
            @RequestParam("type") String type,
            @RequestParam("description") String description) {

        try {
            // 현재 사용자 ID 가져오기
            Long corpId = getCurrentUserId();

            Optional<Ad> existingAd = adService.findAdByUserId(corpId);

            // 기존 파일이 존재할 경우 삭제
            if (existingAd.isPresent() && existingAd.get().getFileUrl() != null) {
                adService.deleteFileFromS3(existingAd.get().getFileUrl());
            }

            // 새 파일 업로드
            String fileUrl = adService.uploadImageToS3(file, "ad/" + corpId);

            // 광고 생성 또는 업데이트
            CompletableFuture<Ad> futureAd = adService.createOrUpdateAd(corpId, fileUrl, type, description, 1);

            Ad ad = futureAd.get(); // 비동기 결과를 동기적으로 기다림

            return ResponseEntity.ok().body(ApiUtils.success(ad));
        } catch (IOException e) {
            return ResponseEntity.ok().body(ApiUtils.error(e.getMessage()));
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/{adId}")
    public ResponseEntity<Ad> getAd(@PathVariable("adId") Long adId) {
        Ad ad = adService.findByAdIdAndIsOpened(adId);
        return ResponseEntity.ok(ad);
    }
}
