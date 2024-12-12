package com.ohgiraffers.unicorn.bookmark.repository;

import com.ohgiraffers.unicorn.auth.entity.Indiv;
import com.ohgiraffers.unicorn.bookmark.entity.Bookmark;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {

    @Query("SELECT b.corpId FROM Bookmark b WHERE b.indivId = :indivId")
    List<Long> findCorpIdsByIndivId(@Param("indivId") Long indivId);

    @Query("SELECT b.indivId FROM Bookmark b WHERE b.corpId = :corpId")
    List<Long> findIndivIdByCorpId(@Param("corpId") Long corpId);
}
