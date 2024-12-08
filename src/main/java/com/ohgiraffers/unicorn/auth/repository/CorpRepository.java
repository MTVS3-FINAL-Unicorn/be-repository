package com.ohgiraffers.unicorn.auth.repository;

import com.ohgiraffers.unicorn.auth.dto.UserResponseDTO;
import com.ohgiraffers.unicorn.auth.entity.Corp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CorpRepository extends JpaRepository<Corp, Long> {

    Optional<Corp> findByEmail(String email);

    @Query("SELECT new com.ohgiraffers.unicorn.auth.dto.UserResponseDTO$CorpProfileDTO(c.id, c.brandName, c.picName, c.binNo, c.contact, c.categoryId) FROM Corp c WHERE c.id IN :corpIds")
    List<UserResponseDTO.CorpProfileDTO> findCorpProfilesByIdIn(@Param("corpIds") List<Long> corpIds);

}
