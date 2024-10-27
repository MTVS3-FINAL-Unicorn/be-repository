package com.ohgiraffers.bridge.ad.service;

import com.ohgiraffers.bridge.ad.entity.Ad;
import com.ohgiraffers.bridge.ad.repository.AdRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service
public class AdService {

    private final String uploadDir = "/Users/admin/git/be-repository/Bridge/uploads";
    private final Logger logger = LoggerFactory.getLogger(AdService.class);

    @Autowired
    private AdRepository adRepository;

    public Ad createAd(Long corpId, MultipartFile file, String type, String description) throws IOException {
        String fileName = file.getOriginalFilename();
        Path filePath = Paths.get(uploadDir, fileName);

        logger.info("Received file: {}", fileName);
        logger.info("Saving file to path: {}", filePath.toString());

        // 디렉토리가 없을 때 생성
        Files.createDirectories(filePath.getParent());

        try {
            // 파일 저장
            file.transferTo(filePath.toFile());
        } catch (IOException e) {
            logger.error("Error saving file: {}", e.getMessage(), e);
            throw e;  // 예외를 다시 던져 처리
        }

        Ad ad = new Ad(corpId, filePath.toString(), type, description);
        return adRepository.save(ad);
    }

    public Ad getAds(Long corpId, int isOpened) {
        return adRepository.findByCorpIdAndIsOpened(corpId, isOpened);
    }
}
