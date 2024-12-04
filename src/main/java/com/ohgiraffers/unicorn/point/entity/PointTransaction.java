package com.ohgiraffers.unicorn.point.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "point_transactions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PointTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;
    private String activityType;
    private int points;
    private String location;
    private LocalDateTime timestamp = LocalDateTime.now();
}
