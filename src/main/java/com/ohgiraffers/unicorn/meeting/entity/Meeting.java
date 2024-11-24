package com.ohgiraffers.unicorn.meeting.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
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
    private Integer participantAgeStart;
    private Integer participantAgeEnd;
    private String rewardType;
    private Long rewardPrice;
    private LocalDate meetingDate;
    private LocalTime meetingTimeStart;
    private LocalTime meetingTimeEnd;
    private LocalDate recruitmentPeriodStart;
    private LocalDate recruitmentPeriodEnd;
    private String extraConditions;
    private Long corpId;
    private int categoryId;
    private boolean hasDeleted = false;
    private boolean isExpired = false;

    @ElementCollection
    private List<String> participantGender = new ArrayList<>(); // Ensure this field is correctly declared

    @ElementCollection
    private List<Participant> participantStatus = new ArrayList<>();

    @Embeddable
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Participant {
        private Long userId;
        private String gender;

        @Enumerated(EnumType.STRING)
        private ParticipantStatus status;
    }

    public void updateExpirationStatus() {
        LocalDate now = LocalDate.now();
        this.isExpired = recruitmentPeriodEnd != null && recruitmentPeriodEnd.isBefore(now);
    }
}
