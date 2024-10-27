package com.ohgiraffers.unicorn.space.service;

import com.ohgiraffers.unicorn.space.entity.BrandSpace;
import com.ohgiraffers.unicorn.space.repository.BrandSpaceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BrandSpaceService {

    @Autowired
    private BrandSpaceRepository brandSpaceRepository;

    public BrandSpace saveOrUpdateItems(Long corpId, BrandSpace brandSpaceItem) {
        // corpId가 일치하는 기존 문서를 찾음
        BrandSpace existingItem = brandSpaceRepository.findByCorpId(corpId).orElse(null);

        if (existingItem != null) {
            // 기존 문서가 있으면 업데이트
            existingItem.setItems(brandSpaceItem.getItems());
            return brandSpaceRepository.save(existingItem);
        } else {
            // 없으면 새로 생성하여 저장
            brandSpaceItem.setCorpId(corpId);
            return brandSpaceRepository.save(brandSpaceItem);
        }
    }
}
