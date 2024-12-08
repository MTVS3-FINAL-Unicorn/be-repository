package com.ohgiraffers.unicorn.bookmark.controller;

import com.ohgiraffers.unicorn._core.utils.ApiUtils;
import com.ohgiraffers.unicorn.auth.dto.UserResponseDTO;
import com.ohgiraffers.unicorn.auth.entity.Corp;
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

    @PostMapping("/add")
    public ResponseEntity<?> addBookmark(@RequestBody Long corpId) {
        try {
            bookmarkService.addBookmark(corpId, getCurrentUserId());
            return ResponseEntity.ok().body(ApiUtils.success(null));
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
}

