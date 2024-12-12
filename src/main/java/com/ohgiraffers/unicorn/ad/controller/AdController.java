package com.ohgiraffers.unicorn.ad.controller;

import com.ohgiraffers.unicorn.ad.entity.Ad;
import com.ohgiraffers.unicorn.ad.service.AdService;
import com.ohgiraffers.unicorn._core.utils.ApiUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
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
            Long corpId = getCurrentUserId();

            Ad ad = adService.createAdImmediately(corpId, description);

            String fileUrl = adService.uploadImageToS3(file, "ad/" + ad.getAdId());
            adService.updateFileUrl(ad.getAdId(), fileUrl);

            adService.updatePreviewAndVideo(ad.getAdId(), description, fileUrl);

            return ResponseEntity.ok().body(ApiUtils.success(ad));
        } catch (IOException e) {
            return ResponseEntity.ok().body(ApiUtils.error(e.getMessage()));
        } catch (Exception e) {
            throw new RuntimeException("광고 생성 중 오류 발생", e);
        }
    }


    @GetMapping("/{adId}")
    public ResponseEntity<Ad> getAd(@PathVariable("adId") Long adId) {
        Ad ad = adService.findByAdId(adId);
        return ResponseEntity.ok(ad);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Ad>> getAllAds() {
        Long corpId = getCurrentUserId();
        List<Ad> ads = adService.findAllAdsByCorpId(corpId);
        return ResponseEntity.ok(ads);
    }

}
