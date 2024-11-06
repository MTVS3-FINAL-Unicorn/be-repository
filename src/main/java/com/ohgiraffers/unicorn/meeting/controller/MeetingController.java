package com.ohgiraffers.unicorn.meeting.controller;

import com.ohgiraffers.unicorn.meeting.dto.MeetingDTO;
import com.ohgiraffers.unicorn.meeting.entity.Meeting;
import com.ohgiraffers.unicorn.meeting.repository.MeetingRepository;
import com.ohgiraffers.unicorn.meeting.service.MeetingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.ohgiraffers.unicorn.utils.SecurityUtils.getCurrentUserId;

@RestController
@RequestMapping("/api/v1/meeting")
public class MeetingController {

    @Autowired
    private MeetingService meetingService;
    private MeetingRepository meetingRepository;

    @GetMapping
    public ResponseEntity<List<MeetingDTO>> getAllMeetings() {
        List<MeetingDTO> meetings = meetingService.getAllMeetings();
        return ResponseEntity.ok(meetings);
    }

    @GetMapping("/{meetingId}")
    public ResponseEntity<MeetingDTO> getMeetingById(@PathVariable Long meetingId) {
        MeetingDTO meeting = meetingService.getMeetingById(meetingId);
        return ResponseEntity.ok(meeting);
    }

    @PostMapping("/{corpId}")
    public ResponseEntity<Meeting> createMeeting(@RequestBody MeetingDTO meeting) {
        meeting.setCorpId(getCurrentUserId());
        Meeting createdMeeting = meetingService.createMeeting(meeting);
        return ResponseEntity.status(201).body(createdMeeting);
    }

    @PutMapping("/{meetingId}")
    public ResponseEntity<Meeting> updateMeeting(@RequestBody MeetingDTO meetingDTO) {
        Meeting updatedMeeting = meetingService.updateMeeting(getCurrentUserId(), meetingDTO);
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
}

