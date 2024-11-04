package com.ohgiraffers.unicorn.auth.repository;

import com.ohgiraffers.unicorn.auth.entity.Corp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CorpRepository extends JpaRepository<Corp, Long> {

    Optional<Corp> findByEmail(String email);
}
