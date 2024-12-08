package com.ohgiraffers.unicorn.bookmark.service;

import com.ohgiraffers.unicorn.bookmark.entity.Bookmark;
import com.ohgiraffers.unicorn.bookmark.repository.BookmarkRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class BookmarkService {

    private final BookmarkRepository bookmarkRepository;

    @Transactional
    public void addBookmark(Long corpId, Long currentUserId) {
        Bookmark bookmark = new Bookmark(corpId, currentUserId);
        bookmarkRepository.save(bookmark);
    }
}
