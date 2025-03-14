package com.example.meetingmanagement.controller;

import com.example.meetingmanagement.dto.ErrorResponseDTO;
import com.example.meetingmanagement.dto.UserResponseDTO;
import com.example.meetingmanagement.entity.Meeting;
import com.example.meetingmanagement.entity.Schedule;
import com.example.meetingmanagement.entity.User;
import com.example.meetingmanagement.service.MeetingService;
import com.example.meetingmanagement.service.ScheduleService;
import com.example.meetingmanagement.service.UserService;
import com.example.meetingmanagement.util.JwtUtil;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "모임 API", description = "모임 관련 API")
public class MeetingController {
    private final MeetingService meetingService;
    private final ScheduleService scheduleService;
    private final UserService userService;


    @PostMapping
    @Operation(
            summary = "모임 생성",
            description = "새로운 모임을 생성합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "모임 생성 성공"),
                    @ApiResponse(responseCode = "401", description = "인증 실패", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
            }
    )
    public ResponseEntity<?> createMeeting(
            @RequestBody @Parameter(description = "생성할 모임 정보") Meeting meeting,
            HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        log.info("token:" + token);
        User user = userService.getUser(token);
        meetingService.createMeeting(meeting, user);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    @Operation(summary = "모든 모임 조회", description = "등록된 모든 모임을 조회합니다.")
    public ResponseEntity<List<Meeting>> getAllMeetings() {
        return ResponseEntity.ok().body(meetingService.getAllMeetings());
    }

    @PutMapping("/{meetingId}")
    @Operation(
            summary = "모임 정보 수정",
            description = "모임 ID를 기준으로 정보를 수정합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "모임 수정 성공"),
                    @ApiResponse(responseCode = "401", description = "인증 실패", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
            }
    )
    private ResponseEntity<Meeting> updateMeeting(
            @PathVariable(name = "meetingId") @Parameter(description = "수정할 모임 ID") Long meetingId,
            @RequestBody @Parameter(description = "수정할 모임 정보") Meeting meeting,
            HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        log.info("token:" + token);
        User user = userService.getUser(token);
        if (meetingService.updateMeeting(meeting, user, meetingId)) {
            Meeting updatedMeeting = meetingService.findMeeting(meetingId);
            return ResponseEntity.ok().body(updatedMeeting);
        } else {
            return ResponseEntity.status(401).build();
        }
    }

    @DeleteMapping("/{meetingId}")
    @Operation(
            summary = "모임 삭제",
            description = "모임 ID를 기준으로 모임을 삭제합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "모임 삭제 성공"),
                    @ApiResponse(responseCode = "401", description = "인증 실패", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
            }
    )
    private ResponseEntity<?> deleteMeeting(
            @PathVariable(name = "meetingId") @Parameter(description = "삭제할 모임 ID") Long meetingId,
            HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        log.info("token:" + token);
        User user = userService.getUser(token);
        Meeting meeting = meetingService.findMeeting(meetingId);
        if (meetingService.deleteMeeting(meeting, user)) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(401).build();
        }
    }

    @PostMapping("/{meetingId}/join")
    @Operation(
            summary = "모임 참가",
            description = "모임 ID를 기준으로 모임에 참가합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "모임 참가 성공"),
                    @ApiResponse(responseCode = "401", description = "인증 실패", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
            }
    )
    private ResponseEntity<?> joinMeeting(
            @PathVariable(name = "meetingId") @Parameter(description = "참가할 모임 ID") Long meetingId,
            HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        log.info("token:" + token);
        User user = userService.getUser(token);
        Meeting meeting = meetingService.findMeeting(meetingId);
        if (meetingService.joinMeeting(meeting, user)) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(401).build();
        }
    }

    @GetMapping("/{meetingId}/participants")
    @Operation(summary = "모임 참가자 조회", description = "모임에 참가한 유저 목록을 조회합니다.")
    public ResponseEntity<List<UserResponseDTO>> getMeetingParticipants(
            @PathVariable(name = "meetingId") @Parameter(description = "참가자를 조회할 모임 ID") Long meetingId) {
        return ResponseEntity.ok().body(meetingService.getMeetingUser(meetingService.findMeeting(meetingId)));
    }
    @PostMapping("/{meetingID}/schedules")
    public ResponseEntity<?> addSchedule(@PathVariable(name = "meetingID") Long meetingID,HttpServletRequest request,@RequestBody Schedule schedule){
        String token = request.getHeader("Authorization");
        log.info("token:" + token);
        User user = userService.getUser(token);
        if(scheduleService.createSchedule(schedule,user,meetingID)) {
            return ResponseEntity.ok().build();
        }else{
            return ResponseEntity.status(401).build();
        }
    }
    @GetMapping("/{meetingID}/schedules")
    public ResponseEntity<List<Schedule>> getSchedules(@PathVariable(name = "meetingID") Long meetingID) {
        List<Schedule> schedules=scheduleService.getSchedulesByMeetingId(meetingID);
        return ResponseEntity.ok().body(schedules);
    }
    @PostMapping("/{meetingID}/schedules/{scheduleId}/join")
    public ResponseEntity<?> joinSchedule(@PathVariable(name = "meetingID") Long meetingID,@PathVariable(name = "scheduleId") Long scheduleId,HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        log.info("token:" + token);
        User user = userService.getUser(token);
       if(scheduleService.joinSchedule(user,meetingID,scheduleId)){
           return ResponseEntity.ok().build();
       }else{
           return ResponseEntity.status(401).build();
       }
    }
    @DeleteMapping("/{meetingID}/schedules/{scheduleId}/leave")
    public ResponseEntity<?> leaveSchedule(@PathVariable(name = "meetingID") Long meetingID,@PathVariable(name = "scheduleId") Long scheduleId,HttpServletRequest request){
        String token = request.getHeader("Authorization");
        log.info("token:" + token);
        User user = userService.getUser(token);
        if(scheduleService.leaveSchedule(user,meetingID,scheduleId)){
            return ResponseEntity.ok().build();
        }else {
            return ResponseEntity.status(401).build();
        }
    }
    @GetMapping("/schedules/{scheduleId}/participants")
    public ResponseEntity<List<UserResponseDTO>> getScheduleParticipants(@PathVariable(name = "scheduleId") Long scheduleId){
        List<UserResponseDTO> userResponseDTOS=scheduleService.getParticipants(scheduleId);
        return ResponseEntity.ok().body(userResponseDTOS);
    }


}