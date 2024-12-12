package com.ohgiraffers.unicorn.bookmark.controller;

import com.ohgiraffers.unicorn._core.utils.ApiUtils;
import com.ohgiraffers.unicorn.auth.dto.UserResponseDTO;
import com.ohgiraffers.unicorn.bookmark.service.BookmarkService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.ohgiraffers.unicorn._core.utils.SecurityUtils.getCurrentUserId;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/bookmark")
public class BookmarkController {

    private final BookmarkService bookmarkService;

    @PostMapping("/toggle")
    public ResponseEntity<?> toggleBookmark(@RequestBody Long corpId) {
        try {
            boolean isAdded = bookmarkService.toggleBookmark(corpId, getCurrentUserId());

            if (isAdded) {
                return ResponseEntity.ok().body(ApiUtils.success("즐겨찾기에 추가되었습니다."));
            } else {
                return ResponseEntity.ok().body(ApiUtils.success("즐겨찾기가 해제되었습니다."));
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(ApiUtils.error(e.getMessage()));
        }
    }


    @GetMapping("/my-bookmarks")
    public ResponseEntity<?> getMyBookmarks() {
        Long indivId = getCurrentUserId();
        List<UserResponseDTO.CorpProfileDTO> bookmarks = bookmarkService.getMyBookmarks(indivId);
        return ResponseEntity.ok(ApiUtils.success(bookmarks));
    }

    @GetMapping("/my-subscribers")
    public ResponseEntity<?> getMySubscribers() {
        Long corpId = getCurrentUserId();
        List<UserResponseDTO.IndivProfileDTO> subscribers = bookmarkService.getMySubscribers(corpId);
        return ResponseEntity.ok(ApiUtils.success(subscribers));
    }

}

