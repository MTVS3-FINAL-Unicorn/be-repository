package com.ohgiraffers.unicorn.meeting.dto;

import com.ohgiraffers.unicorn.auth.dto.UserResponseDTO;
import com.ohgiraffers.unicorn.auth.entity.Indiv;
import lombok.Data;

import java.util.List;

@Data
public class MeetingDTO {

    private String meetingTitle;
    private List<String> participantGender;
    private String participantAge;  // e.g., "20 - 30"
    private String rewardType;
    private String rewardPrice;
    private String meetingDate;     // e.g., "2024-11-29"
    private String meetingTime;     // e.g., "15:00:00 - 17:00:00"
    private String recruitmentPeriod;  // e.g., "2024-10-31T10:00:00 - 2024-11-15T23:59:59"
    private String extraConditions;
    private Long corpId;
    private List<UserResponseDTO.IndivProfileDTO> participants;
}

