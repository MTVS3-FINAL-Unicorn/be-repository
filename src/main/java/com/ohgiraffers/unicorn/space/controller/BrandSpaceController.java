package com.ohgiraffers.unicorn.space.controller;

import com.ohgiraffers.unicorn.space.entity.BrandSpace;
import com.ohgiraffers.unicorn.space.service.BrandSpaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/place")
public class BrandSpaceController {

    @Autowired
    private BrandSpaceService brandSpaceService;

    @PostMapping("/{corpId}/items")
    public ResponseEntity<BrandSpace> saveOrUpdateBrandSpaceItems(
            @PathVariable Long corpId,
            @RequestBody BrandSpace brandSpaceItem) {

        BrandSpace savedItem = brandSpaceService.saveOrUpdateItems(corpId, brandSpaceItem);
        return ResponseEntity.ok(savedItem);
    }
}
