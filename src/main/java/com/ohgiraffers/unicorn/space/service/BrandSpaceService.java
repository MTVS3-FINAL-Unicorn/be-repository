package com.ohgiraffers.unicorn.space.service;

import com.ohgiraffers.unicorn.space.entity.BrandSpace;
import com.ohgiraffers.unicorn.space.repository.BrandSpaceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BrandSpaceService {

    @Autowired
    private BrandSpaceRepository brandSpaceRepository;

    public BrandSpace saveOrUpdateItems(Long corpId, List<BrandSpace.Item> items) {
        BrandSpace brandSpace = brandSpaceRepository.findByCorpId(corpId)
                .orElse(new BrandSpace(corpId, items, 0, new BrandSpace.Light()));
        brandSpace.setItems(items);
        return brandSpaceRepository.save(brandSpace);
    }

    public BrandSpace updateBgm(Long corpId, int bgm) {
        BrandSpace brandSpace = brandSpaceRepository.findByCorpId(corpId)
                .orElseThrow(() -> new RuntimeException("브랜드관 정보를 찾을 수 없습니다."));
        brandSpace.setBgm(bgm);
        return brandSpaceRepository.save(brandSpace);
    }

    public BrandSpace updateLighting(Long corpId, BrandSpace.Light lighting) {
        BrandSpace brandSpace = brandSpaceRepository.findByCorpId(corpId)
                .orElseThrow(() -> new RuntimeException("BrandSpace not found"));
        brandSpace.setLighting(lighting);
        return brandSpaceRepository.save(brandSpace);
    }

    public BrandSpace saveOrUpdateQna(Long corpId, List<BrandSpace.Qna> qnaList) {
        BrandSpace brandSpace = brandSpaceRepository.findByCorpId(corpId)
                .orElseThrow(() -> new RuntimeException("BrandSpace not found"));
        brandSpace.setQna(qnaList);
        return brandSpaceRepository.save(brandSpace);
    }
}
