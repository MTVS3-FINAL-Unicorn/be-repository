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
                .orElse(new BrandSpace(corpId, items));
        brandSpace.setItems(items);
        return brandSpaceRepository.save(brandSpace);
    }
}
