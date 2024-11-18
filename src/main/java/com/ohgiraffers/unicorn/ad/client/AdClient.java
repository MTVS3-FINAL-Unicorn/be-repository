package com.ohgiraffers.unicorn.ad.client;

import com.ohgiraffers.unicorn.ad.dto.AdRequestDTO;
import com.ohgiraffers.unicorn.config.FeignClientConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

@FeignClient(name = "generating-ad", url = "http://metaai2.iptime.org:7777")
public interface AdClient {

    @PostMapping(value = "/generate-videoad", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    String generateVideoAd(
            @RequestPart("image") MultipartFile file,
            @RequestPart("prompt") String prompt,
            @RequestPart("corpId") Long corpId,
            @RequestPart("ratioType") String ratioType
    );
}
