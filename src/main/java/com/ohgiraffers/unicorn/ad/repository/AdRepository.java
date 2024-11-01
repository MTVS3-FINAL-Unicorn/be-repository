package com.ohgiraffers.unicorn.ad.repository;

import com.ohgiraffers.unicorn.ad.entity.Ad;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdRepository extends JpaRepository<Ad, Long> {
    Ad findByCorpIdAndIsOpened(Long corpId, int isOpened);
}
