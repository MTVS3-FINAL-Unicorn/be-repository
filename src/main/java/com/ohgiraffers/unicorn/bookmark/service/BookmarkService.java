package com.ohgiraffers.unicorn.bookmark.service;

import com.ohgiraffers.unicorn.auth.dto.UserResponseDTO;
import com.ohgiraffers.unicorn.auth.entity.Indiv;
import com.ohgiraffers.unicorn.auth.repository.CorpRepository;
import com.ohgiraffers.unicorn.auth.repository.IndivRepository;
import com.ohgiraffers.unicorn.bookmark.entity.Bookmark;
import com.ohgiraffers.unicorn.bookmark.repository.BookmarkRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Arrays.stream;

@RequiredArgsConstructor
@Service
public class BookmarkService {

    private final BookmarkRepository bookmarkRepository;
    private final CorpRepository corpRepository;
    private final IndivRepository indivRepository;

    @Transactional
    public void addBookmark(Long corpId, Long currentUserId) {
        Bookmark bookmark = new Bookmark(corpId, currentUserId);
        bookmarkRepository.save(bookmark);
    }

    @Transactional(readOnly = true)
    public List<UserResponseDTO.CorpProfileDTO> getMyBookmarks(Long indivId) {
        List<Long> corpIds = bookmarkRepository.findCorpIdsByIndivId(indivId);

        if (corpIds.isEmpty()) {
            return new ArrayList<>();
        }

        return corpRepository.findCorpProfilesByIdIn(corpIds);
    }

    @Transactional(readOnly = true)
    public List<UserResponseDTO.IndivProfileDTO> getMySubscribers(Long corpId) {
        List<Long> indivIds = bookmarkRepository.findIndivIdByCorpId(corpId);

        if (indivIds.isEmpty()) {
            return new ArrayList<>();
        }

        return indivIds.stream()
                .map(this::getIndivProfileById)
                .collect(Collectors.toList());
    }

    private UserResponseDTO.IndivProfileDTO getIndivProfileById(Long indivId) {
        Indiv indiv = indivRepository.findById(indivId)
                .orElseThrow(() -> new IllegalArgumentException("Indiv not found for id: " + indivId));
        return convertToIndivProfileDTO(indiv);
    }

    private UserResponseDTO.IndivProfileDTO convertToIndivProfileDTO(Indiv indiv) {
        // 나이 계산 (만 나이)
        int age = calculateAge(indiv.getBirthDate());

        return new UserResponseDTO.IndivProfileDTO(
                indiv.getId(),
                indiv.getName(),
                indiv.getNickname(),
                age,
                indiv.getGender(),
                indiv.getContact(),
                indiv.getCategoryId()
        );
    }

    private int calculateAge(LocalDate birthDate) {
        return Period.between(birthDate, LocalDate.now()).getYears();
    }

}
