package com.example.meetingmanagement.controller;

import com.example.meetingmanagement.dto.UserResponseDTO;
import com.example.meetingmanagement.entity.Meeting;
import com.example.meetingmanagement.entity.User;
import com.example.meetingmanagement.service.MeetingService;
import com.example.meetingmanagement.service.UserService;
import com.example.meetingmanagement.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/meetings")
@RequiredArgsConstructor
@Slf4j
public class MeetingController {
    private final MeetingService meetingService;
    private final JwtUtil jwtUtil;
    private final UserService userService;

    @PostMapping
    public ResponseEntity<?> createMeeting(@RequestBody Meeting meeting, HttpServletRequest request) {
        String token=request.getHeader("Authorization");
        log.info("token:"+token);
        User user=userService.getUser(token);
        meetingService.createMeeting(meeting, user);
        return ResponseEntity.ok().build();
    }
    @GetMapping
    public ResponseEntity<List<Meeting>> getAllMeetings() {
        return ResponseEntity.ok().body(meetingService.getAllMeetings());

    }
    @PutMapping("/{meetingId}")
    private ResponseEntity<Meeting> updateMeeting(@PathVariable(name = "meetingId") Long meetingId,@RequestBody Meeting meeting ,HttpServletRequest request) {
        String token=request.getHeader("Authorization");
        log.info("token:"+token);
        User user=userService.getUser(token);
        if(meetingService.updateMeeting(meeting,user,meetingId)){
            Meeting updateMeeting=meetingService.findMeeting(meetingId);
            return ResponseEntity.ok().body(updateMeeting);
        }else {
            return ResponseEntity.status(401).build();
        }
    }
    @DeleteMapping("/{meetingId}")
    private ResponseEntity<?> deleteMeeting(@PathVariable(name = "meetingId") Long meetingId, HttpServletRequest request) {
        String token=request.getHeader("Authorization");
        log.info("token:"+token);
        User user=userService.getUser(token);
        Meeting meeting=meetingService.findMeeting(meetingId);
        if(meetingService.deleteMeeting(meeting,user)){
            return ResponseEntity.ok().build();
        }else {
            return ResponseEntity.status(401).build();
        }
    }

    @PostMapping("/{meetingId}/join")
    private ResponseEntity<?> joinMeeting(@PathVariable(name = "meetingId") Long meetingId, HttpServletRequest request) {
        String token=request.getHeader("Authorization");
        log.info("token:"+token);
        User user=userService.getUser(token);
        Meeting meeting=meetingService.findMeeting(meetingId);
        if(meetingService.joinMeeting(meeting,user)){
            return ResponseEntity.ok().build();
        }else{
            return ResponseEntity.status(401).build();
        }
    }
    @GetMapping("/{meetingId}/participants")
    public ResponseEntity<List<UserResponseDTO>> getParticipants(@PathVariable(name = "meetingId") Long meetingId) {

        return ResponseEntity.ok().body(meetingService.getMeetingUser(meetingService.findMeeting(meetingId)));
    }
}
