package com.ohgiraffers.unicorn.space.controller;

import com.ohgiraffers.unicorn.space.entity.BrandSpace;
import com.ohgiraffers.unicorn.space.service.BrandSpaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/space/{corpId}")
public class BrandSpaceController {

    @Autowired
    private BrandSpaceService brandSpaceService;

    @GetMapping
    public ResponseEntity<BrandSpace> getBrandSpace(@PathVariable Long corpId) {
        return ResponseEntity.ok(brandSpaceService.getBrandSpaceByCorpId(corpId));
    }

    @PostMapping("/items")
    public ResponseEntity<BrandSpace> saveOrUpdateItems(
            @PathVariable Long corpId,
            @RequestBody List<BrandSpace.Item> items) {
        return ResponseEntity.ok(brandSpaceService.saveOrUpdateItems(corpId, items));
    }

    @PutMapping("/bgm")
    public ResponseEntity<BrandSpace> updateBgm(
            @PathVariable Long corpId,
            @RequestBody int bgm) {
        return ResponseEntity.ok(brandSpaceService.updateBgm(corpId, bgm));
    }

    @PutMapping("/lighting")
    public ResponseEntity<BrandSpace> updateLighting(
            @PathVariable Long corpId,
            @RequestBody BrandSpace.Light lighting) {
        return ResponseEntity.ok(brandSpaceService.updateLighting(corpId, lighting));
    }

    @PostMapping("/qna")
    public ResponseEntity<BrandSpace> saveOrUpdateQna(
            @PathVariable Long corpId,
            @RequestBody List<BrandSpace.Qna> qnaList) {
        return ResponseEntity.ok(brandSpaceService.saveOrUpdateQna(corpId, qnaList));
    }
}
