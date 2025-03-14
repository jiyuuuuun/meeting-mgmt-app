package com.example.meetingmanagement.repository;

import com.example.meetingmanagement.entity.Meeting;
import com.example.meetingmanagement.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MeetingRepository extends JpaRepository<Meeting, Long> {
    @Override
    Optional<Meeting> findById(Long aLong);
    Optional<List<Schedule>> findScheduleByMeetingId(Long meetingId);
}
