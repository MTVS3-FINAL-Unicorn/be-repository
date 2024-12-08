package com.ohgiraffers.unicorn.bookmark.controller;

import com.ohgiraffers.unicorn._core.utils.ApiUtils;
import com.ohgiraffers.unicorn.bookmark.service.BookmarkService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}

