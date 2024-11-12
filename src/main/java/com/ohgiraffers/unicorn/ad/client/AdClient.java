package com.ohgiraffers.unicorn.ad.client;

import com.ohgiraffers.unicorn.ad.dto.AdRequestDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "generating-ad", url = "http://metaai2.iptime.org:7777")
public interface AdClient {

    @PostMapping("/generate-videoad")
    String generateVideoAd(@RequestBody AdRequestDTO request);
}
