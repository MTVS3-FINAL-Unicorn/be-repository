package com.ohgiraffers.unicorn.meeting.service;

import com.ohgiraffers.unicorn.auth.dto.UserResponseDTO;
import com.ohgiraffers.unicorn.auth.entity.Indiv;
import com.ohgiraffers.unicorn.auth.repository.IndivRepository;
import com.ohgiraffers.unicorn.meeting.dto.MeetingDTO;
import com.ohgiraffers.unicorn.meeting.entity.Meeting;
import com.ohgiraffers.unicorn.meeting.entity.ParticipantStatus;
import com.ohgiraffers.unicorn.meeting.repository.MeetingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Period;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MeetingService {

    @Autowired
    private MeetingRepository meetingRepository;
    @Autowired
    private IndivRepository indivRepository;

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

    @Transactional
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

    @Transactional
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

    @Transactional
    public void softDeleteMeeting(Long meetingId) {
        Meeting meeting = meetingRepository.findById(meetingId)
                .orElseThrow(() -> new RuntimeException("Meeting not found"));
        meeting.setHasDeleted(true);
        meetingRepository.save(meeting);
    }

    @Transactional
    public void joinMeeting(Long meetingId, Long userId) {
        Meeting meeting = meetingRepository.findById(meetingId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid meeting ID"));
        Indiv user = indivRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user ID"));

        // Validate age and gender eligibility
        int userAge = calculateAge(user.getBirthDate());
        if (userAge < meeting.getParticipantAgeStart() || userAge > meeting.getParticipantAgeEnd()) {
            throw new IllegalArgumentException("신청 가능한 연령을 확인해주세요.");
        }
        if (!meeting.getParticipantGender().contains(user.getGender())) {
            throw new IllegalArgumentException("신청 가능한 성별을 확인해주세요.");
        }
        if (hasScheduleConflict(meeting, userId)) {
            throw new IllegalArgumentException("해당 시간대에 신청된 좌담회가 있습니다.");
        }

        // Check if the user is already a participant and add them with PENDING status if not
        boolean isAlreadyParticipant = meeting.getParticipantStatus().stream()
                .anyMatch(p -> p.getUserId().equals(userId));
        if (!isAlreadyParticipant) {
            meeting.getParticipantStatus().add(new Meeting.Participant(userId, user.getGender(), ParticipantStatus.PENDING));
            meetingRepository.save(meeting);
        }
    }

    @Transactional
    public void cancelMeeting(Long meetingId, Long userId) {
        Meeting meeting = meetingRepository.findById(meetingId)
                .orElseThrow(() -> new IllegalArgumentException("좌담회 정보를 찾을 수 없습니다."));

        Meeting.Participant participant = meeting.getParticipantStatus().stream()
                .filter(p -> p.getUserId().equals(userId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("신청 내역이 존재하지 않습니다."));

        if (participant.getStatus() == ParticipantStatus.CANCELLED_PENDING ||
                participant.getStatus() == ParticipantStatus.CANCELLED_CONFIRMED) {
            throw new IllegalArgumentException("이미 취소된 신청입니다.");
        }

        participant.setStatus(ParticipantStatus.CANCELLED_PENDING); // Update status
        meetingRepository.save(meeting);
    }

    public List<UserResponseDTO.IndivProfileDTO> getMeetingParticipants(Long meetingId) {
        Meeting meeting = meetingRepository.findById(meetingId)
                .orElseThrow(() -> new IllegalArgumentException("좌담회 정보를 찾을 수 없습니다."));

        // Participant ID 리스트 추출
        List<Long> participantIds = meeting.getParticipantStatus().stream()
                .map(Meeting.Participant::getUserId)
                .collect(Collectors.toList());

        // 해당하는 모든 Indiv 엔티티 조회 후, IndivProfileDTO로 변환
        return indivRepository.findAllById(participantIds).stream()
                .map(indiv -> new UserResponseDTO.IndivProfileDTO(
                        indiv.getName(),
                        indiv.getNickname(),
                        calculateAge(indiv.getBirthDate()),
                        indiv.getGender(),
                        indiv.getContact(),
                        indiv.getCategoryId()
                ))
                .collect(Collectors.toList());
    }

    // 사용자 연령 계산
    private int calculateAge(LocalDate birthDate) {
        return Period.between(birthDate, LocalDate.now()).getYears();
    }

    // 스케쥴 중복 여부 확인
    private boolean hasScheduleConflict(Meeting newMeeting, Long userId) {
        List<Meeting> userMeetings = meetingRepository.findAllByParticipantUserId(userId);

        for (Meeting meeting : userMeetings) {
            if (meeting.getMeetingDate().equals(newMeeting.getMeetingDate()) &&
                    ((newMeeting.getMeetingTimeStart().isBefore(meeting.getMeetingTimeEnd()) &&
                            newMeeting.getMeetingTimeEnd().isAfter(meeting.getMeetingTimeStart())))) {
                return true;
            }
        }
        return false;
    }
}

