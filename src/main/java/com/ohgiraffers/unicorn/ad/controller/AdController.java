package com.ohgiraffers.unicorn.ad.controller;

import com.ohgiraffers.unicorn.ad.entity.Ad;
import com.ohgiraffers.unicorn.ad.service.AdService;
import com.ohgiraffers.unicorn._core.utils.ApiUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static com.ohgiraffers.unicorn._core.utils.SecurityUtils.getCurrentUserId;

@RestController
@RequestMapping("/api/v1/ad")
public class AdController {

    @Autowired
    private AdService adService;

    @PostMapping("/create")
    public ResponseEntity<?> createAd(
            @RequestParam("file") MultipartFile file,
            @RequestParam("description") String description) {
        try {
            // 현재 사용자 ID 가져오기
            Long corpId = getCurrentUserId();
            System.out.println("corpId = " + corpId);
            String fileUrl = adService.uploadImageToS3(file, "ad/" + corpId);

            // 광고 생성 또는 업데이트
            CompletableFuture<Ad> futureAd = adService.createAd(corpId, description, fileUrl);

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
        Ad ad = adService.findByAdId(adId);
        return ResponseEntity.ok(ad);
    }
}
