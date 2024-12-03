package com.ohgiraffers.unicorn.meeting.controller;

import com.ohgiraffers.unicorn.auth.dto.UserResponseDTO;
import com.ohgiraffers.unicorn.auth.repository.CorpRepository;
import com.ohgiraffers.unicorn.meeting.dto.IndivMeetingDTO;
import com.ohgiraffers.unicorn.meeting.dto.MeetingDTO;
import com.ohgiraffers.unicorn.meeting.entity.Meeting;
import com.ohgiraffers.unicorn.meeting.repository.MeetingRepository;
import com.ohgiraffers.unicorn.meeting.service.MeetingService;
import com.ohgiraffers.unicorn._core.utils.ApiUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.ohgiraffers.unicorn._core.utils.SecurityUtils.getCurrentUserId;

@RestController
@RequestMapping("/api/v1/meeting")
public class MeetingController {

    @Autowired
    private MeetingService meetingService;

    @Autowired
    private MeetingRepository meetingRepository;
    @Autowired
    private CorpRepository corpRepository;

    @GetMapping
    public ResponseEntity<List<MeetingDTO>> getAllMeetings() {
        List<MeetingDTO> meetings = meetingService.getAllMeetings();
        return ResponseEntity.ok(meetings);
    }

    @GetMapping("/{meetingId}")
    public ResponseEntity<MeetingDTO> getMeetingById(@PathVariable("meetingId") Long meetingId) {
        MeetingDTO meeting = meetingService.getMeetingById(meetingId);
        List<UserResponseDTO.IndivProfileWithStatusDTO> participants = meetingService.getMeetingParticipants(meetingId);
        meeting.setParticipants(participants);
        return ResponseEntity.ok(meeting);
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<MeetingDTO>> getMeetingByCategory(@PathVariable("categoryId") Long categoryId) {
        List<MeetingDTO> meetings = meetingService.getMeetingsByCategoryId(categoryId);
        return ResponseEntity.ok(meetings);
    }

    @GetMapping("/individual")
    public ResponseEntity<List<IndivMeetingDTO>> getMeetingsByIndivId() {
        Long indivId = getCurrentUserId();
        List<IndivMeetingDTO> meetings = meetingService.getMeetingsByIndivId(indivId);
        return ResponseEntity.ok(meetings);
    }

    @GetMapping("/corporate")
    public ResponseEntity<List<MeetingDTO>> getMeetingsByCorpId() {
        Long corpId = getCurrentUserId();
        List<MeetingDTO> meetings = meetingService.getMeetingsByCorpId(corpId);
        return ResponseEntity.ok(meetings);
    }

    @PostMapping
    public ResponseEntity<Meeting> createMeeting(@RequestBody MeetingDTO meeting) {
        meeting.setCorpId(getCurrentUserId());
        meeting.setCategoryId(corpRepository.findById(getCurrentUserId()).get().getCategoryId());
        Meeting createdMeeting = meetingService.createMeeting(meeting);
        return ResponseEntity.status(201).body(createdMeeting);
    }

    @PutMapping
    public ResponseEntity<Meeting> updateMeeting(
            @RequestBody MeetingDTO meetingDTO) {
        Meeting updatedMeeting = meetingService.updateMeeting(meetingDTO);
        return ResponseEntity.ok(updatedMeeting);
    }

    @DeleteMapping("/{meetingId}")
    public ResponseEntity<Void> softDeleteMeeting(@PathVariable Long meetingId) {

        Long meetingCorpId = meetingRepository.findById(meetingId).get().getCorpId();

        if (meetingCorpId != null && meetingCorpId.equals(getCurrentUserId())) {
            meetingService.softDeleteMeeting(meetingId);
        } else {
            return ResponseEntity.status(403).build();
        }

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{meetingId}/join")
    public ResponseEntity<?> joinMeeting(@PathVariable("meetingId") Long meetingId) {
        meetingService.joinMeeting(meetingId, getCurrentUserId());
        return ResponseEntity.ok(ApiUtils.success("좌담회 신청이 완료되었습니다."));
    }

    @PostMapping("/{meetingId}/cancel")
    public ResponseEntity<?> cancelMeeting(@PathVariable("meetingId") Long meetingId) {
        meetingService.cancelMeeting(meetingId, getCurrentUserId());
        return ResponseEntity.ok(ApiUtils.success("좌담회 신청이 취소되었습니다."));
    }

    @PostMapping("/{meetingId}/reject/{participantId}")
    public ResponseEntity<?> rejectParticipant(
            @PathVariable("meetingId") Long meetingId,
            @PathVariable("participantId") Long participantId){
        Long corpId = getCurrentUserId();
        meetingService.rejectParticipant(meetingId, participantId, corpId);
        return ResponseEntity.ok(ApiUtils.success("참가 신청이 거절되었습니다."));
    }

    @PostMapping("/{meetingId}/approve/{participantId}")
    public ResponseEntity<?> approveParticipant(
            @PathVariable("meetingId") Long meetingId,
            @PathVariable("participantId") Long participantId) {
        Long corpId = getCurrentUserId();
        meetingService.approveParticipant(meetingId, participantId, corpId);
        return ResponseEntity.ok(ApiUtils.success("참가 신청이 승인되었습니다."));
    }

}

