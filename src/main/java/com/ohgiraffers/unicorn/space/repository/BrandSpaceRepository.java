package com.ohgiraffers.unicorn.space.repository;

import com.ohgiraffers.unicorn.space.entity.BrandSpace;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface BrandSpaceRepository extends MongoRepository<BrandSpace, String> {
    Optional<BrandSpace> findByCorpId(Long corpId);
}