package com.ohgiraffers.unicorn.ad.repository;

import com.ohgiraffers.unicorn.ad.entity.Ad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AdRepository extends JpaRepository<Ad, Long> {
    Optional<Ad> findByAdId(Long adId);
    List<Ad> findAllByCorpId(Long corpId);
}
