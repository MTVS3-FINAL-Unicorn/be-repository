package com.ohgiraffers.unicorn.meeting.service;

import com.ohgiraffers.unicorn.meeting.dto.MeetingDTO;
import com.ohgiraffers.unicorn.meeting.entity.Meeting;
import com.ohgiraffers.unicorn.meeting.repository.MeetingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
public class MeetingService {

    @Autowired
    private MeetingRepository meetingRepository;

    public Meeting createMeeting(MeetingDTO meetingDTO) {
        Meeting meeting = new Meeting();

        // 기본적인 필드 설정
        meeting.setMeetingTitle(meetingDTO.getMeetingTitle());
        meeting.setParticipantGender(meetingDTO.getParticipantGender());
        meeting.setRewardType(meetingDTO.getRewardType());
        meeting.setExtraConditions(meetingDTO.getExtraConditions());
        meeting.setCorpId(meetingDTO.getCorpId());

        // 연령대 범위 설정
        if (meetingDTO.getParticipantAge() != null) {
            String[] ageRange = meetingDTO.getParticipantAge().split(" - ");
            meeting.setParticipantAgeStart(Integer.parseInt(ageRange[0]));
            meeting.setParticipantAgeEnd(Integer.parseInt(ageRange[1]));
        }

        // 보상 금액 설정
        if (meetingDTO.getRewardPrice() != null && !meetingDTO.getRewardPrice().isEmpty()) {
            meeting.setRewardPrice(Long.parseLong(meetingDTO.getRewardPrice()));
        }

        // 좌담회 날짜 설정
        if (meetingDTO.getMeetingDate() != null) {
            meeting.setMeetingDate(LocalDate.parse(meetingDTO.getMeetingDate()));
        }

        // 좌담회 시간 범위 설정
        if (meetingDTO.getMeetingTime() != null) {
            String[] timeRange = meetingDTO.getMeetingTime().split(" - ");
            meeting.setMeetingTimeStart(LocalTime.parse(timeRange[0]));
            meeting.setMeetingTimeEnd(LocalTime.parse(timeRange[1]));
        }

        // 모집 기간 범위 설정
        if (meetingDTO.getRecruitmentPeriod() != null) {
            String[] periodRange = meetingDTO.getRecruitmentPeriod().split(" - ");
            meeting.setRecruitmentPeriodStart(LocalDateTime.parse(periodRange[0]));
            meeting.setRecruitmentPeriodEnd(LocalDateTime.parse(periodRange[1]));
        }

        return meetingRepository.save(meeting);
    }

    public List<Meeting> getAllMeetings() {
        return meetingRepository.findAll();
    }

    public Meeting getMeetingById(Long meetingId) {
        return meetingRepository.findById(meetingId)
                .orElseThrow(() -> new RuntimeException("Meeting not found with id: " + meetingId));
    }
}

