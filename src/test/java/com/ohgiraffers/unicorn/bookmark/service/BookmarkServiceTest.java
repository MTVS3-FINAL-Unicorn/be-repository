package com.ohgiraffers.unicorn.bookmark.service;

import com.ohgiraffers.unicorn.auth.entity.Authority;
import com.ohgiraffers.unicorn.auth.entity.Indiv;
import com.ohgiraffers.unicorn.auth.repository.CorpRepository;
import com.ohgiraffers.unicorn.auth.repository.IndivRepository;
import com.ohgiraffers.unicorn.bookmark.entity.Bookmark;
import com.ohgiraffers.unicorn.bookmark.repository.BookmarkRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class BookmarkServiceTest {

    @Mock
    private BookmarkRepository bookmarkRepository;

    @Mock
    private CorpRepository corpRepository;

    @Mock
    private IndivRepository indivRepository;

    @InjectMocks
    private BookmarkService bookmarkService;

    @Test
    void toggleBookmark_addsBookmarkWhenNotExisting() {
        given(bookmarkRepository.findByCorpIdAndIndivId(1L, 2L)).willReturn(Optional.empty());

        boolean result = bookmarkService.toggleBookmark(1L, 2L);

        assertThat(result).isTrue();
        verify(bookmarkRepository).save(any(Bookmark.class));
    }

    @Test
    void toggleBookmark_removesBookmarkWhenAlreadyExists() {
        Bookmark existing = new Bookmark(1L, 2L);
        given(bookmarkRepository.findByCorpIdAndIndivId(1L, 2L)).willReturn(Optional.of(existing));

        boolean result = bookmarkService.toggleBookmark(1L, 2L);

        assertThat(result).isFalse();
        verify(bookmarkRepository).delete(existing);
        verify(bookmarkRepository, never()).save(any(Bookmark.class));
    }

    @Test
    void getMyBookmarks_returnsEmptyListWhenNoBookmarksExist() {
        given(bookmarkRepository.findCorpIdsByIndivId(2L)).willReturn(List.of());

        List<?> result = bookmarkService.getMyBookmarks(2L);

        assertThat(result).isEmpty();
    }

    @Test
    void getMySubscribers_returnsEmptyListWhenNoSubscribersExist() {
        given(bookmarkRepository.findIndivIdByCorpId(1L)).willReturn(List.of());

        List<?> result = bookmarkService.getMySubscribers(1L);

        assertThat(result).isEmpty();
    }

    @Test
    void getMySubscribers_mapsIndivToProfileDtoWithCalculatedAge() {
        LocalDate birthDate = LocalDate.now().minusYears(20);
        Indiv indiv = Indiv.builder()
                .email("test@example.com")
                .password("password")
                .name("홍길동")
                .nickname("길동이")
                .gender("M")
                .birthDate(birthDate)
                .contact("010-0000-0000")
                .categoryId(List.of(1))
                .authority(Authority.INDIV)
                .build();
        given(indivRepository.findById(2L)).willReturn(Optional.of(indiv));
        given(bookmarkRepository.findIndivIdByCorpId(1L)).willReturn(List.of(2L));

        var result = bookmarkService.getMySubscribers(1L);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).age()).isEqualTo(Period.between(birthDate, LocalDate.now()).getYears());
    }
}
