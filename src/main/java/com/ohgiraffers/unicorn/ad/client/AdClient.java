package com.ohgiraffers.unicorn.ad.client;

import com.ohgiraffers.unicorn.ad.dto.AdRequestDTO;
import com.ohgiraffers.unicorn._core.config.FeignClientConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "generating-ad", url = "http://metaai2.iptime.org:7777", configuration = FeignClientConfig.class)
public interface AdClient {

    @PostMapping(value = "/generate-preview")
    String generatePreview(@RequestBody AdRequestDTO request);

    @PostMapping(value = "/generate-videoad")
    String generateVideoAd(@RequestBody AdRequestDTO request);
}
