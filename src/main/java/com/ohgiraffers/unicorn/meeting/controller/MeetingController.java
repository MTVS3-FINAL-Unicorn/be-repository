package com.ohgiraffers.unicorn.meeting.controller;

import com.ohgiraffers.unicorn.meeting.dto.MeetingDTO;
import com.ohgiraffers.unicorn.meeting.entity.Meeting;
import com.ohgiraffers.unicorn.meeting.service.MeetingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/meeting")
public class MeetingController {

    @Autowired
    private MeetingService meetingService;

    @GetMapping
    public ResponseEntity<List<Meeting>> getAllMeetings() {
        List<Meeting> meetings = meetingService.getAllMeetings();
        return ResponseEntity.ok(meetings);
    }

    @GetMapping("/{meetingId}")
    public ResponseEntity<Meeting> getMeetingById(@PathVariable Long meetingId) {
        Meeting meeting = meetingService.getMeetingById(meetingId);
        return ResponseEntity.ok(meeting);
    }

    @PostMapping("/{corpId}")
    public ResponseEntity<Meeting> createMeeting(@PathVariable Long corpId, @RequestBody MeetingDTO meeting) {
        meeting.setCorpId(corpId);
        Meeting createdMeeting = meetingService.createMeeting(meeting);
        return ResponseEntity.status(201).body(createdMeeting);
    }

    @PutMapping("/{meetingId}")
    public ResponseEntity<Meeting> updateMeeting(@PathVariable Long meetingId, @RequestBody MeetingDTO meetingDTO) {
        Meeting updatedMeeting = meetingService.updateMeeting(meetingId, meetingDTO);
        return ResponseEntity.ok(updatedMeeting);
    }

    @DeleteMapping("/{meetingId}")
    public ResponseEntity<Void> softDeleteMeeting(@PathVariable Long meetingId) {
        meetingService.softDeleteMeeting(meetingId);
        return ResponseEntity.noContent().build();
    }
}

