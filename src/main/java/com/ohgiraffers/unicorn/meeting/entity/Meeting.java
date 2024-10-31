package com.ohgiraffers.unicorn.meeting.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Entity
@Table(name = "meetings")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Meeting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long meetingId;

    private String meetingTitle;

    @ElementCollection
    private List<String> participantGender;

    private Integer participantAgeStart;
    private Integer participantAgeEnd;

    private String rewardType;

    private Long rewardPrice;

    private LocalDate meetingDate;

    private LocalTime meetingTimeStart;
    private LocalTime meetingTimeEnd;

    private LocalDateTime recruitmentPeriodStart;
    private LocalDateTime recruitmentPeriodEnd;

    private String extraConditions;

    private Long corpId;

    private boolean hasDeleted = false;
    private boolean isExpired = false;

    public void updateExpirationStatus() {
        LocalDateTime now = LocalDateTime.now();
        this.isExpired = recruitmentPeriodEnd != null && recruitmentPeriodEnd.isBefore(now);
    }
}
