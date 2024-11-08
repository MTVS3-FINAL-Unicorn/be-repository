package com.ohgiraffers.unicorn.ad.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.ohgiraffers.unicorn.ad.entity.Ad;
import com.ohgiraffers.unicorn.ad.repository.AdRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@Service
public class AdService {

    private final Logger logger = LoggerFactory.getLogger(AdService.class);

    @Autowired
    private AdRepository adRepository;

    @Autowired
    private AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;


    // 광고 생성 또는 업데이트 메서드

    public Ad createOrUpdateAd(Long corpId, String description, String type, String fileUrl, int isOpended) {

        // 기존 광고가 있는지 확인하여 생성 또는 업데이트
        Optional<Ad> existingAd = adRepository.findByCorpId(corpId);

        if (existingAd.isPresent()) {
            Ad ad = existingAd.get();
            ad.setCorpId(corpId);
            ad.setFileUrl(fileUrl); // S3 이미지 파일 URL
            ad.setType(type);
            ad.setDescription(description);
            ad.setIsOpened(ad.getIsOpened());

            return adRepository.save(ad);
        } else {
            Ad ad = new Ad(corpId, fileUrl, type, description, isOpended);
            return adRepository.save(ad);
        }
    }
    public Optional<Ad> findAdByUserId(Long corpId) {
        return adRepository.findByCorpId(corpId);
    }

    // S3에 파일 업로드 메서드
    public String uploadFileToS3(MultipartFile file, String folderName) throws IOException {
        String fileName = folderName + "/" + file.getOriginalFilename(); // ad 폴더 경로 포함
        String fileUrl = "https://" + bucket + ".s3.ap-northeast-2.amazonaws.com/" + fileName;

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(file.getContentType());
        metadata.setContentLength(file.getSize());

        // S3에 파일 업로드
        amazonS3Client.putObject(bucket, fileName, file.getInputStream(), metadata);
        logger.info("File uploaded to S3: {}", fileUrl);

        return fileUrl;
    }

    public void deleteFileFromS3(String fileUrl) {
        String fileKey = fileUrl.replace("https://" + bucket + ".s3.ap-northeast-2.amazonaws.com/", "");

        amazonS3Client.deleteObject(bucket, fileKey);
        logger.info("File deleted from S3: {}", fileUrl);
    }

    public Ad findByAdIdAndIsOpened(Long adId) {
        return adRepository.findByAdIdAndIsOpened(adId, 1)
                .orElseThrow(() -> new IllegalArgumentException("요청하신 광고를 찾을 수 없습니다."));
    }
}
