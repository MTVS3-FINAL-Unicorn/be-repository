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
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service
public class AdService {

    private final Logger logger = LoggerFactory.getLogger(AdService.class);

    @Autowired
    private AdRepository adRepository;

    @Autowired
    private AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

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

    // 광고 생성 메서드
    public Ad createAd(Long corpId, String fileUrl, String type, String description) {
        Ad ad = new Ad(corpId, fileUrl, type, description);
        return adRepository.save(ad);
    }

    public Ad getAds(Long corpId, int isOpened) {
        return adRepository.findByCorpIdAndIsOpened(corpId, isOpened);
    }
}
