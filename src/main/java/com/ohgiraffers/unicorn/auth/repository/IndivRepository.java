package com.ohgiraffers.unicorn.auth.repository;

import com.ohgiraffers.unicorn.auth.entity.Indiv;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IndivRepository extends JpaRepository<Indiv, Long> {

    Optional<Indiv> findByEmail(String email);

}
