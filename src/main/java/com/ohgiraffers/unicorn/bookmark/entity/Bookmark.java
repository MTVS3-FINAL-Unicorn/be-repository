package com.ohgiraffers.unicorn.bookmark.entity;

import com.ohgiraffers.unicorn.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Getter
@ToString
@NoArgsConstructor
public class Bookmark extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long corpId;

    @Column(nullable = false)
    private Long indivId;

    public Bookmark(Long corpId, Long indivId) {
        this.corpId = corpId;
        this.indivId = indivId;
    }
}
