package com.ohgiraffers.unicorn.auth.entity;

import com.ohgiraffers.unicorn.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Indiv extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String nickname;

    @Column(nullable = false)
    private LocalDate birthDate;

    @Column(nullable = false)
    private String gender;

    @Column(nullable = false)
    private String contact;

    @Column
    @ElementCollection
    private List<Integer> categoryId;

    @Enumerated(value = EnumType.STRING)
    @ColumnDefault("'INDIV'")
    private Authority authority;

    @Builder
    public Indiv(String email, String password, String name, String nickname, String gender, LocalDate birthDate, String contact, List<Integer> categoryId, Authority authority) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.nickname = nickname;
        this.birthDate = birthDate;
        this.gender = gender;
        this.contact = contact;
        this.categoryId = categoryId;
        this.authority = authority;
    }

}
