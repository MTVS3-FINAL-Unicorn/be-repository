package com.ohgiraffers.unicorn.ad.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.ohgiraffers.unicorn.ad.client.AdClient;
import com.ohgiraffers.unicorn.ad.dto.AdRequestDTO;
import com.ohgiraffers.unicorn.ad.entity.Ad;
import com.ohgiraffers.unicorn.ad.repository.AdRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Service
public class AdService {

    private final Logger logger = LoggerFactory.getLogger(AdService.class);

    @Autowired
    private AdRepository adRepository;

    @Autowired
    private AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Autowired
    private AdClient adClient;

    @Async
    public CompletableFuture<Ad> createAd(Long corpId, String description, String fileUrl) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                Ad ad = new Ad(corpId, fileUrl, description);
                AdRequestDTO requestDTO = new AdRequestDTO(description, corpId, fileUrl);

                // 광고 생성 요청 (AI 서버로 요청 전송)
                String response = adClient.generateVideoAd(requestDTO);
                ad.setAdVideoUrl(response);

                // 광고 저장
                Ad savedAd = adRepository.save(ad);
                logger.info("Ad created for corpId {} with adId {}", corpId, savedAd.getAdId());

                return savedAd;
            } catch (Exception e) {
                logger.error("광고 생성 중 오류 발생: {}", e.getMessage());
                throw new RuntimeException("광고 생성 실패", e);
            }
        });
    }

    public void updatePreviewUrl(Long adId, String previewUrl) {
        Ad ad = adRepository.findByAdId(adId)
                .orElseThrow(() -> new IllegalArgumentException("Ad not found"));
        ad.setPreviewUrl(previewUrl);
        adRepository.save(ad);
    }

    public void updateAdVideoUrl(Long adId, String adVideoUrl) {
        Ad ad = adRepository.findByAdId(adId)
                .orElseThrow(() -> new IllegalArgumentException("Ad not found"));
        ad.setAdVideoUrl(adVideoUrl);
        adRepository.save(ad);
    }

    // S3에 파일 업로드 메서드
    public String uploadImageToS3(MultipartFile file, String folderName) throws IOException {
        String fileName = folderName + "/originalImage_" + file.getOriginalFilename();
        String fileUrl = "https://" + bucket + ".s3.ap-northeast-2.amazonaws.com/" + fileName;

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(file.getContentType());
        metadata.setContentLength(file.getSize());

        amazonS3Client.putObject(bucket, fileName, file.getInputStream(), metadata);
        logger.info("File uploaded to S3: {}", fileUrl);

        return fileUrl;
    }

    public Ad findByAdId(Long adId) {
        return adRepository.findByAdId(adId)
                .orElseThrow(() -> new IllegalArgumentException("요청하신 광고를 찾을 수 없습니다."));
    }
}
