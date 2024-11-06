package com.ohgiraffers.unicorn.auth.entity;

import com.ohgiraffers.unicorn.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
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

    @Column(nullable = false)
    private String picName;

    @Column
    private String binNo;

    @Column(nullable = false)
    private String contact;

    @Enumerated(value = EnumType.STRING)
    @ColumnDefault("'CORP'")
    private Authority authority;

    @Builder
    public Corp(String brandName, String picName, String binNo, String contact, String email, String password, Authority authority) {
        this.brandName = brandName;
        this.picName = picName;
        this.binNo = binNo;
        this.contact = contact;
        this.email = email;
        this.password = password;
        this.authority = authority;
    }

}
