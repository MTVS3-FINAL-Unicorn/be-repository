package com.ohgiraffers.unicorn.ad.service;

import com.ohgiraffers.unicorn.ad.entity.Ad;
import com.ohgiraffers.unicorn.ad.repository.AdRepository;
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

    private final Logger logger = LoggerFactory.getLogger(AdService.class);

    @Autowired
    private AdRepository adRepository;

    public Ad createAd(Long corpId, String fileUrl, String type, String description) throws IOException {

        Ad ad = new Ad(corpId, fileUrl, type, description);
        return adRepository.save(ad);
    }

    public Ad getAds(Long corpId, int isOpened) {
        return adRepository.findByCorpIdAndIsOpened(corpId, isOpened);
    }
}
