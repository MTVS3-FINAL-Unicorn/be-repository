package com.ohgiraffers.unicorn.openfeign.client;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "generating-ad", url = "http://metaai2.iptime.org:7777")
public interface AdClient {


}
