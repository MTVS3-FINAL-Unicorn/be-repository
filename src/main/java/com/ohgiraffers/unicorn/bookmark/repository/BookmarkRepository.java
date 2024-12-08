package com.ohgiraffers.unicorn.bookmark.repository;

import com.ohgiraffers.unicorn.bookmark.entity.Bookmark;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {

}
