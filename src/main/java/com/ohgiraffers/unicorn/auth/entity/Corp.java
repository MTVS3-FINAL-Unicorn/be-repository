package com.ohgiraffers.unicorn.auth.entity;

import com.ohgiraffers.unicorn.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Corp extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String brandName;

    @Enumerated(value = EnumType.STRING)
    @ColumnDefault("'CORP'")
    private Authority authority;

    @Builder
    public Corp(String email, String password, String brandName, Authority authority) {
        this.email = email;
        this.password = password;
        this.brandName = brandName;
        this.authority = authority;
    }

}
