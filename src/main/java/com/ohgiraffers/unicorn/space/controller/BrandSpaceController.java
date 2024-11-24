package com.ohgiraffers.unicorn.space.controller;

import com.ohgiraffers.unicorn.space.dto.PaperingWrapper;
import com.ohgiraffers.unicorn.space.entity.BrandSpace;
import com.ohgiraffers.unicorn.space.service.BrandSpaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import static com.ohgiraffers.unicorn._core.utils.SecurityUtils.getCurrentUserId;

@RestController
@RequestMapping("/api/v1/space")
public class BrandSpaceController {

    @Autowired
    private BrandSpaceService brandSpaceService;

    @GetMapping("/{corpId}")
    public ResponseEntity<BrandSpace> getBrandSpace(@PathVariable("corpId") Long corpId) {
        return ResponseEntity.ok(brandSpaceService.getBrandSpaceByCorpId(corpId));
    }

    @GetMapping("/{corpId}/questions")
    public ResponseEntity<List<BrandSpace.Qna>> getQuestionList(
            @PathVariable Long corpId) {
        List<BrandSpace.Qna> answer = brandSpaceService.getQuestionList(corpId);
        return ResponseEntity.ok(answer);
    }

    @PostMapping("/{corpId}/answer")
    public ResponseEntity<String> getQnaAnswer(
            @PathVariable Long corpId,
            @RequestBody Map<String, String> requestBody) {
        String question = requestBody.get("question");
        String answer = brandSpaceService.getAnswerByQuestion(getCurrentUserId(), question);
        return ResponseEntity.ok(answer);
    }


    @PostMapping("/items")
    public ResponseEntity<BrandSpace> saveOrUpdateItems(
            @RequestBody List<BrandSpace.Item> items) {
        return ResponseEntity.ok(brandSpaceService.saveOrUpdateItems(getCurrentUserId(), items));
    }

    @PutMapping("/bgm")
    public ResponseEntity<BrandSpace> updateBgm(
            @RequestBody int bgm) {
        return ResponseEntity.ok(brandSpaceService.updateBgm(getCurrentUserId(), bgm));
    }

    @PutMapping("/lighting")
    public ResponseEntity<BrandSpace> updateLighting(
            @RequestBody BrandSpace.Light lighting) {
        return ResponseEntity.ok(brandSpaceService.updateLighting(getCurrentUserId(), lighting));
    }

    @PutMapping("/papering")
    public ResponseEntity<BrandSpace> updatePapering(
            @RequestBody PaperingWrapper paperingWrapper) {
        return ResponseEntity.ok(brandSpaceService.updatePapering(getCurrentUserId(), paperingWrapper.getPapering()));
    }

    @PostMapping("/qna")
    public ResponseEntity<BrandSpace> saveOrUpdateQna(
            @RequestBody List<BrandSpace.Qna> qnaList) {
        return ResponseEntity.ok(brandSpaceService.saveOrUpdateQna(getCurrentUserId(), qnaList));
    }
}

