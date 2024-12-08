package com.ohgiraffers.unicorn.bookmark.service;

import com.ohgiraffers.unicorn.auth.dto.UserResponseDTO;
import com.ohgiraffers.unicorn.auth.entity.Corp;
import com.ohgiraffers.unicorn.auth.repository.CorpRepository;
import com.ohgiraffers.unicorn.bookmark.entity.Bookmark;
import com.ohgiraffers.unicorn.bookmark.repository.BookmarkRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class BookmarkService {

    private final BookmarkRepository bookmarkRepository;
    private final CorpRepository corpRepository;

    @Transactional
    public void addBookmark(Long corpId, Long currentUserId) {
        Bookmark bookmark = new Bookmark(corpId, currentUserId);
        bookmarkRepository.save(bookmark);
    }

    @Transactional(readOnly = true)
    public List<UserResponseDTO.CorpProfileDTO> getMyBookmarks(Long indivId) {
        // Bookmark 테이블에서 indivId에 해당하는 corpId 목록을 가져옴
        List<Long> corpIds = bookmarkRepository.findCorpIdsByIndivId(indivId);

        // corpIds가 비어있으면 빈 리스트 반환
        if (corpIds.isEmpty()) {
            return new ArrayList<>();
        }

        // CorpRepository에서 해당 브랜드 정보를 DTO로 변환하여 가져옴
        return corpRepository.findCorpProfilesByIdIn(corpIds);
    }



}
