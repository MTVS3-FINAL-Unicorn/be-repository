package com.ohgiraffers.unicorn.meeting.service;

import com.ohgiraffers.unicorn.auth.dto.UserResponseDTO;
import com.ohgiraffers.unicorn.auth.entity.Indiv;
import com.ohgiraffers.unicorn.auth.repository.IndivRepository;
import com.ohgiraffers.unicorn.auth.service.CorpService;
import com.ohgiraffers.unicorn.meeting.dto.IndivMeetingDTO;
import com.ohgiraffers.unicorn.meeting.dto.MeetingDTO;
import com.ohgiraffers.unicorn.meeting.entity.Meeting;
import com.ohgiraffers.unicorn.meeting.entity.ParticipantStatus;
import com.ohgiraffers.unicorn.meeting.repository.MeetingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Period;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class MeetingService {

    @Autowired
    private MeetingRepository meetingRepository;
    @Autowired
    private IndivRepository indivRepository;
    @Autowired
    private CorpService corpService;

    public MeetingService(MeetingRepository meetingRepository) {
        this.meetingRepository = meetingRepository;
    }

    // 전체 조회 메서드
    public List<MeetingDTO> getAllMeetings() {
        return meetingRepository.findAll().stream()
                .filter(meeting -> !meeting.isHasDeleted() && !meeting.isExpired())
                .map(this::convertToDTOWithFilteredParticipants)
                .collect(Collectors.toList());
    }

    // 단일 조회 메서드
    public MeetingDTO getMeetingById(Long meetingId) {
        Meeting meeting = meetingRepository.findById(meetingId)
                .orElseThrow(() -> new RuntimeException("Meeting not found with id: " + meetingId));
        return convertToDTOWithFilteredParticipants(meeting);
    }

    public List<IndivMeetingDTO> getMeetingsByIndivId(Long indivId) {
        List<Meeting> meetings = meetingRepository.findAllByParticipantUserId(indivId);
        return meetings.stream()
                .map(meeting -> convertToIndivDTO(meeting, indivId)) // Pass both meeting and indivId
                .collect(Collectors.toList());
    }

    public List<MeetingDTO> getMeetingsByCorpId(Long corpId) {
        List<Meeting> meetings = meetingRepository.findByCorpId(corpId);
        return meetings.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private MeetingDTO convertToDTOWithFilteredParticipants(Meeting meeting) {
        MeetingDTO meetingDTO = convertToDTO(meeting);

        List<UserResponseDTO.IndivProfileWithStatusDTO> filteredParticipants = meeting.getParticipantStatus().stream()
                .filter(participant -> participant.getStatus() == ParticipantStatus.PENDING
                        || participant.getStatus() == ParticipantStatus.APPROVED)
                .map(participant -> indivRepository.findById(participant.getUserId())
                        .map(indiv -> {
                            UserResponseDTO.IndivProfileWithStatusDTO profile = new UserResponseDTO.IndivProfileWithStatusDTO(
                                    indiv.getId(),
                                    indiv.getName(),
                                    indiv.getNickname(),
                                    calculateAge(indiv.getBirthDate()),
                                    indiv.getGender(),
                                    indiv.getContact(),
                                    indiv.getCategoryId(),
                                    participant.getStatus()
                            );
                            return profile;
                        })
                        .orElse(null))
                .filter(profile -> profile != null)
                .collect(Collectors.toList());

        meetingDTO.setParticipants(filteredParticipants);
        return meetingDTO;
    }

    private MeetingDTO convertToDTO(Meeting meeting) {
        MeetingDTO meetingDTO = new MeetingDTO();
        meetingDTO.setMeetingId(meeting.getMeetingId());
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

        List<UserResponseDTO.IndivProfileWithStatusDTO> participants = meeting.getParticipantStatus().stream()
                .map(participant -> indivRepository.findById(participant.getUserId())
                        .map(indiv -> new UserResponseDTO.IndivProfileWithStatusDTO(
                                indiv.getId(),
                                indiv.getName(),
                                indiv.getNickname(),
                                calculateAge(indiv.getBirthDate()),
                                indiv.getGender(),
                                indiv.getContact(),
                                indiv.getCategoryId(),
                                participant.getStatus()
                        ))
                        .orElse(null))
                .filter(profile -> profile != null)
                .collect(Collectors.toList());

        meetingDTO.setParticipants(participants);

        return meetingDTO;
    }

    private IndivMeetingDTO convertToIndivDTO(Meeting meeting, Long indivId) {
        IndivMeetingDTO meetingDTO = new IndivMeetingDTO();
        UserResponseDTO.CorpProfileDTO corp = corpService.getUserProfile(meeting.getCorpId());

        meetingDTO.setBrandName(corp.brandName());
        meetingDTO.setMeetingId(meeting.getMeetingId());
        meetingDTO.setMeetingTitle(meeting.getMeetingTitle());
        meetingDTO.setParticipantGender(meeting.getParticipantGender());
        meetingDTO.setParticipantAge(meeting.getParticipantAgeStart() + " - " + meeting.getParticipantAgeEnd());
        meetingDTO.setRewardType(meeting.getRewardType());
        meetingDTO.setRewardPrice(meeting.getRewardPrice() != null ? meeting.getRewardPrice().toString() : null);
        meetingDTO.setMeetingDate(meeting.getMeetingDate() != null ? meeting.getMeetingDate().toString() : null);
        meetingDTO.setMeetingTime(meeting.getMeetingTimeStart() + " - " + meeting.getMeetingTimeEnd());
        meetingDTO.setRecruitmentPeriod(meeting.getRecruitmentPeriodStart() + " - " + meeting.getRecruitmentPeriodEnd());
        meetingDTO.setExtraConditions(meeting.getExtraConditions());
        meetingDTO.setCorpId(meeting.getCorpId());

        // Find the participant's status for this individual
        meeting.getParticipantStatus().stream()
                .filter(participant -> participant.getUserId().equals(indivId))
                .findFirst()
                .ifPresent(participant -> meetingDTO.setStatus(participant.getStatus().toString()));

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
            meeting.setRecruitmentPeriodStart(LocalDate.parse(periodRange[0]));
            meeting.setRecruitmentPeriodEnd(LocalDate.parse(periodRange[1]));
        }

        return meetingRepository.save(meeting);
    }

    @Transactional
    public Meeting updateMeeting(MeetingDTO meetingDTO) {
        Meeting meeting = meetingRepository.findById(meetingDTO.getMeetingId())
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
            meeting.setRecruitmentPeriodStart(LocalDate.parse(periodRange[0]));
            meeting.setRecruitmentPeriodEnd(LocalDate.parse(periodRange[1]));
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

        // 참가자 목록에서 사용자를 찾고, 상태 확인
        Meeting.Participant existingParticipant = meeting.getParticipantStatus().stream()
                .filter(p -> p.getUserId().equals(userId))
                .findFirst()
                .orElse(null);

        if (existingParticipant != null) {
            if (existingParticipant.getStatus() == ParticipantStatus.REJECTED) {
                throw new IllegalArgumentException("참여할 수 없는 좌담회입니다");
            }
        } else {
            // 연령 및 성별 검증 후 새로운 참가자로 추가
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

            meeting.getParticipantStatus().add(new Meeting.Participant(userId, user.getGender(), ParticipantStatus.PENDING));
            meetingRepository.save(meeting);
        }
    }


    @Transactional
    public void cancelMeeting(Long meetingId, Long userId) {
        Meeting meeting = meetingRepository.findById(meetingId)
                .orElseThrow(() -> new IllegalArgumentException("좌담회 정보를 찾을 수 없습니다."));

        boolean removed = meeting.getParticipantStatus().removeIf(participant ->
                participant.getUserId().equals(userId)
        );

        if (!removed) {
            throw new IllegalArgumentException("신청 내역이 존재하지 않습니다.");
        }

        // 변경된 Meeting 객체를 저장하여 삭제된 참가자 정보 반영
        meetingRepository.save(meeting);
    }

    public List<UserResponseDTO.IndivProfileWithStatusDTO> getMeetingParticipants(Long meetingId) {
        Meeting meeting = meetingRepository.findById(meetingId)
                .orElseThrow(() -> new IllegalArgumentException("좌담회 정보를 찾을 수 없습니다."));

        Map<Long, ParticipantStatus> participantStatusMap = meeting.getParticipantStatus().stream()
                .collect(Collectors.toMap(Meeting.Participant::getUserId, Meeting.Participant::getStatus));

        return indivRepository.findAllById(participantStatusMap.keySet()).stream()
                .map(indiv -> new UserResponseDTO.IndivProfileWithStatusDTO(
                        indiv.getId(),
                        indiv.getName(),
                        indiv.getNickname(),
                        calculateAge(indiv.getBirthDate()),
                        indiv.getGender(),
                        indiv.getContact(),
                        indiv.getCategoryId(),
                        participantStatusMap.get(indiv.getId()) // Retrieve status from the map
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
            boolean isPendingOrApproved = meeting.getParticipantStatus().stream()
                    .anyMatch(p -> p.getUserId().equals(userId) &&
                            (p.getStatus() == ParticipantStatus.PENDING || p.getStatus() == ParticipantStatus.APPROVED));

            if (isPendingOrApproved && meeting.getMeetingDate().equals(newMeeting.getMeetingDate()) &&
                    ((newMeeting.getMeetingTimeStart().isBefore(meeting.getMeetingTimeEnd()) &&
                            newMeeting.getMeetingTimeEnd().isAfter(meeting.getMeetingTimeStart())))) {
                return true;
            }
        }
        return false;
    }

    @Transactional
    public void approveParticipant(Long meetingId, Long participantId, Long corpId) {
        Meeting meeting = meetingRepository.findById(meetingId)
                .orElseThrow(() -> new IllegalArgumentException("좌담회 정보를 찾을 수 없습니다."));

        // 기업이 해당 좌담회를 소유하고 있는지 확인
        if (!meeting.getCorpId().equals(corpId)) {
            throw new IllegalArgumentException("해당 좌담회에 대한 권한이 없습니다.");
        }

        // 신청자의 상태를 승인 상태로 변경
        Meeting.Participant participant = meeting.getParticipantStatus().stream()
                .filter(p -> p.getUserId().equals(participantId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("참가자를 찾을 수 없습니다."));

        participant.setStatus(ParticipantStatus.APPROVED);
        meetingRepository.save(meeting);
    }

    @Transactional
    public void rejectParticipant(Long meetingId, Long participantId, Long corpId) {
        Meeting meeting = meetingRepository.findById(meetingId)
                .orElseThrow(() -> new IllegalArgumentException("좌담회 정보를 찾을 수 없습니다."));

        if (!meeting.getCorpId().equals(corpId)) {
            throw new IllegalArgumentException("해당 좌담회에 대한 권한이 없습니다.");
        }

        Meeting.Participant participant = meeting.getParticipantStatus().stream()
                .filter(p -> p.getUserId().equals(participantId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("참가자를 찾을 수 없습니다."));

        participant.setStatus(ParticipantStatus.REJECTED);
        meetingRepository.save(meeting);
    }
}

