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
import java.util.stream.Collectors;

@Service
public class MeetingService {

    @Autowired
    private MeetingRepository meetingRepository;

    public MeetingService(MeetingRepository meetingRepository) {
        this.meetingRepository = meetingRepository;
    }

    // 전체 조회 메서드
    public List<MeetingDTO> getAllMeetings() {
        return meetingRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // 단일 조회 메서드
    public MeetingDTO getMeetingById(Long meetingId) {
        Meeting meeting = meetingRepository.findById(meetingId)
                .orElseThrow(() -> new RuntimeException("Meeting not found with id: " + meetingId));
        return convertToDto(meeting);
    }

    // Meeting 엔티티를 MeetingDTO로 변환하는 메서드
    private MeetingDTO convertToDto(Meeting meeting) {
        MeetingDTO meetingDTO = new MeetingDTO();
        meetingDTO.setMeetingTitle(meeting.getMeetingTitle());
        meetingDTO.setParticipantGender(meeting.getParticipantGender());
        meetingDTO.setParticipantAge(
                meeting.getParticipantAgeStart() + " - " + meeting.getParticipantAgeEnd());
        meetingDTO.setRewardType(meeting.getRewardType());
        meetingDTO.setRewardPrice(meeting.getRewardPrice() != null ? meeting.getRewardPrice().toString() : null);
        meetingDTO.setMeetingDate(meeting.getMeetingDate() != null ? meeting.getMeetingDate().toString() : null);
        meetingDTO.setMeetingTime(meeting.getMeetingTimeStart() + " - " + meeting.getMeetingTimeEnd());
        meetingDTO.setRecruitmentPeriod(
                meeting.getRecruitmentPeriodStart() + " - " + meeting.getRecruitmentPeriodEnd());
        meetingDTO.setExtraConditions(meeting.getExtraConditions());
        meetingDTO.setCorpId(meeting.getCorpId());
        return meetingDTO;
    }


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

    public Meeting updateMeeting(Long meetingId, MeetingDTO meetingDTO) {
        Meeting meeting = meetingRepository.findById(meetingId)
                .orElseThrow(() -> new RuntimeException("Meeting not found"));

        // 필드 업데이트
        meeting.setMeetingTitle(meetingDTO.getMeetingTitle());
        meeting.setParticipantGender(meetingDTO.getParticipantGender());
        meeting.setRewardType(meetingDTO.getRewardType());
        meeting.setExtraConditions(meetingDTO.getExtraConditions());

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

        // 좌담회 날짜 및 시간 범위 설정
        if (meetingDTO.getMeetingDate() != null) {
            meeting.setMeetingDate(LocalDate.parse(meetingDTO.getMeetingDate()));
        }
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

    public void softDeleteMeeting(Long meetingId) {
        Meeting meeting = meetingRepository.findById(meetingId)
                .orElseThrow(() -> new RuntimeException("Meeting not found"));
        meeting.setHasDeleted(true);
        meetingRepository.save(meeting);
    }
}

