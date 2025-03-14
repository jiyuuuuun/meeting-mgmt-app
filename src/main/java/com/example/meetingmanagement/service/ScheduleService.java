package com.example.meetingmanagement.service;

import com.example.meetingmanagement.dto.UserResponseDTO;
import com.example.meetingmanagement.entity.Schedule;
import com.example.meetingmanagement.entity.ScheduleParticipant;
import com.example.meetingmanagement.entity.User;
import com.example.meetingmanagement.repository.MeetingParticipantRepository;
import com.example.meetingmanagement.repository.MeetingRepository;
import com.example.meetingmanagement.repository.ScheduleParticipantRepository;
import com.example.meetingmanagement.repository.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

@Service
@RequiredArgsConstructor
@Slf4j
public class ScheduleService {
    private final ScheduleRepository scheduleRepository;
    private final MeetingRepository meetingRepository;
    private final MeetingParticipantRepository meetingParticipantRepository;
    private final ScheduleParticipantRepository scheduleParticipantRepository;

    //일정 만들기
    @Transactional
    public boolean createSchedule(Schedule schedule, User user,Long meetingId) {
        AtomicBoolean isUserParticipant = new AtomicBoolean(false);
        meetingRepository.findById(meetingId).ifPresent(meeting -> {
             isUserParticipant.set(meeting.getParticipants().stream()
                     .anyMatch(p -> p.getUser().getUserId() == user.getUserId()));
        });
        if (!isUserParticipant.get()) {
            log.info("사용자가 모임 참가자가 아닙니다.");
            return false;
        }
        schedule.setMeeting(meetingRepository.findById(meetingId).get());
        ScheduleParticipant scheduleParticipant = new ScheduleParticipant();
        scheduleParticipant.setSchedule(schedule);
        scheduleParticipant.setUser(user);
        schedule.getParticipants().add(scheduleParticipant);
        log.info(schedule.toString());
        scheduleRepository.save(schedule);

        return true;
    }
    //일정 목록 조회
    @Transactional(readOnly = true)
    public List<Schedule> getSchedulesByMeetingId(Long meetingId) {
        List<Schedule> schedules=new ArrayList<>();
        meetingRepository.findById(meetingId).ifPresent(meeting -> {
           meeting.getSchedules().forEach(schedule -> {
               schedules.add(schedule);
           });
        });

        return schedules;
    }
    //일정 참가
    @Transactional
    public boolean joinSchedule(User user, Long meetingId,Long scheduleId) {
        AtomicBoolean isUserParticipant = new AtomicBoolean(false);
        meetingRepository.findById(meetingId).ifPresent(meeting -> {
            isUserParticipant.set(meeting.getParticipants().stream()
                    .anyMatch(p -> p.getUser().getUserId() == user.getUserId()));
        });
        if (!isUserParticipant.get()) {
            log.info("사용자가 모임 참가자가 아닙니다.");
            return false;
        }
        scheduleRepository.findById(scheduleId).ifPresent(schedule -> {
            ScheduleParticipant scheduleParticipant = new ScheduleParticipant();
            scheduleParticipant.setSchedule(schedule);
            scheduleParticipant.setUser(user);
            schedule.getParticipants().add(scheduleParticipant);

        });
        return true;
    }
    //일정 탈퇴
    @Transactional
    public boolean leaveSchedule(User user, Long meetingId,Long scheduleId) {
        AtomicBoolean isUserParticipant = new AtomicBoolean(false);
        meetingRepository.findById(meetingId).ifPresent(meeting -> {
            isUserParticipant.set(meeting.getParticipants().stream()
                    .anyMatch(p -> p.getUser().getUserId() == user.getUserId()));
        });
        if (!isUserParticipant.get()) {
            log.info("사용자가 모임 참가자가 아닙니다.");
            return false;
        }
        AtomicReference<Long> schedulePartId= new AtomicReference<>(0L);
        scheduleRepository.findById(scheduleId).ifPresent(schedule -> {
            schedule.getParticipants().forEach(scheduleParticipant -> {
                if (scheduleParticipant.getUser().getUserId() == user.getUserId()) {
                    schedulePartId.set(scheduleParticipant.getSchedulePartId());
                }
            });
            scheduleParticipantRepository.findBySchedulePartId(schedulePartId.get()).ifPresent(scheduleParticipant -> {
                schedule.getParticipants().remove(scheduleParticipant);
            });

        });
        return true;
    }
    //특정 일정 참가자 목록 조회
    @Transactional
    public List<UserResponseDTO> getParticipants(Long scheduleId) {
        List<UserResponseDTO> userResponseDTOs = new ArrayList<>();
        scheduleRepository.findById(scheduleId).ifPresent(schedule -> {
            schedule.getParticipants().forEach(scheduleParticipant -> {
                UserResponseDTO userResponseDTO = UserResponseDTO.builder()
                        .name(scheduleParticipant.getUser().getUserName())
                        .id(scheduleParticipant.getUser().getUserId())
                        .build();
                userResponseDTOs.add(userResponseDTO);
            });
        });
        return userResponseDTOs;
    }
}
