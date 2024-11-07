package com.ohgiraffers.unicorn.ad.repository;

import com.ohgiraffers.unicorn.ad.entity.Ad;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdRepository extends JpaRepository<Ad, Long> {
    Ad findByCorpIdAndIsOpened(Long corpId, int isOpened);
    Optional<Ad> findByCorpId(Long corpId);
}
